package com.example.payserver;

import com.example.platformboot.BootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.payserver.dao")
public class PayServerApplication extends BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServerApplication.class, args);
    }

}
