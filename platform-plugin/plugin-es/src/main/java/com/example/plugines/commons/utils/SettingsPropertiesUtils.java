package com.example.plugines.commons.utils;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * Created by Administrator on 2018/10/22.
 */
public class SettingsPropertiesUtils {

    public static final String[] PROPERTIES = new String[] {"settings/elastic_search.properties"};

    private static Properties properties = new Properties();

    static {
        try {
            for (String str : PROPERTIES) {
                properties.load(getResourceAsStream(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SettingsPropertiesUtils() {
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;

        if (classLoader == null) {
            try {
                Method method = Thread.class.getMethod("getContextClassLoader", (Class[]) null);
                try {
                    classLoader = (ClassLoader) method.invoke(Thread.currentThread(), (Class[]) null);
                } catch (IllegalAccessException e) {
                    ; // ignore
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof SecurityException) { }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if (classLoader == null) {
            classLoader = SettingsPropertiesUtils.class.getClassLoader();
        }

        // Return the selected class loader
        return classLoader;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static InputStream getResourceAsStream(final String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            @Override
            public Object run() {
                ClassLoader threadCL = getContextClassLoader();

                if (threadCL != null) {
                    return threadCL.getResourceAsStream(name);
                } else {
                    return ClassLoader.getSystemResourceAsStream(name);
                }
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(SettingsPropertiesUtils.getProperty("testSettings"));
    }

}
