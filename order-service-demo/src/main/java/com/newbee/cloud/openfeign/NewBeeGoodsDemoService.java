package com.newbee.cloud.openfeign;

import com.newbee.cloud.entity.NewBeeGoodsInfo;
import com.newbee.cloud.param.ComplexObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "newbee-cloud-goods-service",path = "/goods")
public interface NewBeeGoodsDemoService {
    @GetMapping("/serverPort")
    String getServerPort();

    @GetMapping("/detail")
    String goodsDetail(@RequestParam("goodsId") Integer goodsId);

    @PostMapping("/updNewBeeGoodsInfo")
    NewBeeGoodsInfo updNewBeeGoodsInfo(@RequestBody NewBeeGoodsInfo newBeeGoodsInfo);

    @PostMapping("/testComplexObject")
    ComplexObject testComplexObject(@RequestBody ComplexObject complexObject);

    @PutMapping(value = "/{goodsId}")
    Boolean deStock(@PathVariable(value = "goodsId") int goodsId);
}
