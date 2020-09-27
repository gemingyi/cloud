package com.example.userserver.controller;


import com.example.commons.exceptions.DataNotFoundException;
import com.example.commons.result.RestResult;
import com.example.userserver.client.TestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
@Api(tags = "测试接口")
public class TestController {

    @Autowired
    private TestClient testClient;


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


    @ApiOperation("测试异常")
    @GetMapping("/testException")
    public RestResult<Object> testException() {
        if (true) {
            throw new DataNotFoundException("找不到数据");
        }
        return RestResult.success();
    }

    @ApiOperation("测试接口")
    @GetMapping("/test2")
    public RestResult<Object> test() {
        testClient.refresh();
        return null;
    }

}
