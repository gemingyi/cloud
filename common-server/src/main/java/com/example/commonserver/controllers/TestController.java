package com.example.commonserver.controllers;

import com.example.commons.exceptions.DataConflictException;
import com.example.commons.result.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Api(tags = "测试接口")
public class TestController {

    @ApiOperation("测试接口")
    @RequestMapping("test")
    public RestResult<Object> refresh() {
        if (true) {
            throw new DataConflictException();
        }
        return RestResult.success();
    }
}
