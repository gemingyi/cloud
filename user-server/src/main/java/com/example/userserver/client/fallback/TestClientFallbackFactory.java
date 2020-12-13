package com.example.userserver.client.fallback;

import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;
import com.example.userserver.client.TestClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TestClientFallbackFactory implements FallbackFactory<TestClient> {

    @Override
    public TestClient create(Throwable throwable) {
        return new TestClient() {

            @Override
            public RestResult<Object> test1() {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

            @Override
            public RestResult<Object> test2() {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

            @Override
            public RestResult<Object> seataTest() {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

            @Override
            public RestResult<Object> testImport(MultipartFile file) {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }

            @Override
            public RestResult<Object> checkValidateCode(String token, String validateCode) {
                return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
            }
        };
    }
}
