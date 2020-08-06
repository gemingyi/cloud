package com.example.authserver.controllers;

import com.example.commons.model.JWTToken;
import com.example.commons.utils.JWTUtil;
import com.example.commons.result.RestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {


    @PostMapping("login")
    public RestResult<JWTToken> login(String userName, String password) {
        //查询数据库 验证账号密码
        String account = "admin";
        String roles = "admin";
        //
        JWTToken jwtToken = JWTUtil.sign(account, roles);
        return RestResult.success(jwtToken);
    }

    @RequestMapping("refresh")
    public RestResult<JWTToken> refresh(String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        JWTToken jwtToken = JWTUtil.refresh(token);
        return RestResult.success(jwtToken);
    }

    @RequestMapping("check")
    public RestResult<Object> check(String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        return RestResult.success();
    }

    @RequestMapping("info")
    public RestResult<Object> info(String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        String currentUser = JWTUtil.getSubject(token);
        return RestResult.success(currentUser);
    }

}
