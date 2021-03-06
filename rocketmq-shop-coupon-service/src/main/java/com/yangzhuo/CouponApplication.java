package com.yangzhuo;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class CouponApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(CouponApplication.class, args);
    }
}
