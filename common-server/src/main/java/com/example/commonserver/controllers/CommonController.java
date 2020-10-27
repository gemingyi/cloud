package com.example.commonserver.controllers;

import com.example.commons.result.RestResult;
import com.example.commonserver.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonController {

    @Autowired
    private ICommonService commonService;


    /**
     * 获取图片验证码
     */
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
