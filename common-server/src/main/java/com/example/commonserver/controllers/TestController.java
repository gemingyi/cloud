package com.example.commonserver.controllers;

import com.example.commons.exceptionHandle.exceptions.DataConflictException;
import com.example.commons.result.RestResult;
import com.example.commonserver.service.ITestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation("测试远程文件上传")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResult<Object> testImport(@RequestPart(value = "file", required = false) MultipartFile file) {
        System.out.println(file.getName());
        return RestResult.success();
    }

}
