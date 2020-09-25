package com.example.commons.label;

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
     */
    public static List<EnumsData> getEnumDataList(Class<?> enumClass) throws Exception {
        List<EnumsData> list = new ArrayList<>();
        Method key = enumClass.getMethod("getKey");
        Method value = enumClass.getMethod("getValue");
        for (Object o : enumClass.getEnumConstants()){
            list.add(new EnumsData(key.invoke(o).toString(), value.invoke(o)));
        }
        return list;
    }

    /**
     * @Description 获取枚举数据 K-V
     */
    public static HashMap<String, Object> getEnumMaps(Class<?> enumClass) throws Exception {
        HashMap<String, Object> map = new HashMap<>(4);
        Method k = enumClass.getMethod("getKey");
        Method v = enumClass.getMethod("getValue");
        for (Object o : enumClass.getEnumConstants()){
            map.put(k.invoke(o).toString(), v.invoke(o));
        }
        return map;
    }

    /**
     * @Description 获取枚举数据 K-Type
     */
    public static HashMap<String, Object> getEnumTypeMaps(Class<?> enumClass) throws Exception {
        HashMap<String, Object> map = new HashMap<>(4);
        Method k = enumClass.getMethod("getKey");
        Method valueOf = enumClass.getMethod("valueOf", String.class);
        for (Object o : enumClass.getEnumConstants()){
            map.put(k.invoke(o).toString(), valueOf.invoke(o, k.invoke(o).toString()));
        }
        return map;
    }

}
