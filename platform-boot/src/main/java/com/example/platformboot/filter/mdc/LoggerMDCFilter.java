package com.example.platformboot.filter.mdc;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * 接入skywalking可直接打印     https://blog.csdn.net/feiying0canglang/article/details/120147462
 * https://blog.csdn.net/forezp/article/details/100060140
 * https://blog.csdn.net/forezp/article/details/106626080
 */
@Slf4j
//@WebFilter(filterName = "loggerMDCFilter", urlPatterns = "/*", asyncSupported = true)
public class LoggerMDCFilter implements Filter {

    public LoggerMDCFilter() {}

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.beforeFilter(request);

        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException exception) {
            log.error("过滤异常！", exception);
            throw exception;
        } finally {
            this.afterFilter();
        }
    }

    @Override
    public void destroy() {
    }


    //------------------ private ------------------

    private void beforeFilter(ServletRequest servletRequest) throws IOException {
        String traceId = servletRequest.getParameter("req.traceId");

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            MDC.put("req.requestURI", httpServletRequest.getRequestURI());
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            if (requestURL != null) {
                MDC.put("req.requestURL", requestURL.toString());
            }
            String method = httpServletRequest.getMethod();
            MDC.put("req.method", method);

            String contentType = httpServletRequest.getContentType();
            String requestParams;
            if (StringUtils.isNotEmpty(contentType) && contentType.equalsIgnoreCase(HttpMethod.POST.name()) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                HttpServletBodyRequestWrapper requestWrapper = new HttpServletBodyRequestWrapper((HttpServletRequest) servletRequest);
                requestParams = requestWrapper.getBodyString();
            } else if (StringUtils.isNotEmpty(contentType) && contentType.equalsIgnoreCase(HttpMethod.POST.name()) && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                requestParams = JSONObject.toJSONString(httpServletRequest.getParameterMap());
            } else {
                requestParams = JSONObject.toJSONString(httpServletRequest.getQueryString());
            }
            MDC.put("req.requestParams", requestParams);
            if (traceId == null) {
                traceId = httpServletRequest.getHeader("req.traceId");
            }
        }
        if (traceId == null) {
            IdWorker idWorker = new IdWorker(null);
            traceId = String.valueOf(idWorker.nextId());
        }
        MDC.put("req.traceId", traceId);
//        MDC.put("req.userId", "anonymous");
    }

    private void afterFilter() {
        MDC.clear();
//        MDC.remove("req.traceId");
//        MDC.remove("req.requestURI");
//        MDC.remove("req.requestURL");
//        MDC.remove("req.method");
//        MDC.remove("req.requestParams");
////        MDC.remove("req.userId");
    }

}
