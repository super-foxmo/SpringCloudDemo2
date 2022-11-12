package com.newbee.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class NewBeeCloudShopCartController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/shop-cart/detail")
    public String cartItemDetail(@RequestParam("cartId") int cartId) {
        // 根据id查询商品并返回给调用端
        if (cartId < 0 || cartId > 1000) {
            return "查询购物项为空，当前服务的端口号为" + serverPort;
        }
        String cartItem = "购物项" + cartId;
        // 返回信息给调用端
        return cartItem + "，当前服务的端口号为" + serverPort;
    }

    @GetMapping("/shop-cart/page/{pageNum}")
    public String cartItemList(@PathVariable("pageNum") int pageNum) throws InterruptedException {
        // 返回信息给调用端
        return "请求cartItemList，当前服务的端口号为" + serverPort;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/shop-cart/getGoodsId")
    public int getGoodsId(@RequestParam("cartId") int cartId) {
        // 根据主键id查询购物表
        Map<String, Object> cartItemObject = jdbcTemplate.queryForMap("select * from tb_cart_item where cart_id=" + cartId + " limit 1");
        if (cartItemObject.containsKey("goods_id")) {
            // 返回商品id
            return (int) cartItemObject.get("goods_id");
        }
        return 0;
    }

    @DeleteMapping("/shop-cart/{cartId}")
    @Transactional
    public Boolean deleteItem(@PathVariable("cartId") int cartId) {
        // 删除购物车数据
        int result = jdbcTemplate.update("delete from tb_cart_item where cart_id=" + cartId);

//        // 此处出现了异常
//        int i=1/0;

        if (result > 0) {
            return true;
        }
        return false;
    }
}
