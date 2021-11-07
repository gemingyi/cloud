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
     * 分布式锁，前缀
     */
    String LOCK_NAME_PREFIX = "lock" + KEY_SPLIT_CHAR;


}
