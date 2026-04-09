package com.example.commonserver.utils.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;

/**
 * https://blog.csdn.net/qq_43647359/article/details/104638599
 */
@Component
public class EmailSenderUtil {

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    /**
     * 普通邮件
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(emailProperties.getUserName());
            messageHelper.setSubject(subject);

            messageHelper.setTo(to);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带附件
     */
    public void sendMail(String toUser, String subject, String message, InputStream inputStream, String ioType) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.setFrom(emailProperties.getUserName());
            helper.setTo(toUser);
            helper.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            //内容
            BodyPart html = new MimeBodyPart();
            html.setContent(message, "text/html;charset=utf-8");
            multipart.addBodyPart(html);

            //附件IO流
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setFileName("");
            DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(inputStream, ioType));
            mimeBodyPart.setDataHandler(dataHandler);
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 带附件
     */
    public void sendMail(String toUser, String subject, String message, String filePath) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.setFrom(emailProperties.getUserName());
            helper.setTo(toUser);
            helper.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            //内容
            BodyPart html = new MimeBodyPart();
            html.setContent(message, "text/html;charset=utf-8");
            multipart.addBodyPart(html);
            mimeMessage.setContent(multipart);

            //附件
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 Thymeleaf 模板邮件
     */
    public void sendHtmlEmail(String to, String subject,
                              String htmlTemplate, Context context) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 发件人、收件人、标题
        helper.setFrom(emailProperties.getUserName());
        helper.setTo(to);
        helper.setSubject(subject);

        // 封装模板数据
        String html = templateEngine.process(htmlTemplate, context);
        helper.setText(html, true);

        // 发送
        javaMailSender.send(message);
    }

}
