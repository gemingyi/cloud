package com.example.userserver.feign.fallback;

import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;
import com.example.userserver.feign.TestFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TestFeignFallbackFactory implements FallbackFactory<TestFeign> {

    @Override
    public TestFeign create(Throwable throwable) {
        return new TestFeign() {

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
