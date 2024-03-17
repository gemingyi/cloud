package com.example.dddapplication.service;

public interface ICommonService {

    /**
     * 生成图片验证码
     */
    String generateValidateCode(String token) throws Exception;

    /**
     * 校验图片验证码
     */
    boolean checkValidateCode(String token, String validateCode);

}
