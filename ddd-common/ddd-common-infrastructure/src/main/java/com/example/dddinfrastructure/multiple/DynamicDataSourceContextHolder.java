package com.example.dddinfrastructure.multiple;

public class DynamicDataSourceContextHolder{

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();


    public static void setDataSourceType(String dsType) {
        CONTEXT_HOLDER.set(dsType);
    }


    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get() == null ? DataSourceType.MASTER.name() : CONTEXT_HOLDER.get();
    }


    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}