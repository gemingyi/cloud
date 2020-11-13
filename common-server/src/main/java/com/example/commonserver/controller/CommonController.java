package com.example.commonserver.controller;

import com.example.commons.result.RestResult;
import com.example.commonserver.service.ICommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private ICommonService commonService;


    /**
     * 获取图片验证码
     */
    @ApiOperation("获取图片验证码")
    @GetMapping("get_validate_token")
    public RestResult<Object> getValidateToken(HttpServletResponse response) throws Exception {
        String token = UUID.randomUUID().toString();
        //
        String imgStr = commonService.generateValidateCode(token);
        Map<String, Object> result = new HashMap<>();
        result.put("imgStr", imgStr);
        result.put("validate_token", token);
        return RestResult.success(result);
    }

}
