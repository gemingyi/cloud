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

/**
 * @Description 全局controller日志打印
 * @Author mingyi ge
 * @Date 2020/9/25 17:29
 */
@Aspect
@Component
public class GlobalControllerLogHandler {

    private Logger log = LoggerFactory.getLogger(GlobalControllerLogHandler.class);

    /**
     * 定义日志切点   控制层接口日志
     */
    @Pointcut("execution(public * com.example.*.controller..*(..))")
    public void logPointcut() {
    }


    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uri = request.getRequestURI();
        String tid = request.getHeader("tid");
        String method = request.getMethod();
//        Map<String, Object> headers = IPUtil.getHeaders(request);
        String arg = Arrays.toString(joinPoint.getArgs());
        String stackTrace = ExceptionUtils.getStackTrace(e);
        log.error("[tid={}] ***请求异常*** URL=[{}],请求方式=[{}],请求参数=[{}],异常详情=[{}]", tid, uri, method, arg, stackTrace);
    }


    @Around("logPointcut()")
    public Object logAopAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Stopwatch start = Stopwatch.createStarted();
        logBeforeReturning(joinPoint);
        Object object = joinPoint.proceed(joinPoint.getArgs());
        logAfterReturning(object, start);
        return object;
    }

    //    @Before("logPointcut()")
    public void logBeforeReturning(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String tid = request.getHeader("tid");
        String ip = IPUtil.getRealIp(request);
        String arg = Arrays.toString(joinPoint.getArgs());
        log.info("[tid={}] ###请求开始### 请求方式=[{}],URL=[{}],请求IP=[{}],请求参数=[{}]", tid, method, uri, ip, arg);
    }


    //    @AfterReturning(value = "logPointcut()", returning = "object")
    public void logAfterReturning(Object object, Stopwatch start) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String tid = request.getHeader("tid");
        String arg = null != object ? JSON.toJSONString(object) : "";
        long millis = start.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("[tid={}] ^^^请求结束^^^ 耗时=[{}],请求方式=[{}],URL=[{}],响应参数=[{}]", tid, millis, method, uri, arg);
    }

}
