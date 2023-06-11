package com.example.commons.utils.bean;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * bean map互转
 */
public class BeanUtil {

    public static <T> void setCreateValue(T t, String operationId, String operationName, Date date) {
        if (t == null) {
            return;
        }
        final BeanWrapper wrapper = new BeanWrapperImpl(t);

        if (!StringUtils.isEmpty(date)) {
            if (wrapper.isWritableProperty("createTime")) {
                wrapper.setPropertyValue("createTime", date);
            }
            if (wrapper.isWritableProperty("updateTime")) {
                wrapper.setPropertyValue("updateTime", date);
            }
        }

        if (!StringUtils.isEmpty(operationId)) {
            if (wrapper.isWritableProperty("operationId")) {
                wrapper.setPropertyValue("operationId", operationId);
            }
        }
        if (!StringUtils.isEmpty(operationName)) {
            if (wrapper.isWritableProperty("operationName")) {
                wrapper.setPropertyValue("operationName", operationName);
            }
        }

        if (wrapper.isWritableProperty("deleteFlag")) {
            wrapper.setPropertyValue("deleteFlag", 0);
        }
    }

    public static <T> void setModifyValue(T t, String operationId, String operationName, Date date) {
        if (t == null) {
            return;
        }
        final BeanWrapper wrapper = new BeanWrapperImpl(t);

        if (!StringUtils.isEmpty(date)) {
            if (wrapper.isWritableProperty("updateTime")) {
                wrapper.setPropertyValue("updateTime", date);
            }
        }

        if (!StringUtils.isEmpty(operationId)) {
            if (wrapper.isWritableProperty("operationId")) {
                wrapper.setPropertyValue("operationId", operationId);
            }
        }
        if (!StringUtils.isEmpty(operationName)) {
            if (wrapper.isWritableProperty("operationName")) {
                wrapper.setPropertyValue("operationName", operationName);
            }
        }
    }

}
