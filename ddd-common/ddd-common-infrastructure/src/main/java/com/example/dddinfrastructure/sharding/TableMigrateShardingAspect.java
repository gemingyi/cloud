//package com.example.dddinfrastructure.sharding;
//
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.aop.support.AopUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.function.Function;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Aspect
//@Component
//public class TableMigrateShardingAspect {
//
//    private static final Pattern WRITE_METHOD_PATTERN = Pattern.compile("^(insert|delete|update).*$");
//
//    private static Map<String, TableRule> rules = new HashMap<>();
//
//    @Autowired
//    private TableRuleProperties tableRuleProperties;
//
//
//    @PostConstruct
//    public void init() {
//        List<TableRule> tableRuleList = tableRuleProperties.getTableRuleList();
//        rules = tableRuleList.stream().collect(Collectors.toMap(TableRule::getTableName, Function.identity()));
//    }
//
//
//    @Pointcut("execution(* com.example.dddinfrastructure.mapper.*Mapper.*(..))")
////    @Pointcut("@target(com.example.dddinfrastructure.sharding.Invoke) && execution(* com.example.dddinfrastructure.mapper.*Mapper.*(..))")
//    public void sharding() {
//    }
//
//    @Around("sharding()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Annotation annotation = ((Class) AopUtils
//                .getTargetClass(joinPoint.getTarget()).getGenericInterfaces()[0]).getAnnotation(Invoke.class);
//        Invoke newTableDef = (Invoke) annotation;
//
//        if (newTableDef == null) {
//            return joinPoint.proceed();
//        }
//
//        //表名
//        String tableName = newTableDef.tableName();
//
//        // 读取ThreadLocal中的路由(在迁移任务等特定场景下才会设置 ThreadLocal,无关路由配置)
//        String threadLocalRoute = ShardingThreadLocal.getValue();
//        if (StringUtils.isNotBlank(threadLocalRoute)) {
//            if (Objects.equals(threadLocalRoute, TableRule.NEW)) {
//                return executeWithRoute(joinPoint, TableRule.NEW);
//            } else {
//                return executeWithRoute(joinPoint, TableRule.OLD);
//            }
//        }
//
//        TableRule tableConfig = rules.get(tableName);
//        if (tableConfig == null) {
//            // 找不到表配置，直接执行
//            return joinPoint.proceed();
//        }
//
//        //读取配置文件的路由
//        Matcher matcher = WRITE_METHOD_PATTERN.matcher(method.getName());
//        if (matcher.find()) {
//            //写语句
//            return executeWriteOperation(joinPoint, tableConfig, tableName);
//        } else {
//            //读语句
//            return executeReadOperation(joinPoint, tableConfig);
//
//        }
//    }
//
//    /**
//     * 执行读操作：按路由规则处理 OLD/NEW
//     */
//    private Object executeReadOperation(ProceedingJoinPoint joinPoint, TableRule tableConfig) throws Throwable {
//        return Objects.equals(tableConfig.getReadRule(), TableRule.NEW) ?
//                executeWithRoute(joinPoint, TableRule.NEW) :
//                executeWithRoute(joinPoint, TableRule.OLD);
//    }
//
//    /**
//     * 执行写操作：按路由规则处理 OLD/NEW/BOTH
//     */
//    private Object executeWriteOperation(ProceedingJoinPoint joinPoint, TableRule tableConfig, String tableName) throws Throwable {
//        String writeSwitch = tableConfig.getWriteRule();
//        Object result;
//        if (Objects.equals(writeSwitch, TableRule.NEW)) {
//            // 只写新表
//            result = executeWithRoute(joinPoint, TableRule.NEW);
//        } else if (Objects.equals(writeSwitch, TableRule.BOTH)) {
//            // 双写：先写旧表，再写新表
//            result = executeWithRoute(joinPoint, TableRule.OLD);
//
//            // 旧表写入成功，写新表
//            try {
//                executeWithRoute(joinPoint, TableRule.NEW);
//            } catch (DuplicateKeyException e) {
//                // 主键冲突
//                log.warn(
//                        "新表写入失败,DuplicateKeyException:{},tableName:{}, methodName:{}", tableName,
//                        joinPoint.getSignature().getName(), e);
//
//            } catch (Exception e) {
//                // 新表写入失败
//                log.warn("新表写入失败,tableName:{},methodName:{}", tableName, joinPoint.getSignature().getName(), e);
//            }
//        } else {
//            // 只写老表
//            result = executeWithRoute(joinPoint, TableRule.OLD);
//        }
//        return result;
//    }
//
//    /**
//     * 通用路由执行方法：统一处理路由设置与恢复
//     *
//     * @param joinPoint   切面连接点
//     * @param targetRoute 目标路由（NEW/OLD）
//     */
//    private Object executeWithRoute(ProceedingJoinPoint joinPoint, String targetRoute) throws Throwable {
//
//        String originalRoute = ShardingThreadLocal.getValue();
//        try {
//            // 仅当原始路由为空时，才设置目标路由（避免覆盖上游已设置的路由）
//            if (StringUtils.isBlank(originalRoute)) {
//                ShardingThreadLocal.setValue(targetRoute);
//            }
//            return joinPoint.proceed();
//        } finally {
//            // 恢复原始路由：上游已设置则还原，本切面设置则清理
//            if (StringUtils.isNotBlank(originalRoute)) {
//                ShardingThreadLocal.setValue(originalRoute);
//            } else {
//                ShardingThreadLocal.clear();
//            }
//        }
//    }
//}