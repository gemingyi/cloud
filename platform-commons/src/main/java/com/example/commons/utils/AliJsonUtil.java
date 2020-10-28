package com.example.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;


public class AliJsonUtil {

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };


    /**
     * json转bean
     */
    public static <T> T jsonStrToBean(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * json转数组
     */
    public static <T> Object[] jsonStrToArray(String jsonStr) {
        return jsonStrToArray(jsonStr, null);
    }

    /**
     * json转对象数组
     */
    public static <T> Object[] jsonStrToArray(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr, clazz).toArray();
    }

    /**
     * json转对象List
     */
    public static <T> List<T> jsonStrToList(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr, clazz);
    }

    /**
     * json转对象List<Map>
     */
    public static <T> List<Map<String, T>> jsonStrToList(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<List<Map<String, T>>>() {
        });
    }

    /**
     * json转对象
     */
    public static Object jsonStrToObject(String jsonStr) {
        return JSON.parse(jsonStr);
    }

    /**
     * json转化为map
     */
    public static <K, V> Map<K, V> jsonStrToMap(String jsonStr) {
        return (Map<K, V>) JSONObject.parseObject(jsonStr);
    }

    /**
     * 将map转化为string
     */
    public static <K, V> String mapToJsonStr(Map<K, V> m) {
        return JSONObject.toJSONString(m);
    }

    /**
     * 对象转jsonStr
     */
    public static String objectToJsonStr(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * 对象转jsonStr
     */
    public static String featureObjectToJsonStr(Object object) {
        return JSON.toJSONString(object, config, features);
    }

}
