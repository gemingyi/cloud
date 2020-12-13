package com.example.commonserver.feign;

import com.example.commons.jwt.JWTToken;
import com.example.commons.jwt.JWTUtil;
import com.example.commons.result.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {


    /**
     * 登录
     */
    @PostMapping("login")
    public RestResult<JWTToken> login(String userName, String password) {
        //查询数据库 验证账号密码
        String account = "admin";
        String roles = "admin";
        //
        JWTToken jwtToken = JWTUtil.sign(account, roles);
        return RestResult.success(jwtToken);
    }

    /**
     * 刷新token
     */
    @GetMapping("refresh")
    public RestResult<JWTToken> refresh(String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        JWTToken jwtToken = JWTUtil.refresh(token);
        return RestResult.success(jwtToken);
    }

    /**
     * 检查token
     */
    @GetMapping("check")
    public RestResult<Object> check(@RequestParam("token") String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        return RestResult.success();
    }

    /**
     * token信息
     */
    @GetMapping("info")
    public RestResult<Object> info(@RequestParam("token") String token) {
        boolean flag = JWTUtil.verify(token);
        if (!flag) {
            return RestResult.failure("token error!");
        }
        String currentUser = JWTUtil.getSubject(token);
        return RestResult.success(currentUser);
    }

}
