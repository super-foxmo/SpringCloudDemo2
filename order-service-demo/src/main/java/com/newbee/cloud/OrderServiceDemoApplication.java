package com.newbee.cloud;

import com.newbee.cloud.config.NewBeeCloudLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
//使用basePackages配置了扫描包，即ltd.order.cloud.newbee.openfeign包中标注了@FeignClient注解的类都会生效
@EnableFeignClients(basePackages = {"com.newbee.cloud.openfeign"})
//对newbee-cloud-goods-service服务使用自定义的负载均衡算法
//@LoadBalancerClient(value = "newbee-cloud-goods-service",configuration = NewBeeCloudLoadBalancerConfiguration.class)
public class OrderServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceDemoApplication.class,args);
    }
}
