package com.newbee.cloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//自定义全局过滤器（不用在配置文件中做额外的配置，所有请求都会使用到它。）
@Component
public class TimeCalculateGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求开始时间
        long startTime = System.currentTimeMillis();
        String requestURL = String.format("Host:%s  Port:%s  Path:%s  Params:%s",
                exchange.getRequest().getURI().getHost(),
                exchange.getRequest().getURI().getPort(),
                exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getQueryParams());

        System.out.println(requestURL);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //获取请求结束时间
            long endTime = System.currentTimeMillis();
            //打印当前请求花费的时间
            long requestTime = endTime - startTime;

            System.out.println(exchange.getRequest().getURI().getPath() + "请求花费时间：" + requestTime);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
