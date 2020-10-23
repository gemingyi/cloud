package com.example.platformboot.config;

import com.example.commons.exceptionHandle.exceptions.InternalServerException;
import com.example.commons.utils.JSONUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

/**
* @Description feign 处理 被调用端抛出的异常
* @Author mingyi ge
* @Date 2020/9/25 17:29
*/
@Slf4j
@Configuration
public class FeignExceptionDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        InternalServerException internalServerException = new InternalServerException();
        try {
            // 获取Service中抛出异常提示信息
            String message = Util.toString(response.body().asReader());
            internalServerException = JSONUtil.JsonToBean(message, InternalServerException.class);
            if (StringUtils.isBlank(internalServerException.getMessage()) && internalServerException.getMessage().length() > 50){
                internalServerException.setMessage("服务被调用方异常");
            }
            return internalServerException;
        } catch (Exception e) {
            log.error("decode error",e);
        }
        return internalServerException;
    }
}
