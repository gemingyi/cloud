package com.example.commons.constans;

/**
* @Description 亲求头常量
* @Author mingyi ge
*/
public interface HeaderConstants {

    /**
     * 用户的登录token
     */
    public static final String X_TOKEN = "X-Token";

    /**
     * api的版本号
     */
    public static final String API_VERSION = "Api-Version";

    /**
     * app版本号
     */
    public static final String APP_VERSION = "App-Version";

    /**
     * 调用来源
     */
    public static final String CALL_SOURCE = "Call-Source";


    public static String getXToken() {
        return X_TOKEN;
    }

    public static String getApiVersion() {
        return API_VERSION;
    }

    public static String getAppVersion() {
        return APP_VERSION;
    }

    public static String getCallSource() {
        return CALL_SOURCE;
    }

}
