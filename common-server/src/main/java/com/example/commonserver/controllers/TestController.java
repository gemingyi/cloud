package com.example.commonserver.controllers;

import com.example.commons.exceptions.DataConflictException;
import com.example.commons.result.RestResult;
import com.example.commonserver.service.ITestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Api(tags = "测试接口")
public class TestController {

    @Autowired
    private ITestService testService;


    @ApiOperation("测试远程异常接口")
    @RequestMapping("test")
    public RestResult<Object> test() {
        if (true) {
            throw new DataConflictException();
        }
        return RestResult.success();
    }

    @ApiOperation("seata")
    @PostMapping("seata")
    public RestResult<Object> seataTest() {
        testService.seataTest();
        return RestResult.success();
    }

}
