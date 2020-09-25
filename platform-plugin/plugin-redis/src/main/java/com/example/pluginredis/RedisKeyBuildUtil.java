package com.example.pluginredis;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.pluginredis.RedisKeyConstants.KEY_PREFIX;
import static com.example.pluginredis.RedisKeyConstants.KEY_SPLIT_CHAR;

public class RedisKeyBuildUtil {


    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String module, String func, Object... args) {
        return keyBuilder(null, module, func, args);
    }

    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param obj    对象
     * @return key
     */
    public static String keyBuilder(String module, String func, Object obj) {
        String[] fields = ObjToStrArray(obj);
        return keyBuilder(null, module, func, fields);
    }

    public static String keyBuilder(String prefix, String module, String func, Object obj) {
        String[] fields = ObjToStrArray(obj);
        return keyBuilder(prefix, module, func, fields);
    }

    /**
     * redis的key键规则定义
     *
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数...
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, Object... args) {
        // 项目前缀
        if (prefix == null) {
            prefix = KEY_PREFIX;
        }
        StringBuilder key = new StringBuilder(prefix);
        // KEY_SPLIT_CHAR 为分割字符
        key.append(KEY_SPLIT_CHAR).append(module).append(KEY_SPLIT_CHAR).append(func);
        if (args != null) {
            for (Object arg : args) {
                if (!StringUtils.isEmpty(arg)) {
                    key.append(KEY_SPLIT_CHAR).append(arg);
                }
            }
        }
        return key.toString();
    }

    /**
     * @Description
     * @Author mingyi ge
     */
    private static String[] ObjToStrArray(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> list = new ArrayList<>(fields.length);
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(obj);
                if (value != null) {
                    list.add(f.getName() + "_" + value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new String[fields.length]);
    }

}

