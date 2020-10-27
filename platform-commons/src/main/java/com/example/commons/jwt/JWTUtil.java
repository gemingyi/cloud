package com.example.commons.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @Description: JWT工具类
 * @Author: mingyi ge
 * @CreateDate: 2019/9/3 9:04
 */

public class JWTUtil {

    // token过期时间 一天
    private static final long expiresIn = 1000 * 60 * 60 * 24L;
    // 密钥
    private static final String secret = "#2020-06-17^^!))(><-+%";


    /**
     * 生成JWT签名
     *
     * @author: mingyi ge
     * @date: 2019/9/3 9:14
     */
    public static JWTToken sign(String account, String role) {
        try {
            Date date = new Date(System.currentTimeMillis() + expiresIn);
            //
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withSubject(account)
                    //
                    .withClaim("roles", role)
                    //签发时间
                    .withIssuedAt(new Date())
                    //过期时间
                    .withExpiresAt(date)
                    //设置签名
                    .sign(algorithm);
            JWTToken jwtToken = new JWTToken();
            jwtToken.setAccess_token(token);
            jwtToken.setToken_type("JWT");
            jwtToken.setExpires_in(expiresIn / 1000);
            return jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 刷新token
     */
    public static JWTToken refresh(String token) {
        String currentSubject = getSubject(token);
        String roles = getClaim(token, "roles");
        return sign(currentSubject, roles);
    }

    /**
     * 校验token是否正确
     *
     * @author: mingyi ge
     * @date: 2019/9/3 9:13
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
//            DecodedJWT jwt = verifier.verify(token);
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
    * @Description 获取当前用户信息（账户）
    * @Author mingyi ge
    * @Date 2020/10/23 16:27
    */
    public static String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @author: mingyi ge
     * @date: 2019/9/3 14:36
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

}
