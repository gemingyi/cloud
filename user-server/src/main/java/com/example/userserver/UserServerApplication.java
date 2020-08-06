package com.example.userserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.userserver.dao")
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
