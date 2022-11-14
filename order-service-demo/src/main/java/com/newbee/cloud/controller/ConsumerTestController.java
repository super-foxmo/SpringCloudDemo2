package com.newbee.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.newbee.cloud.entity.NewBeeCartItem;
import com.newbee.cloud.entity.NewBeeGoodsInfo;
import com.newbee.cloud.entity.Result;
import com.newbee.cloud.openfeign.NewBeeGoodsDemoService;
import com.newbee.cloud.openfeign.NewBeeShopCartDemoService;
import com.newbee.cloud.param.ComplexObject;
import com.newbee.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ConsumerTestController {
//    private static final String CLOUD_GOODS_SERVICE_URL = "http://newbee-cloud-goods-service";
//
//    private static final String CLOUD_SHOPCART_SERVICE_URL = "http://newbee-cloud-shopcart-service";

//    @Resource
//    private RestTemplate restTemplate;

    @Resource
    private NewBeeGoodsDemoService newBeeGoodsDemoService;

    @Resource
    private NewBeeShopCartDemoService newBeeShopCartDemoService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/order/serverPort")
    public String getServerPort(){
        return newBeeGoodsDemoService.getServerPort() + "  当前Order服务器端口号："+ serverPort;
    }

    @GetMapping("/order/goodsDetail/{goodsId}")
    public String goodsDetail(@PathVariable("goodsId") Integer goodsId){
        return newBeeGoodsDemoService.goodsDetail(goodsId);
    }

    @GetMapping("/order/saveOrder/{goodsId}/{cartId}")
    public String saveOrder(@PathVariable("goodsId") Integer goodsId,
                            @PathVariable("cartId") Integer cartId){
//        String goodsResult = restTemplate.getForObject(CLOUD_GOODS_SERVICE_URL + "/goods/" + goodsId, String.class);
//        String cartResult = restTemplate.getForObject(CLOUD_SHOPCART_SERVICE_URL + "/shop-cart/" + cartId,String.class);
        // 调用商品服务
        String goodsResult = newBeeGoodsDemoService.goodsDetail(goodsId);
        // 调用购物车服务
        String cartResult = newBeeShopCartDemoService.cartItemDetail(cartId);

        return "success! goodsResult:" + goodsResult + "cartResult:" + cartResult;
    }

    @GetMapping("/order/simpleObjectTest")
    public String simpleObjectTest1() {

        NewBeeGoodsInfo newBeeGoodsInfo = new NewBeeGoodsInfo();
        newBeeGoodsInfo.setGoodsId(2022);
        newBeeGoodsInfo.setGoodsName("Spring Cloud Alibaba 微服务架构");
        newBeeGoodsInfo.setStock(2035);

        NewBeeGoodsInfo result = newBeeGoodsDemoService.updNewBeeGoodsInfo(newBeeGoodsInfo);

        return result.toString();
    }

    @GetMapping("/order/complexbjectTest")
    public String complexbjectTest() {

        ComplexObject complexObject = new ComplexObject();

        complexObject.setRequestNum(13);

        List<Integer> cartIds = new ArrayList<>();
        cartIds.add(2022);
        cartIds.add(13);
        complexObject.setCartIds(cartIds);

        NewBeeCartItem newBeeCartItem = new NewBeeCartItem();
        newBeeCartItem.setItemId(2023);
        newBeeCartItem.setCartString("newbee cloud");
        complexObject.setNewBeeCartItem(newBeeCartItem);

        List<NewBeeGoodsInfo> newBeeGoodsInfos = new ArrayList<>();
        NewBeeGoodsInfo newBeeGoodsInfo1 = new NewBeeGoodsInfo();
        newBeeGoodsInfo1.setGoodsName("Spring Cloud Alibaba 大型微服务项目实战（上册）");
        newBeeGoodsInfo1.setGoodsId(2024);
        newBeeGoodsInfo1.setStock(10000);

        NewBeeGoodsInfo newBeeGoodsInfo2 = new NewBeeGoodsInfo();
        newBeeGoodsInfo2.setGoodsName("Spring Cloud Alibaba 大型微服务项目实战（下册）");
        newBeeGoodsInfo2.setGoodsId(2025);
        newBeeGoodsInfo2.setStock(10000);
        newBeeGoodsInfos.add(newBeeGoodsInfo1);
        newBeeGoodsInfos.add(newBeeGoodsInfo2);

        complexObject.setNewBeeGoodsInfos(newBeeGoodsInfos);

        // 以上这些代码相当于平时开发时的请求参数整理

        ComplexObject result = newBeeGoodsDemoService.testComplexObject(complexObject);
        return result.toString();
    }


    /* seata测试 */
    @Resource
    private OrderService orderService;

    @SentinelResource(value = "订单流程")
    @GetMapping("/order/saveOrder")
    public Result saveOrder(@RequestParam("cartId") int cartId) {
        return orderService.saveOrder(cartId);
    }

    /* Sleuth测试 */
    @GetMapping("/order/saveOrderWithSleuth")
    public Result saveOrderWithSleuth(@RequestParam("cartId") int cartId) {
        return orderService.saveOrderWithSleuth(cartId);
    }

    @GetMapping("/order/cartDetail/{cartId}")
    public String cartDetail(@PathVariable("cartId") Integer cartId){
        return newBeeShopCartDemoService.cartItemDetail(cartId);
    }

    /* sentinel测试 */
    @GetMapping("/order/testChainApi1")
    public String testChainApi1() {
        String result = orderService.getNumber(2022);
        if ("BLOCKED".equals(result)){
            return "testChainApi1 error! "+result;
        }
        return "testChainApi1 success! "+result;
    }

    @GetMapping("/order/testChainApi2")
    public String testChainApi2() {
        String result = orderService.getNumber(2025);
        if ("BLOCKED".equals(result)){
            return "testChainApi2 error! "+result;
        }
        return "testChainApi2 success! "+result;
    }

    @GetMapping("/order/testRelateApi1")
    public String testRelateApi1() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return "testRelateApi1 error!";
        }
//        int i = 10 / 0;
        return "testRelateApi1 success!";
    }

    @GetMapping("/order/testRelateApi2")
    public String testRelateApi2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return "testRelateApi2 error!";
        }
        return "testRelateApi2 success!";
    }
}
