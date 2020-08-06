//package com.example.userserver.shiro.filter;
//
//import org.apache.shiro.authc.*;
//import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
//import org.apache.shiro.web.util.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @Description: JWT认证过滤器拦截全部请求
// * @Author: mingyi ge
// * @CreateDate: 2019/9/3 9:44
// */
//public class JWTAuthFilter extends BasicHttpAuthenticationFilter {
//
//    private Logger logger = LoggerFactory.getLogger(JWTAuthFilter.class);
//
//
//    /**
//     * 返回false阻止后面的filter和servlet的执行
//     *
//     * @author: mingyi ge
//     * @date: 2019/9/3 16:16
//     */
//    @Override
//    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) {
//        //开发环境
//        if (jwtProperties.isDev()) {
//            return true;
//        }
//        String contextPath = WebUtils.getPathWithinApplication(WebUtils.toHttp(servletRequest));
//        if (!StringUtils.isEmpty(jwtProperties.getAnonymousPath())) {
//            //匿名可访问的url
//            String[] anonUrls = jwtProperties.getAnonymousPath().split(",");
//            for (int i = 0; i < anonUrls.length; i++) {
//                if (contextPath.contains(anonUrls[i])) {
//                    return true;
//                }
//            }
//        }
//        //获取请求头token
//        AuthenticationToken token = this.createToken(servletRequest, servletResponse);
//        if (token == null) {
//            //获取不到token（未登录）
//            handler401(servletResponse, ErrorCodeUtil.CODE_NO_LOGIN);
//            return false;
//        } else {
//            try {
//                this.getSubject(servletRequest, servletResponse).login(token);
//                return true;
//            } catch (Exception e) {
//                //账户不存在
//                if(e instanceof UnknownAccountException) {
//                    handler401(servletResponse, ErrorCodeUtil.CODE_NO_LOGIN);
//                    return false;
//                }
//                //账户被禁用
//                if(e instanceof DisabledAccountException) {
//                    handler401(servletResponse, ErrorCodeUtil.CODE_ACCOUNT_ISLOCK);
//                    return false;
//                }
//                //token错误
//                if(e instanceof IncorrectCredentialsException) {
//                    handler401(servletResponse, ErrorCodeUtil.CODE_TOKEN_ERROR);
//                    return false;
//                }
//                //token过期
//                if(e instanceof ExpiredCredentialsException) {
//                    // TODO: 2019/9/4 这里要做刷新Token
//                    handler401(servletResponse, ErrorCodeUtil.CODE_TOKEN_OVERDUE);
//                    return false;
//                }
//                //未知
//                handler401(servletResponse, ErrorCodeUtil.CODE_NO_LOGIN);
//                return false;
//            }
//        }
//    }
//
//    /**
//     * 请求头中获取token
//     *
//     * @author: mingyi ge
//     * @date: 2019/9/3 13:38
//     */
//    @Override
//    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String token = request.getHeader("Authorization");
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        }
//        //获取登录方式
//        String loginType = JWTUtil.getClaim(token, "loginType");
//        return new JWTToken(token, loginType);
//    }
//
//
//    /**
//     * token有问题处理
//     * @author: mingyi ge
//     * @date: 2019/9/3 13:39
//     */
//    private void handler401(ServletResponse response, String code) {
//        try {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(HttpStatus.OK.value());
//            httpResponse.setContentType("application/json;charset=utf-8");
//            httpResponse.getWriter().write("{\"status\": false, \"data\": " + null + ", \"msg\": \"" + code + "\"}");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//}
//
