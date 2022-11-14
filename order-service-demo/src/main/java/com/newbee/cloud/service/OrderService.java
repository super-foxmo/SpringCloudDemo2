package com.newbee.cloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.newbee.cloud.entity.Result;
import com.newbee.cloud.openfeign.NewBeeGoodsDemoService;
import com.newbee.cloud.openfeign.NewBeeShopCartDemoService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private NewBeeGoodsDemoService newBeeGoodsDemoService;

    @Resource
    private NewBeeShopCartDemoService newBeeShopCartDemoService;

    /*
        不止是微服务架构，在常见的分布式架构中，由于数据库的分割、项目代码的分割，导致事务并不在同一个事务管理器中，分布式事务的问题就出现了。
     */
//    @Transactional
    //加上这个注解，开启Seata分布式事务
    @GlobalTransactional
    public Result saveOrder(int cartId){
        // 简单的模拟下单流程，包括服务间的调用流程。

        // 调用购物车服务-获取即将操作的goods_id
        int goodsId = newBeeShopCartDemoService.getGoodsId(cartId);
        //调用商品服务-减库存
        Boolean goodsResult = newBeeGoodsDemoService.deStock(goodsId);
        //调用购物车服务-删除当前购物车数据
        Boolean cartResult = newBeeShopCartDemoService.deleteItem(cartId);

        //执行下单逻辑
        if (goodsResult && cartResult){
            //新增订单（向订单表中新增一条记录）
            int orderResult = jdbcTemplate.update("insert into tb_order(`cart_id`) value (" + cartId + ")");

//            // 此处出现了异常
//            int i=1/0;

            if (orderResult > 0){
                return new Result(200,"Success");
            }
            return new Result(400,"false");
        }
        return new Result(400,"false");
    }

    @SentinelResource(value = "getNumber")
    public String getNumber(int i) {
        if (i == 2022) {
            return "BLOCKED";
        }
        return "NO_BLOCKED";
    }

    /* sleuth测试 */
    @GlobalTransactional
    public Result saveOrderWithSleuth(int cartId){
        // 简单的模拟下单流程，包括服务间的调用流程。

        // 调用购物车服务-获取即将操作的goods_id
        int goodsId = newBeeShopCartDemoService.getGoodsId(cartId);
        //调用购物车服务-删除当前购物车数据和库存数据
        Boolean cartResult = newBeeShopCartDemoService.deleteCartAndGoods(cartId,goodsId);

        //执行下单逻辑
        if (cartResult){
            //新增订单（向订单表中新增一条记录）
            int orderResult = jdbcTemplate.update("insert into tb_order(`cart_id`) value (" + cartId + ")");

            if (orderResult > 0){
                return new Result(200,"Success");
            }
            return new Result(400,"false");
        }
        return new Result(400,"false");
    }
}
