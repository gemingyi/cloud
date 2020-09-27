package com.example.userserver.client;

import com.example.commons.result.RestResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 鉴权远程调用
 */
//@FeignClient(name = "common-server", fallbackFactory = AuthFeignServiceFallbackFactory.class)
@FeignClient(value = "common-server", path = "test")
public interface TestClient {

    @RequestMapping("test")
    RestResult<Object> refresh();

}
