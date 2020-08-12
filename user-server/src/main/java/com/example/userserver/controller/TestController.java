package com.example.userserver.controller;


import com.example.commons.result.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@Api(tags = "user接口")
public class TestController {


    @ApiOperation("上传文件")
    @PostMapping(value = "/import/excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")
    })
    public RestResult<Object> testImport(@RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println(file.getName());
        return RestResult.success();
    }

    @ApiOperation("测试接口")
    @GetMapping("/test")
    public RestResult<Object> test(@RequestParam(value = "arg", required = false) String arg) {
        System.out.println(arg);
        return RestResult.success();
    }
}
