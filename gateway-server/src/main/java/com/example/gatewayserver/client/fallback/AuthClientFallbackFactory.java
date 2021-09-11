package com.example.gatewayserver.client.fallback;

import com.example.commons.result.ResultCode;
import com.example.commons.result.RestResult;
import com.example.gatewayserver.client.AuthClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 鉴权远程调用降级
 */
@Component
public class AuthClientFallbackFactory implements FallbackFactory<AuthClient> {

    @Override
    public AuthClient create(Throwable throwable) {
        return new AuthClient() {

            @Override
            public RestResult<Object> check(String token) {
                return RestResult.failure(ResultCode.SERVICE_CALL_EXCEPTION);
            }

            @Override
            public RestResult<Object> info(String token) {
                return RestResult.failure(ResultCode.SERVICE_CALL_EXCEPTION);
            }

        };
    }

}
