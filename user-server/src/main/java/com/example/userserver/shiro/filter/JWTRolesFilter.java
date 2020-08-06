//package com.example.userserver.shiro.filter;
//
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.util.CollectionUtils;
//import org.apache.shiro.web.filter.authz.AuthorizationFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Set;
//
///**
// * @Description: 角色过滤器（拦截指定请求）
// * 实现isAccessAllowed()方法， 重写onAccessDenied()方法
// * 先进入isAccessAllowed() 返回true直接放行，否则进入onAccessDenied()。因为isAccessAllowed()已经判断过权限了，所以onAccessDenied()直接返回403
// * @Author: mingyi ge
// * @CreateDate: 2019/9/19 10:22
// */
//public class JWTRolesFilter extends AuthorizationFilter {
//
//    private Logger logger = LoggerFactory.getLogger(JWTRolesFilter.class);
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
//        Subject subject = this.getSubject(servletRequest, servletResponse);
//        String[] rolesArray = (String[]) ((String[]) o);
//        if (rolesArray != null && rolesArray.length != 0) {
//            Set<String> roles = CollectionUtils.asSet(rolesArray);
//            return subject.hasAllRoles(roles);
//        } else {
//            return true;
//        }
//    }
//
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
//        handler403(response, ErrorCodeUtil.CODE_NO_PERMISSION);
//        return false;
//    }
//
//    /**
//     * 返回403
//     *
//     * @author: mingyi ge
//     * @date: 2019/9/19 10:53
//     */
//    private void handler403(ServletResponse response, String code) {
//        try {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(HttpStatus.OK.value());
//            httpResponse.setContentType("application/json;charset=utf-8");
//            httpResponse.getWriter().write("{\"status\": false, \"data\": " + null + ", \"msg\": \"" + code + "\"}");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//}
//
