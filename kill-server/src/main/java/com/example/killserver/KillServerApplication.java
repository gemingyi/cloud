package com.example.killserver;

import com.example.platformboot.BootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@MapperScan("com.example.killserver.dao")
public class KillServerApplication extends BootApplication {


    public static void main(String[] args) {

        SpringApplication.run(KillServerApplication.class, args);
    }

}
