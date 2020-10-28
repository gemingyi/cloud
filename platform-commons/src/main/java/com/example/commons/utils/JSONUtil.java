//package com.example.commons.utils;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Description Json工具类
// * @Date 2019/8/22 11:39
// */
//public class JSONUtil {
//
//    private static Gson gson = null;
//    //判断gson对象是否存在了,不存在则创建对象
//    static {
//        if (gson == null) {
//            gson = new Gson();
//            //当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
////            gson= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        }
//    }
//
//    public JSONUtil() {
//    }
//
//
//    /**
//     * 将object对象转成json字符串
//     */
//    public static String JsonToString(Object object) {
//        String gsonString = null;
//        if (gson != null) {
//            gsonString = gson.toJson(object);
//        }
//        return gsonString;
//    }
//
//    /**
//     * 将gsonString转成泛型bean
//     */
//    public static <T> T JsonToBean(String jsonString, Class<T> cls) {
//        T t = null;
//        if (gson != null) {
//            t = gson.fromJson(jsonString, cls);
//        }
//        return t;
//    }
//
//    /**
//     * 转成list
//     * 泛型在编译期类型被擦除导致报错
//     */
//    public static <T> List<T> jsonToList(String jsonString) {
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
//            }.getType());
//        }
//        return list;
//    }
//
//    /**
//     * 转成list
//     * 解决泛型问题
//     */
//    public static <T> List<T> JsonToList(String json, Class<T> cls) {
//        Gson gson = new Gson();
//        List<T> list = new ArrayList<>();
//        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
//        for(final JsonElement elem : array){
//            list.add(gson.fromJson(elem, cls));
//        }
//        return list;
//    }
//
//    /**
//     * 转成list中有map的
//     */
//    public static <T> List<Map<String, T>> jsonToListMaps(String jsonString) {
//        List<Map<String, T>> list = null;
//        if (gson != null) {
//            list = gson.fromJson(jsonString,
//                    new TypeToken<List<Map<String, T>>>() {
//                    }.getType());
//        }
//        return list;
//    }
//
//    /**
//     * 转成map的
//     */
//    public static <T> Map<String, T> jsonToMaps(String jsonString) {
//        Map<String, T> map = null;
//        if (gson != null) {
//            map = gson.fromJson(jsonString, new TypeToken<Map<String, T>>() {
//            }.getType());
//        }
//        return map;
//    }
//
//    /**
//     * 把一个bean（或者其他的字符串什么的）转成json
//     */
//    public static String BeanToJson(Object object){
//        return gson.toJson(object);
//    }
//
//}
