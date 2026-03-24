package com.example.dddinfrastructure.multiple;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Objects;

//@Aspect
//@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.example.dddinfrastructure.multiple.DataSource)")
    public void dsPointCut() {
    }

//    @Pointcut("execution(* com.example.dddinfrastructure.mapper.*Mapper.*(..))")
    @Pointcut("execution(* com.example.dddinfrastructure.mapper.*Mapper.*(..))")
    public void dsPointCut2(){}

    @Around("dsPointCut2()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        DataSource dataSource = getDataSource(joinPoint);
        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }
        try {
            return joinPoint.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
//        DataSource dataSourceA = AnnotationUtils.findAnnotation(signature.getClass(), DataSource.class);
        //获取类上的注解
        Annotation annotation = ((Class) AopUtils
                .getTargetClass(point.getTarget()).getGenericInterfaces()[0]).getAnnotation(DataSource.class);
        DataSource dataSource = (DataSource) annotation;
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
