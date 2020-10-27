package com.example.pluginredis;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.example.pluginredis.RedisKeyConstants.KEY_PREFIX;
import static com.example.pluginredis.RedisKeyConstants.KEY_SPLIT_CHAR;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public class RedisKeyBuildUtil {

    private static final String GET = "get";
    private static final String IS = "is";
    private static final String GET_IS = "get|is";
    private static final String GET_CLASS = "getClass";

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
//        Method[] methods = obj.getClass().getMethods();
//        List<String> list = new ArrayList<>(methods.length);
//        try {
//            for (Method m : methods) {
//                if (m.getParameterTypes().length > 0) {
//                    continue;
//                }
//                if (m.getReturnType() == Boolean.class || m.getReturnType() == boolean.class) {
//                    // 如果返回值是 boolean 则兼容 isXxx 的写法
//                    if (m.getName().startsWith(IS)) {
//                        String fieldName = uncapitalize(m.getName().substring(2));
//                        Object value = m.invoke(obj);
//                        if (value != null) {
//                            list.add(fieldName + "_" + value);
//                        }
//                        continue;
//                    }
//                }
//                // 以get开头但排除getClass()方法
//                if (m.getName().startsWith(GET) && !GET_CLASS.equals(m.getName())) {
//                    String fieldName = uncapitalize(m.getName().replaceFirst(GET_IS, ""));
//                    Object value = m.invoke(obj);
//                    if (value != null) {
//                        list.add(fieldName + "_" + value);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list.toArray(new String[methods.length]);

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

