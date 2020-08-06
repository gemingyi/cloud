package com.example.gatewayserver.feign;

import com.example.commons.result.RestResult;
import com.example.gatewayserver.feign.fallback.AuthFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 鉴权远程调用
 */
//@FeignClient(name = "auth-server", fallbackFactory = AuthFeignServiceFallbackFactory.class)
@FeignClient(name = "auth-server")
public interface AuthFeign {

    @RequestMapping("check")
    RestResult<Object> check(String token);

    @RequestMapping("info")
    RestResult<Object> info(String token);

}
