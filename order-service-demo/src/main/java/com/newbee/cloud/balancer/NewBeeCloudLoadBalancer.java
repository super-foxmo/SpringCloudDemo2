package com.newbee.cloud.balancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NewBeeCloudLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private String serviceName;

    public NewBeeCloudLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceName) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceName = serviceName;
    }

    private AtomicInteger atomicCount = new AtomicInteger(0);

    private AtomicInteger atomicCurrentIndex = new AtomicInteger(0);

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();

        return supplier.get().next().map(this::getInstanceResponse);
    }

    /**
     * 自定已负载均衡方式，获取服务实例
     * @param instances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances){
        ServiceInstance serviceInstance = null;

        if (instances.isEmpty()){
            System.out.println("服务注册中心无可用服务实例：" + serviceName);
            return new EmptyResponse();
        }

        // 累加并得到值 请求次数
        int requestNumber = atomicCount.incrementAndGet();

        //自定义算法
        if (requestNumber < 2) {
            serviceInstance = instances.get(atomicCurrentIndex.get());
        } else {
            // 已经大于2了 重置
            atomicCount = new AtomicInteger(0);

            // atomicCurrentIndex变量加1
            atomicCurrentIndex.incrementAndGet();

            if (atomicCurrentIndex.get() >= instances.size()) {
                atomicCurrentIndex = new AtomicInteger(0);
                serviceInstance = instances.get(instances.size() - 1);
                return new DefaultResponse(serviceInstance);
            }
            //从可用的实例中，获取一个实例来进行操作，类似轮询算法
            serviceInstance = instances.get(atomicCurrentIndex.get() - 1);
        }
        return new DefaultResponse(serviceInstance);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose() {
        return ReactorServiceInstanceLoadBalancer.super.choose();
    }
}
