package com.example.platformboot;

import com.example.commons.utils.SpringContextProvider;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.*;

public class QueryCallable implements Callable<Object> {

    private Class<?> springBeanClazz;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] parameterTypes;

    public QueryCallable(Class<?> springBeanClazz, String methodName,
                         Object[] parameters, Class<?>[] parameterTypes) {
        super();
        this.springBeanClazz = springBeanClazz;
        this.methodName = methodName;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Object call() throws Exception {
        Class<?> springBean = SpringBeanHelper.getBean(springBeanClazz).getClass();
        Method method = ReflectionUtils.findMethod(springBean, methodName, parameterTypes);
        return method.invoke(SpringBeanHelper.getBean(springBeanClazz), parameters);
//        return ReflectionUtils.invokeMethod(method, parameters);

//        Class<?> springBean = SpringBeanHelper.getBean("testServiceImpl").getClass();
//        Method method = springBean.getMethod(methodName, parameterTypes);
//        return method.invoke(SpringBeanHelper.getBean("testServiceImpl"), parameters);

//        Class<?> springBean = SpringBeanHelper.getBean("testServiceImpl").getClass();
//        Method method = springBean.getMethod(methodName, parameterTypes);
//        return method.invoke(springBean.newInstance(), parameters);
    }

}
