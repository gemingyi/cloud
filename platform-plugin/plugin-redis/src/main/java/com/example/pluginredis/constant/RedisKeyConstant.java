package com.example.pluginredis.constant;

public class RedisKeyConstant {

    /**
     * 默认项目前缀
     */
    public static final String KEY_PREFIX = "default";

    /**
     * 分割字符，默认[:]
     */
    public static final String KEY_SPLIT_CHAR = ":";

    /**
     * 连接字符，默认[_]
     */
    public static final String KEY_CONNECT_CHAR = "_";

    /**
     * 分布式锁，前缀
     */
    public static final String LOCK_NAME_PREFIX = "lock" + KEY_SPLIT_CHAR;

    /**
     * 公共
     */
    public static final String COMMON_MODULE = "common";

    /**
     * 用户模块
     */
    public static final String USER_MODULE = "user";


}
