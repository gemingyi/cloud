package com.example.commonserver.rest;

import com.example.commons.exceptionHandle.exceptions.DataConflictException;
import com.example.commons.result.RestResult;
import com.example.commonserver.service.ICommonService;
import com.example.commonserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("test")
public class TestRest {

    @Autowired
    private ITestService testService;
    @Autowired
    private ICommonService commonService;


    /**
     * 测试远程异常接口
     */
    @GetMapping("test1")
    public RestResult<Object> test1() {
        return RestResult.success();
    }

    /**
     * 测试远程异常接口
     */
    @GetMapping("test2")
    public RestResult<Object> test2() {
        if (true) {
            throw new DataConflictException();
        }
        return RestResult.success();
    }

    /**
     * 测试seata事务
     */
    @PostMapping("seata")
    public RestResult<Object> seataTest() {
        testService.seataTest();
        return RestResult.success();
    }

    @PostMapping("seata2")
    public RestResult<Object> seataTest2() {
        testService.seataTest2();
        return RestResult.success();
    }

    /**
     * 测试远程文件上传
     */
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResult<Object> testImport(@RequestPart(value = "file", required = false) MultipartFile file) {
        System.out.println(file.getName());
        return RestResult.success();
    }

    /**
     * 校验图片验证码
     */
    @GetMapping(value = "/checkValidateCode")
    public RestResult<Object> checkValidateCode(@RequestParam("token") String token, @RequestParam("validateCode")String validateCode) {
        boolean flag = commonService.checkValidateCode(token, validateCode);
        return RestResult.success(flag);
    }

}
