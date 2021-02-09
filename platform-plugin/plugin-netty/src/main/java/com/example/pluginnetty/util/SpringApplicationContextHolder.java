package com.example.pluginnetty.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description spring context 提供者
 * @Author dawn
 * @Date 2019/8/29 11:11
 */
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的setApplicationContext, 存储spring上下文对象为静态变量.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取静态变量ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("application is null");
        }
        return applicationContext;
    }

    /**
     * 获取bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        verifyApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        verifyApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    /**
     * 验证ApplicationContext是否为null
     */
    private static void verifyApplicationContext(){
        if (applicationContext == null) {
            throw new IllegalStateException("application is null");
        }
    }

}