package com.newbee.cloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class GoodsIdGatewayFilterFactory extends AbstractGatewayFilterFactory<GoodsIdGatewayFilterFactory.Config> {

    public GoodsIdGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // 定义配置文件中的参数项（最大值和最小值）
        return Arrays.asList("minValue", "maxValue");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //获取参数值
                String goodsIdParam = exchange.getRequest().getQueryParams().getFirst("goodsId");
                //判空
                if (!StringUtils.isEmpty(goodsIdParam)) {
                    int goodsId = Integer.parseInt(goodsIdParam);
                    if (goodsId > config.getMinValue() && goodsId < config.getMaxValue()) {
                        //判断goodsId是否再配置区间内，直接放行
                        return chain.filter(exchange);
                    } else {
                        // 不符合条件，返回错误的提示信息，不进行后续的路由
                        byte[] bytes = ("BAD REQUEST").getBytes(StandardCharsets.UTF_8);
                        DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
                        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                        return exchange.getResponse().writeWith(Flux.just(wrap));
                    }
                }
                //直接放行
                return chain.filter(exchange);
            }
        };
    }

    public static class Config {
        private int minValue;

        private int maxValue;

        public int getMinValue() {
            return minValue;
        }

        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
    }
}
