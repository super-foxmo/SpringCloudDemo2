package com.newbee.cloud.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "newbee-cloud-goods-service",path = "/goods")
public interface NewBeeGoodsDemoService {
    @PutMapping("/{goodsId}")
    Boolean deStock(@PathVariable("goodsId") int goodsId);

    @GetMapping("/goodsDetail2")
    String getGoodsDetail2(@RequestParam("goodsId") int goodsId);
}
