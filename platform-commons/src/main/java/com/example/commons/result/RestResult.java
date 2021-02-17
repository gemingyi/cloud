package com.example.commons.result;


/**
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 17:21$
 */
public class RestResult<T> implements Result {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示
     */
    private String message;

    /**
     * 数据
     */
    private T data;


    public static <T> RestResult<T> success() {
        RestResult<T> result = new RestResult<>();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(null);
        return result;
    }

    public static <T> RestResult<T> success(T data) {
        RestResult<T> result = new RestResult<>();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> RestResult<T> failure(ResultCode resultCode) {
        RestResult<T> result = new RestResult<>();
        result.setResultCode(resultCode);
        result.setData(null);
        return result;
    }

    public static <T> RestResult<T> failure(Integer code, String message) {
        RestResult<T> result = new RestResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> RestResult<T> failure(String message) {
        RestResult<T> result = new RestResult<>();
        result.setCode(ResultCode.SYSTEM_INNER_ERROR.code());
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public RestResult<T> code(Integer code) {
        this.code =code;
        return this;
    }

    public RestResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public RestResult<T> data(T data) {
        this.data = data;
        return this;
    }

    private void setResultCode(ResultCode code) {
        this.code = code.code();
        this.message = code.message();
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
