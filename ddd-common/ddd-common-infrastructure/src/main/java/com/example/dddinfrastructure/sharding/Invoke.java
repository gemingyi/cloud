package com.example.dddinfrastructure.sharding;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Invoke {

    String tableName();

    String[] writeMethod();

}
