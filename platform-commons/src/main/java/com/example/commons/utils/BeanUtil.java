package com.example.commons.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * bean map互转
 */
public class BeanUtil {

    public static <T> void setCreateValue(T t, String userName, Date date) {
        if (t == null) {
            return;
        }
        final BeanWrapper wrapper = new BeanWrapperImpl(t);

        if (!StringUtils.isEmpty(userName)) {
            if (!StringUtils.isEmpty(userName)) {
                if (wrapper.isWritableProperty("createBy")) {
                    wrapper.setPropertyValue("createBy", userName);
                }
            }
        }

        if (!StringUtils.isEmpty(date)) {
            if (wrapper.isWritableProperty("createTime")) {
                wrapper.setPropertyValue("createTime", date);
            }
        }

        if (wrapper.isWritableProperty("isDelete")) {
            wrapper.setPropertyValue("isDelete", 0);
        }
    }

    public static <T> void setModifyValue(T t, String userName, Date date) {
        if (t == null) {
            return;
        }
        final BeanWrapper wrapper = new BeanWrapperImpl(t);

        if (!StringUtils.isEmpty(userName)) {
            if (!StringUtils.isEmpty(userName)) {
                if (wrapper.isWritableProperty("updateBy")) {
                    wrapper.setPropertyValue("updateBy", userName);
                }
            }
        }

        if (!StringUtils.isEmpty(date)) {
            if (wrapper.isWritableProperty("updateTime")) {
                wrapper.setPropertyValue("updateTime", date);
            }
        }
    }

}
