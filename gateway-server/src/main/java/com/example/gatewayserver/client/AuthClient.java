package com.example.gatewayserver.client;

import com.example.commons.result.RestResult;
import com.example.gatewayserver.client.fallback.AuthClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权远程调用
 */
//@FeignClient(value = "common-server", path = "auth")
@FeignClient(value = "common-server", path = "auth", fallbackFactory = AuthClientFallbackFactory.class)
public interface AuthClient {

    @GetMapping("check")
    RestResult<Object> check(@RequestParam("token") String token);

    @GetMapping("info")
    RestResult<Object> info(@RequestParam("token") String token);

}
