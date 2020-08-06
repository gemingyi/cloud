package com.example.commons.result;

import com.example.commons.enums.BusinessExceptionEnum;
import com.example.commons.enums.ResultCode;
import com.example.commons.exceptions.BusinessException;
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


    public static <T> SysErrorResult<T> failure(ResultCode resultCode, Throwable e, Object errors) {
        SysErrorResult<T> result = new SysErrorResult<>();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message() != null ? resultCode.message() : e.getMessage());
        result.setData(e.getClass().getName() + ": " + e.getMessage());
        return result;
    }

    public static <T> SysErrorResult<T> failure(BusinessException e) {
        BusinessExceptionEnum ee = BusinessExceptionEnum.getByEClass(e.getClass());
        if (ee != null) {
            return SysErrorResult.failure(ee.getResultCode(), e, e.getData());
        }

        SysErrorResult<T> defaultErrorResult = SysErrorResult.failure(e.getResultCode() == null ? ResultCode.SUCCESS : e.getResultCode(), e, e.getData());
        if (!StringUtils.isEmpty(e.getMessage())) {
            defaultErrorResult.setMessage(e.getMessage());
        }
        return defaultErrorResult;
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

    public void setData(Object data) {
        this.data = (T) data;
    }
}
