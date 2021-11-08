package com.example.commons.utils.bean;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/10/13 15:00
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}