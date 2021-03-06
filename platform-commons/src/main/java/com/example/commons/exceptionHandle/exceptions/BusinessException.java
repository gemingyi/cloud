package com.example.commons.exceptionHandle.exceptions;


import com.example.commons.exceptionHandle.BusinessExceptionEnum;
import com.example.commons.result.ResultCode;

/**
 * @Description: 业务异常类统一父类
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 9:45
 */
public class BusinessException extends RuntimeException {

    protected String code;

    protected String message;

    protected ResultCode resultCode;

    protected Object data;

    public BusinessException() {
        BusinessExceptionEnum exceptionEnum = BusinessExceptionEnum .getByEClass(this.getClass());
        if (exceptionEnum != null) {
            resultCode = exceptionEnum.getResultCode();
            code = exceptionEnum.getResultCode().code().toString();
            message = exceptionEnum.getResultCode().message();
        }

    }

    public BusinessException(String message) {
        this();
        this.message = message;
    }

    public BusinessException(String format, Object... objects) {
        this();
//        format = StringUtils.replace(format, "{}", "%s");
        format = "";
        this.message = String.format(format, objects);
    }

    public BusinessException(String msg, Throwable cause, Object... objects) {
        this();
//        String format = StringUtils.replace(msg, "{}", "%s");
        String format = "";
        this.message= String.format(format, objects);
    }

    public BusinessException(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.code = resultCode.code().toString();
        this.message = resultCode.message();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
