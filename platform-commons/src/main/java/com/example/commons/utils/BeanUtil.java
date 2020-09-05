package com.example.commons.utils;

import org.json.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * bean map互转
 */
public class BeanUtil {

    public static Map<String, Object> Obj2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(obj) != null)
                map.put(field.getName(), String.valueOf(field.get(obj)));
        }
        return map;
    }

    public static Object map2Obj(Map<String, Object> map, Class<?> clz) throws Exception {
        Object obj = clz.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    public static <T> void setInitValue(T t, String userName, Date date) {
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
            if (wrapper.isWritableProperty("createDate")) {
                wrapper.setPropertyValue("createDate", date);
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
            if (wrapper.isWritableProperty("modifyDate")) {
                wrapper.setPropertyValue("modifyDate", date);
            }
        }
    }

}
