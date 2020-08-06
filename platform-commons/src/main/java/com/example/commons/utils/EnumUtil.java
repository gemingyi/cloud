package com.example.commons.utils;

import com.example.commons.model.EnumsData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 枚举工具类
 */
public class EnumUtil {

    /**
     * @Description 获取枚举数据列表
     * @Author dawn
     * @Date 2019/8/12 16:24
     */
    public static List<EnumsData> getEnumDataList(Class<?> enumClass) throws Exception {
        List<EnumsData> list = new ArrayList<>();
        Method k = enumClass.getMethod("name");
        Method v = enumClass.getMethod("getValue");
        for (Object o : enumClass.getEnumConstants()){
            list.add(new EnumsData(k.invoke(o).toString(), v.invoke(o).toString()));
        }
        return list;
    }

    /**
     * @Description 获取枚举数据 K-V
     * @Author dawn
     * @Date 2019/8/12 16:24
     */
    public static HashMap<String, String> getEnumMaps(Class<?> enumClass) throws Exception {
        HashMap<String, String> map = new HashMap<>(4);
        Method k = enumClass.getMethod("name");
        Method v = enumClass.getMethod("getValue");
        for (Object o : enumClass.getEnumConstants()){
            map.put(k.invoke(o).toString(), v.invoke(o).toString());
        }
        return map;
    }

    /**
     * @Description 获取枚举数据 K-Type
     * @Author dawn
     * @Date 2019/8/12 16:24
     */
    public static HashMap<String, Object> getEnumTypeMaps(Class<?> enumClass) throws Exception {
        HashMap<String, Object> map = new HashMap<>(4);
        Method k = enumClass.getMethod("name");
        Method valueOf = enumClass.getMethod("valueOf", String.class);
        for (Object o : enumClass.getEnumConstants()){
            map.put(k.invoke(o).toString(), valueOf.invoke(o, k.invoke(o).toString()));
        }
        return map;
    }

}
