package com.example.plugines.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 索引名称，索引类型注解
 * Created by gmy on 2018/7/5.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Document {
    String indexName();
    String type() default "";
}
