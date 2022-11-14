package com.newbee.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.newbee.cloud.openfeign"})
public class ShopCartServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopCartServiceDemoApplication.class,args);
    }
}
