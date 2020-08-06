package com.example.commons.enums;

/**
 * 请求来源枚举
 */
public enum CallSourceEnum {
    WEB,
    PC,
    WECHAT,
    IOS,
    ANDROID;

    public static boolean isValid(String name) {
        for (CallSourceEnum callSource : CallSourceEnum.values()) {
            if (callSource.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
