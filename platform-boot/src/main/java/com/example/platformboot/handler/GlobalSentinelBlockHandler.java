package com.example.platformboot.handler;

import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;

/**
 * sentinel 通用 限流降级
 */
public class GlobalSentinelBlockHandler {

    /**
     * 全局 限流降级 兜底方法
     */
    public static RestResult<?> blockHandler(Throwable throwable) {
        return RestResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
    }

}
