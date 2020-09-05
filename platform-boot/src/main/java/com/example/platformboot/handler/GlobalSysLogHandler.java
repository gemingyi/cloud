//package com.example.platformboot.handler;
//
//import com.alibaba.nacos.client.utils.IPUtil;
//import com.example.commons.annotations.SysLog;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
//public class GlobalSysLogHandler {
//
//    @Pointcut("@annotation(com.example.commons.annotations.SysLog)")
//    public void logPointCut() {
//
//    }
//
//    @Around("logPointCut()")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String requestUrl = request.getRequestURL().toString();
//        long beginTime = System.currentTimeMillis();
//        // 执行方法
//        Object result = point.proceed();
//        long executeTime = System.currentTimeMillis() - beginTime;
//        // 记录访问日志
//        saveSysLog(point, requestUrl, executeTime);
//        return result;
//    }
//
//
//    private void saveSysLog(ProceedingJoinPoint joinPoint, String requestUrl, long time) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        //注解上的描述
//        SysLog syslog = method.getAnnotation(SysLog.class);
//
//        //请求的方法名
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = signature.getName();
//        //请求的参数
//        Object[] args = joinPoint.getArgs();
//        //请求IP
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
////        String requestIpAddr = IPUtil.getIpAddr(request);
//
//        System.out.println(String.format("方法%s.%s,执行了%s毫秒", className, methodName, time));
//    }
//
//}
