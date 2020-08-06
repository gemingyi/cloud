//package com.example.userserver.shiro;
//
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.subject.SubjectContext;
//import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
//
///**
// * @Description:    无状态（使用token关闭session）
// * @Author: mingyi ge
// * @CreateDate: 2019/9/3 9:37
// */
//public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
//
//    @Override
//    public Subject createSubject(SubjectContext context) {
//        //不创建session
//        context.setSessionCreationEnabled(false);
//        return super.createSubject(context);
//    }
//}