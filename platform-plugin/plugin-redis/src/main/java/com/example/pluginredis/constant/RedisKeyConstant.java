package com.example.pluginredis.constant;

public interface RedisKeyConstant {

    /**
     * 默认项目前缀
     */
    String KEY_PREFIX = "default";

    /**
     * 分割字符，默认[:]
     */
    String KEY_SPLIT_CHAR = ":";

    /**
     * 连接字符，默认[_]
     */
    public static final String KEY_CONNECT_CHAR = "_";

    /**
     * 分布式锁，前缀
     */
    String LOCK_NAME_PREFIX = "lock" + KEY_SPLIT_CHAR;


}
