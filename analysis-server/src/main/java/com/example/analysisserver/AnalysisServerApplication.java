package com.example.analysisserver;

import com.example.platformboot.BootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan({"com.example.analysisserver.dao.mapper", "com.example.pluginmq.dao.mapper"})
public class AnalysisServerApplication extends BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalysisServerApplication.class, args);
    }

}
