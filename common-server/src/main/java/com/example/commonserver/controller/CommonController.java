package com.example.commonserver.controller;

import com.example.commons.result.RestResult;
import com.example.commonserver.service.IVerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private IVerificationService commonService;


    /**
     * 获取图片验证码
     */
    @ApiOperation("获取图片验证码")
    @GetMapping("get_validate_token")
    public RestResult<Object> getValidateToken(HttpServletResponse response) throws Exception {
        return commonService.generateValidateCode();
    }

    /**
     * 校验图片验证码
     */
    @GetMapping(value = "/checkValidateCode")
    public RestResult<Boolean> checkValidateCode(@RequestParam("token") String token, @RequestParam("validateCode")String validateCode) {
        return commonService.checkValidateCode(token, validateCode);
    }

}
