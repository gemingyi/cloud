package com.example.commons.result;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: API 统一返回状态码
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 9:49$
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(10000, "请求成功"),

    /* 系统错误 */
    SYSTEM_INNER_ERROR(11001, "系统内部错误"),


    /* 参数错误 */
    PARAM_IS_INVALID(12001, "参数非法"),
    /* 数据错误：*/
    RESULT_DATA_NONE(12002, "数据未找到"),
    DATA_IS_WRONG(12003, "数据有误"),
    DATA_ALREADY_EXISTED(12004, "数据已存在"),


    /* 接口错误：*/
    INTERFACE_EXCEED_LOAD(13001, "接口负载过高"),
    INTERFACE_REPEAT_COMMIT(13002, "接口重复请求"),
    INTERFACE_NOT_FOUND(13003, "找不到接口"),

    /* 权限错误：*/
    USER_NOT_LOGGED_IN(14001, "用户未登录"),
    PERMISSION_NO_ACCESS(14003, "无访问权限"),

    /* 微服务 */
    SERVICE_NO_PERMISSION(21001, "服务未授权"),
    SERVICE_DEMOTION(21002, "服务超时"),
    SERVICE_DOWNGRADE(21003, "服务暂时不可用"),
    SERVICE_CALL_EXCEPTION(21004, "服务调用异常");

    /* 用户错误：*/


    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.name();
    }


    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    /***
     * 校验重复的code值
     */
    static void main(String[] args) {
        ResultCode[] apiResultCodes = ResultCode.values();
        List<Integer> codeList = new ArrayList<>();
        for (ResultCode apiResultCode : apiResultCodes) {
            if (codeList.contains(apiResultCode.code)) {
                System.out.println(apiResultCode.code);
            } else {
                codeList.add(apiResultCode.code());
            }
            System.out.println(apiResultCode.code() + " " + apiResultCode.message());
        }
    }
}