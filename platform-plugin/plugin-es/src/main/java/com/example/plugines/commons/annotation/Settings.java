package com.example.plugines.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2018/10/22.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Settings {
    String settingJsonKey() default "";
}
