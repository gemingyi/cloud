package com.example.platformboot.handler;

import com.alibaba.fastjson.JSON;
import com.example.commons.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class GlobalRequestLogHandler {

    /**
     * 定义日志切点
     */
    @Pointcut("execution(public * com.*.*.controller..*.*(..))")
    public void logPointcut() {
    }


    //@Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = IPUtil.getRealIp(request);
        long thread = Thread.currentThread().getId();
        StringBuffer msg = new StringBuffer(String.format("[%s] ##请求开始##", thread));
        msg.append(String.format(" URL=[%s]", uri));
        msg.append(String.format(" 请求方式=[%s]", method));
        msg.append(String.format(" 请求IP=[%s]", ip));
//        msg.append(String.format(" 请求头=[%s]", HttpUtils.getHeaders(request)));
        Object[] args = joinPoint.getArgs();
        if (null != args && args.length > 0) {
            msg.append(" 参数=").append(JSON.toJSONString(args));
        } else {
            Map<String, String[]> params = request.getParameterMap();
            if (!CollectionUtils.isEmpty(params)) {
                msg.append(" 参数=").append(JSON.toJSONString(params));
            }
        }
        log.info(msg.toString());
    }


    //@AfterReturning(value = "logPointcut()", returning = "object")
    public void logAfterReturning(Object object, long start) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String arg = null != object ? JSON.toJSONString(object) : "";
        long took = System.currentTimeMillis() - start;
        long thread = Thread.currentThread().getId();
        log.info(String.format("[%s] ^^请求结束^^ 耗时=[%sms] URL=[%s] 请求方式=[%s] 响应参数=%s", thread, took, uri, method, arg));
    }


    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void logAfterThrowing(Throwable e) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        long thread = Thread.currentThread().getId();
        log.error(String.format("[%s] **请求异常** URL=[%s] 请求方式=[%s] 请求头信息=%s 异常详情=%s", thread, uri, method, IPUtil.getHeaders(request), ExceptionUtils.getStackTrace(e)));
    }


    @Around("logPointcut()")
    public Object logAopAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        logBefore(joinPoint);
        Object object = joinPoint.proceed(joinPoint.getArgs());
        logAfterReturning(object, start);
        return object;
    }
}
