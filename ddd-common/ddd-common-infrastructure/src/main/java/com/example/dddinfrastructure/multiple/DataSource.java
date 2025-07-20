package com.example.dddinfrastructure.multiple;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    DataSourceType value() default DataSourceType.MASTER;

}
