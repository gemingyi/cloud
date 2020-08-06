package com.example.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


/**
 * bean map互转
 */
public class BeanUtil {

    public static Map<String,Object> Obj2Map(Object obj) throws Exception{
        Map<String,Object> map=new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            if(field.get(obj)!=null)
                map.put(field.getName(), String.valueOf(field.get(obj)));
        }
        return map;
    }

    public static Object map2Obj(Map<String,Object> map,Class<?> clz) throws Exception{
        Object obj = clz.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for(Field field:declaredFields){
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }


}
