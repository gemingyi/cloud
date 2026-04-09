package com.example.commonserver.service.impl;

import com.example.commons.result.RestResult;
import com.example.commonserver.service.IVerificationService;
import com.example.commonserver.utils.verification.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements IVerificationService {

    @Autowired
    private VerificationUtil verificationUtil;


    @Override
    public RestResult<Object> generateValidateCode() throws Exception {
        String token = UUID.randomUUID().toString();
        //
        String imgStr = verificationUtil.generateValidateCode(token);
        Map<String, Object> result = new HashMap<>();
        result.put("imgStr", imgStr);
        result.put("validate_token", token);
        return RestResult.success(result);
    }

    @Override
    public RestResult<Boolean> checkValidateCode(String token, String validateCode) {
        boolean flag = verificationUtil.checkValidateCode(token, validateCode);
        return RestResult.success(flag);
    }

}
