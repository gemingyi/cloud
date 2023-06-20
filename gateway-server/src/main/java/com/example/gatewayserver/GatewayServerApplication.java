package com.example.gatewayserver;

import com.example.commons.result.ResultCode;
import com.example.commons.result.RestResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }


//    /**
//     * 网关服务降级
//     */
//    @RestController
//    public class FallBackController {
//        @RequestMapping("/fallback")
//        public RestResult<Object> fallback() {
//            return RestResult.failure(ResultCode.SERVICE_DEMOTION);
//        }
//    }

}