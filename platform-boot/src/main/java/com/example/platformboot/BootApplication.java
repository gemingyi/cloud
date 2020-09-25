package com.example.platformboot;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
* @Description 所有项目跟
* @Author mingyi ge
* @Date 2020/9/9 20:59
*/
@EnableDiscoveryClient
//@ComponentScan(
//        basePackages = {"com.example", "cn.hutool.extra.spring"},
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,pattern = "com.example.commons.*")})
@ComponentScan("com.example")
public class BootApplication {
}
