package com.newbee.cloud.predicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义路由断言工厂处理goodsId
 */
@Component
public class GoodsIdRoutePredicateFactory extends AbstractRoutePredicateFactory<GoodsIdRoutePredicateFactory.Config> {

    public GoodsIdRoutePredicateFactory() { // 构造函数
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // 定义配置文件中的参数项（最大值和最小值）
        return Arrays.asList("minValue", "maxValue");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                // 获取goodsId参数的值
                String goodsId = exchange.getRequest().getQueryParams().getFirst("goodsId");
                if (null != goodsId) {
                    int numberId = Integer.parseInt(goodsId);
                    // 判断goodsId是否在配置区间内
                    if (numberId > config.getMinValue() && numberId < config.getMaxValue()) {
                        // 符合条件，则返回true，路由规则生效
                        return true;
                    }
                }
                // 不符合条件，则返回false，路由规则不生效
                return false;
            }

        };
    }

    @Validated
    //接收配置文件中定义的最大和最小值
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
