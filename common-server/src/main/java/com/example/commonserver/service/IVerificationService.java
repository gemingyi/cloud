package com.example.commonserver.service;

import com.example.commons.result.RestResult;

public interface IVerificationService {

    /**
     * 生成图片验证码
     */
    RestResult<Object> generateValidateCode() throws Exception;

    /**
     * 校验图片验证码
     */
    RestResult<Boolean> checkValidateCode(String token, String validateCode);

}
