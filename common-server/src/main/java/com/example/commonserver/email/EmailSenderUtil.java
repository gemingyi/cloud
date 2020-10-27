//package com.example.commonserver.email;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import javax.activation.DataHandler;
//import javax.mail.BodyPart;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.util.ByteArrayDataSource;
//import java.io.File;
//import java.io.InputStream;
//
//@Component
//public class EmailSenderUtil {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//
//    /**
//     * 普通邮件
//     */
//    public void sendMail(String to, String subject, String content) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
//            messageHelper.setFrom("");
//            if (subject != null) {
//                messageHelper.setSubject(subject);
//            } else {
//                messageHelper.setSubject("");
//            }
//            messageHelper.setTo(to);
//            messageHelper.setText(content, true);
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 带附件
//     */
//    public void sendMail(String toUser, String subject, String message, InputStream inputStream, String ioType) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setFrom("");
//            helper.setTo(toUser);
//            helper.setSubject(subject);
//
//            Multipart multipart = new MimeMultipart();
//            //内容
//            BodyPart html = new MimeBodyPart();
//            html.setContent(message, "text/html;charset=utf-8");
//            multipart.addBodyPart(html);
//            //附件IO流
//            MimeBodyPart excel = new MimeBodyPart();
//            excel.setFileName("");
//            DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(inputStream, ioType));
//            excel.setDataHandler(dataHandler);
//            multipart.addBodyPart(excel);
//
//            mimeMessage.setContent(multipart);
//            javaMailSender.send(mimeMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendMail(String toUser, String subject, String message, String filePath) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setFrom("");
//            helper.setTo(toUser);
//            helper.setSubject(subject);
//
//            Multipart multipart = new MimeMultipart();
//            //内容
//            BodyPart html = new MimeBodyPart();
//            html.setContent(message, "text/html;charset=utf-8");
//            multipart.addBodyPart(html);
//            mimeMessage.setContent(multipart);
//            //附件
//            FileSystemResource file = new FileSystemResource(new File(filePath));
//            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
//            helper.addAttachment(fileName, file);
//            javaMailSender.send(mimeMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
