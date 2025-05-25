package com.example.dddinfrastructure.sharding;

public class ShardingThreadLocal {

    static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "";
        }
    };

    public static void setValue(String value) {
        threadLocal.set(value);
    }

    public static String getValue() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
