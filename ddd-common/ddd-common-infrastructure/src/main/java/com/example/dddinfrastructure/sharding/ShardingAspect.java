package com.example.dddinfrastructure.sharding;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
//@Aspect
//@Component
public class ShardingAspect {

    private static Map<String, TableRule> rules = new HashMap<>();

    // 表读写规则
//    static {
//        TableRule rule = new TableRule();
//        rule.setReadRule("old");
//        rule.setWriteRule("all");
//        rules.put("test_db", rule);
//    }

    @Autowired
    private TableRuleProperties tableRuleProperties;

    @PostConstruct
    public void init() {
        List<TableRule> tableRuleList = tableRuleProperties.getTableRuleList();
        rules = tableRuleList.stream().collect(Collectors.toMap(TableRule::getTableName, Function.identity()));
    }

    @Pointcut("execution(* com.example.dddinfrastructure.mapper.*Mapper.*(..))")
    public void sharding() {
    }

    @Around("sharding()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = ((Class) AopUtils.getTargetClass(joinPoint.getTarget()).getGenericInterfaces()[0]).getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取类上的注解
        Annotation annotation = ((Class) AopUtils
                .getTargetClass(joinPoint.getTarget()).getGenericInterfaces()[0]).getAnnotation(Invoke.class);
        Invoke invokeAnnotation = (Invoke) annotation;
        String tableName = invokeAnnotation.tableName();
        String[] writeMethod = invokeAnnotation.writeMethod();
        List<String> writeMethodList = Arrays.asList(writeMethod);
        String methodName = method.getName();
        TableRule rule = rules.get(tableName);
        if (rule == null) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        // 读方法
        if (!writeMethodList.contains(methodName)) {
            if ("old".equals(rule.getReadRule())) {
                return handlerOld(joinPoint);
            } else if("new".equals(rule.getReadRule())) {
                return handlerNew(joinPoint);
            }
        } else {
            if ("old".equals(rule.getWriteRule())) {
                return handlerOld(joinPoint);
            } else if("new".equals(rule.getWriteRule())) {
                return handlerNew(joinPoint);
            }

            Object result = handlerOld(joinPoint);
            handlerNew(joinPoint);
            return result;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private Object handlerOld(ProceedingJoinPoint joinPoint) throws Throwable {
        ShardingThreadLocal.setValue("old");
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            ShardingThreadLocal.clear();
        }
    }

    private Object handlerNew(ProceedingJoinPoint joinPoint) throws Throwable {
        ShardingThreadLocal.setValue("new");
        try {
           return joinPoint.proceed(joinPoint.getArgs());
        }  finally {
            ShardingThreadLocal.clear();
        }
    }

}
