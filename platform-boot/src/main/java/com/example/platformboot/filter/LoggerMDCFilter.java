package com.example.platformboot.filter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.example.commons.utils.IPUtil;
import com.example.commons.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

//@Slf4j
//@WebFilter(filterName = "loggerMDCFilter", urlPatterns = "/*", asyncSupported = true)
public class LoggerMDCFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggerMDCFilter.class);

    public LoggerMDCFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.beforeFilter(request);

        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException var8) {
            logger.error("过滤异常！", var8);
            throw var8;
        } finally {
            this.afterFilter();
        }

    }

    void beforeFilter(ServletRequest request) {
        MDC.put("req.remoteHost", request.getRemoteHost());
        MDC.put("req.remoteAddr", request.getRemoteAddr());
        MDC.put("req.remotePort", String.valueOf(request.getRemotePort()));
        MDC.put("req.localAddr", request.getLocalAddr());
        MDC.put("req.localPort", String.valueOf(request.getLocalPort()));
        String requestId = request.getParameter("req.requestId");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            MDC.put("req.requestURI", httpServletRequest.getRequestURI());
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            if (requestURL != null) {
                MDC.put("req.requestURL", requestURL.toString());
            }

//            MDC.put("req.method", httpServletRequest.getMethod());
//            MDC.put("req.queryString", httpServletRequest.getQueryString());
//            MDC.put("req.userAgent", httpServletRequest.getHeader("User-Agent"));
//            MDC.put("req.xForwardedFor", httpServletRequest.getHeader("X-Forwarded-For"));
            if (requestId == null) {
                requestId = httpServletRequest.getHeader("req.requestId");
            }
        }

        if (requestId == null) {
            IdWorker idWorker = new IdWorker(null);
            requestId = String.valueOf(idWorker.nextId());
        }

        MDC.put("req.requestId", requestId);
        MDC.put("req.userId", "anonymous");

    }

    void afterFilter() {
        MDC.remove("req.remoteHost");
        MDC.remove("req.remoteAddr");
        MDC.remove("req.remotePort");
        MDC.remove("req.localAddr");
        MDC.remove("req.localPort");
        MDC.remove("req.requestURI");
        MDC.remove("req.requestURL");
//        MDC.remove("req.method");
//        MDC.remove("req.queryString");
//        MDC.remove("req.userAgent");
//        MDC.remove("req.xForwardedFor");
        MDC.remove("req.requestId");
        MDC.remove("req.userId");
    }

    public void destroy() {
    }

}


class RequestId {
    private static final long SYSTEM_CURRENT_MILLIS = System.currentTimeMillis();
    private static final String IP = IPUtil.getServiceIp();
    private static final AtomicLong lastId = new AtomicLong();

    public RequestId() {
    }

    public static String createReqId() {
        return hexIp(IP) + "-" + Long.toString(SYSTEM_CURRENT_MILLIS, 36).toUpperCase() + "-" + lastId.incrementAndGet();
    }

    private static String hexIp(String ip) {
        StringBuilder sb = new StringBuilder();
        String[] ipArray = ip.split("\\.");
        int length = ipArray.length;

        for(int i = 0; i < length; ++i) {
            String seg = ipArray[i];
            String h = Integer.toHexString(Integer.parseInt(seg));
            if (h.length() == 1) {
                sb.append("0");
            }

            sb.append(h);
        }

        return sb.toString();
    }
}
