package com.example.userserver;

import com.example.platformboot.BootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@MapperScan("com.example.userserver.dao")
public class UserServerApplication extends BootApplication {

    public UserServerApplication() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {

        SpringApplication.run(UserServerApplication.class, args);
    }

}
