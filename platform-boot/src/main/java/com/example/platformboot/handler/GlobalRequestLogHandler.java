package com.example.platformboot.handler;

import com.alibaba.fastjson.JSON;
import com.example.commons.utils.IPUtil;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class GlobalRequestLogHandler {

    private Logger log = LoggerFactory.getLogger(GlobalRequestLogHandler.class);

    /**
     * 定义日志切点
     */
    @Pointcut("execution(public * com.example.*.controller..*(..))")
    public void logPointcut() {
    }


//    @Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = IPUtil.getRealIp(request);
        long thread = Thread.currentThread().getId();
        String arg = Arrays.toString(joinPoint.getArgs());
        log.info(String.format("[%s] ###请求开始### 请求方式=[%s] URL=[%s] 请求IP=[%s] 请求参数=[%s]", thread, method, uri, ip, arg));
    }


//    @AfterReturning(value = "logPointcut()", returning = "object")
    public void logAfterReturning(Object object, Stopwatch start) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String arg = null != object ? JSON.toJSONString(object) : "";
        long millis = start.stop().elapsed(TimeUnit.MILLISECONDS);
        long thread = Thread.currentThread().getId();
        log.info(String.format("[%s] ^^^请求结束^^^ 耗时=[%sms] 请求方式=[%s] URL=[%s]  响应参数=%s", thread, millis, method, uri, arg));
    }


    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
//        Map<String, Object> headers = IPUtil.getHeaders(request);
        String arg = Arrays.toString(joinPoint.getArgs());
        String stackTrace = ExceptionUtils.getStackTrace(e);
        long thread = Thread.currentThread().getId();
        log.error(String.format("[%s] ***请求异常*** URL=[%s] 请求方式=[%s] 请求参数=%s 异常详情=%s", thread, uri, method, arg, stackTrace));
    }


    @Around("logPointcut()")
    public Object logAopAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Stopwatch start = Stopwatch.createStarted();
        logBefore(joinPoint);
        Object object = joinPoint.proceed(joinPoint.getArgs());
        logAfterReturning(object, start);
        return object;
    }
}
