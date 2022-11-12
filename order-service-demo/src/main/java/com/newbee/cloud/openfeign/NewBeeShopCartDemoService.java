package com.newbee.cloud.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "newbee-cloud-shopcart-service",path = "/shop-cart")
public interface NewBeeShopCartDemoService {

    @GetMapping("/detail")
    String cartItemDetail(@RequestParam("cartId") int cartId);

    @GetMapping(value = "/getGoodsId")
    int getGoodsId(@RequestParam(value = "cartId") int cartId);

    @DeleteMapping(value = "/{cartId}")
    Boolean deleteItem(@PathVariable(value = "cartId") int cartId);
}
