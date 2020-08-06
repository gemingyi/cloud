package com.example.commons.annotations;

import java.lang.annotation.*;

/**
 * 访问日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
