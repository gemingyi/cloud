package com.example.platformboot.handler;

import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;

/**
 * sentinel 通用 异常
 */
public class GlobalSentinelFallBackHandler {

    /**
     * 全局 FallBack 兜底方法
     */
    public static RestResult<?> exceptionHandler(Throwable throwable) {
        return RestResult.failure(ResultCode.SERVICE_DOWNGRADE);
    }

}
