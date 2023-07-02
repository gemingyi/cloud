package com.example.platformboot.handler;

import com.example.commons.exceptionHandle.exceptions.InternalServerException;
import com.example.commons.result.SysErrorResult;
import com.example.commons.utils.json.FastJsonUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @Description feign 处理 被调用端抛出的异常(熔断器关闭时)
 * @Author mingyi ge
 * @Date 2020/9/25 17:29
 */
@Slf4j
//@Configuration
//@ConditionalOnProperty(name = "feign.hystrix.enabled", havingValue = "false")
public class GlobalFeignExceptionHandler implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = null;
        InternalServerException internalServerException = new InternalServerException();
        try {
            // 获取Service中抛出异常提示信息
            message = Util.toString(response.body().asReader());
        } catch (Exception e) {
            log.error("decode error", e);
        }
        if (message == null) {
            return internalServerException;
        }
        SysErrorResult sysErrorResult = FastJsonUtil.jsonStrToBean(message, SysErrorResult.class);
        internalServerException.setCode(String.valueOf(sysErrorResult.getCode()));
        internalServerException.setMessage(sysErrorResult.getMessage());
        return internalServerException;
    }

}
