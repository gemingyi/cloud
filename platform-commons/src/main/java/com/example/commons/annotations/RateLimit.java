package com.example.commons.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 获取令牌的等待时间  默认0
     * @return
     */
    int timeOut() default 0;


    /**
     * 超时时间单位（毫秒）
     * @return
     */
    TimeUnit timeOutUnit() default TimeUnit.MILLISECONDS;

}
