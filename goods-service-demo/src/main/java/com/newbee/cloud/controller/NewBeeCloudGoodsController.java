package com.newbee.cloud.controller;

import com.newbee.cloud.entity.NewBeeGoodsInfo;
import com.newbee.cloud.param.ComplexObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
public class NewBeeCloudGoodsController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/goods/serverPort")
    public String getServerPort(){
        return "当前Goods服务端口号：" + serverPort;
    }

    @GetMapping("/goods/detail")
    public String goodsDetail(@RequestParam("goodsId") Integer goodsId){
        if (goodsId < 0 || goodsId > 1000){
            return "查询商品为空，当前服务的端口号：" + serverPort;
        }
        String goodsName = "商品" + goodsId;

        return goodsName + ",当前服务端口号：" + serverPort;
    }

    @PostMapping("/goods/updNewBeeGoodsInfo")
    public NewBeeGoodsInfo updNewBeeGoodsInfo(@RequestBody NewBeeGoodsInfo newBeeGoodsInfo) {

        if (newBeeGoodsInfo.getGoodsId() > 0) {
            int stock = newBeeGoodsInfo.getStock();
            stock -= 1;
            //库存减一
            newBeeGoodsInfo.setStock(stock);
        }

        return newBeeGoodsInfo;
    }

    @PostMapping("/goods/testComplexObject")
    public ComplexObject testComplexObject(@RequestBody ComplexObject complexObject) {

        int requestNum = complexObject.getRequestNum();
        requestNum -= 1;
        complexObject.setRequestNum(requestNum);

        // 由于字段过多，这里就用debug的方式来查看接收到的复杂对象参数
        return complexObject;
    }

    @GetMapping("/goods/page/{pageNum}")
    public String goodsList(@PathVariable("pageNum") int pageNum) {
        // 返回信息给调用端
        return  "请求goodsList，当前服务的端口号为" + serverPort + ",pageNum = " + pageNum;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    @PutMapping("/goods/{goodsId}")
    @Transactional
    public Boolean deStock(@PathVariable("goodsId") int goodsId) {
        // 减库存操作
        int result = jdbcTemplate.update("update tb_goods set goods_stock=goods_stock-1 where goods_id=" + goodsId);

//        // 此处出现了异常
//        int i=1/0;
//        //模拟网络波动问题
//        try{
//            Thread.sleep(10 * 1000);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        if (result > 0) {
            return true;
        }
        return false;
    }

    @GetMapping("/goods/goodsDetail2")
    public String getGoodsDetail2(@RequestParam("goodsId") int goodsId) {
        // 根据id查询商品并返回给调用端
        if (goodsId < 1 || goodsId > 100000) {
            return "查询商品为空，当前服务的端口号为" + serverPort;
        }
        String goodsName = "商品" + goodsId;
        // 返回信息给调用端
        return goodsName + "，当前服务的端口号为" + serverPort;
    }
}
