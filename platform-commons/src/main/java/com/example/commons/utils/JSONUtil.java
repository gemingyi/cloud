package com.example.commons.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Json工具类
 * @Date 2019/8/22 11:39
 */
public class JSONUtil {

    private static Gson gson = new Gson();

    /**
     * json字符串转对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        T obj = null;
        if (StringUtils.isEmpty(text)) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        obj = gson.fromJson(text, clazz);
        return obj;
    }

    /**
     * 对象转json串
     */
    public String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * json 转 集合
     */
    public static <T> List<T> parseList(String text) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<T>();
        }
        Type listType = new TypeToken<List<T>>() {}.getType();
        return gson.fromJson(text, listType);
    }


}
