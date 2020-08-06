package com.example.plugines.commons.annotation;


import com.example.plugines.commons.Enum.FieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字段注解
 * Created by gmy on 2018/7/5.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Field {

    FieldType type() default FieldType.Keyword;

    boolean store() default false;

    String searchAnalyzer() default "";

    String analyzer() default "";

    boolean filedData() default false;

    String fields() default "";
}
