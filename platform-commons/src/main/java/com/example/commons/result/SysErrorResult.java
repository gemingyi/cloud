package com.example.commons.result;

import com.example.commons.exceptionHandle.BusinessExceptionEnum;
import com.example.commons.exceptionHandle.exceptions.BusinessException;
import org.springframework.util.StringUtils;


/**
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 15:57$
 */
public class SysErrorResult<T> implements Result {

    /**
     * 系统内部自定义的返回值编码
     */
    private Integer code;

    /**
     * 异常堆栈的精简信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;


    public static <T> SysErrorResult<T> failure(BusinessException e) {
//        BusinessExceptionEnum ee = BusinessExceptionEnum.getByEClass(e.getClass());
        BusinessExceptionEnum ee = BusinessExceptionEnum.getByCode(e);
        if (ee != null) {
            return SysErrorResult.failure(ee.getResultCode(), e);
        }

        SysErrorResult<T> defaultErrorResult = SysErrorResult.failure(e.getResultCode() == null ? ResultCode.SYSTEM_INNER_ERROR : e.getResultCode(), e);
        if (!StringUtils.isEmpty(e.getMessage())) {
            defaultErrorResult.setMessage(e.getMessage());
        }
        return defaultErrorResult;
    }

    public static <T> SysErrorResult<T> failure(ResultCode resultCode, Throwable e) {
        SysErrorResult<T> result = new SysErrorResult<>();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message() != null ? resultCode.message() : e.getMessage());
        return result;
    }

    public static <T> SysErrorResult<T> failure(ResultCode resultCode, String msg) {
        SysErrorResult<T> result = new SysErrorResult<>();
        result.setCode(resultCode.code());
        result.setMessage(msg);
        return result;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
