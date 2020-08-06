//package com.example.commons.utils.email;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
///**
// * 配置模板
// */
//@Configuration
//public class JavaMailSenderConfig {
//
//    @Autowired
//    private EmailProperties emailProperties;
//
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
//
//        javaMail.setHost(emailProperties.getHost());
//        javaMail.setUsername(emailProperties.getUserName());
//        javaMail.setPassword(emailProperties.getPassword());
//
//        Properties properties = new Properties();
//        properties.setProperty("mail.smtp.ssl.enable", "true");
//        properties.setProperty("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.port", Integer.toString(port));//设置端口
//        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(port));//设置ssl端口
//        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
//        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.smtp.starttls.enable", "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.smtp.starttls.required", "javax.net.ssl.SSLSocketFactory");
//        javaMail.setJavaMailProperties(properties);
//        return javaMail;
//    }
//
//}
