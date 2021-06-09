package com.yangzhuo;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.yangzhuo.utils.SnowFlake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDubboConfiguration
@Configuration
public class OrderApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(OrderApplication.class, args);
    }
    
    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(1, 1);
    }
}
