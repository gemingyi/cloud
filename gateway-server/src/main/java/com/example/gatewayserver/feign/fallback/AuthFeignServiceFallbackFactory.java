package com.example.gatewayserver.feign.fallback;

import com.example.commons.enums.ResultCode;
import com.example.commons.result.RestResult;
import com.example.gatewayserver.feign.AuthFeign;
import feign.hystrix.FallbackFactory;

/**
 * 鉴权远程调用降级
 */
public class AuthFeignServiceFallbackFactory implements FallbackFactory<AuthFeign> {

    @Override
    public AuthFeign create(Throwable throwable) {
        return new AuthFeign() {

            @Override
            public RestResult<Object> check(String token) {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

            @Override
            public RestResult<Object> info(String token) {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

        };
    }
    
    private void test(int i, int q) {

    }

}
