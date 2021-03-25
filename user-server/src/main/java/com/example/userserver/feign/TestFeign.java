package com.example.userserver.feign;

import com.example.commons.result.RestResult;
import com.example.userserver.feign.fallback.TestFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 远程调用
 */
//@FeignClient(value = "common-server", path = "test")
@FeignClient(value = "common-server", path = "test", fallbackFactory = TestFeignFallbackFactory.class)
public interface TestFeign {


    @RequestMapping("test1")
    RestResult<Object> test1();

    @RequestMapping("test2")
    RestResult<Object> test2();


    @PostMapping("seata")
    RestResult<Object> seataTest();


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RestResult<Object> testImport(@RequestPart(value = "file", required = false) MultipartFile file);


    @GetMapping(value = "/checkValidateCode")
    RestResult<Object> checkValidateCode(@RequestParam("token") String token, @RequestParam("validateCode")String validateCode);
}
