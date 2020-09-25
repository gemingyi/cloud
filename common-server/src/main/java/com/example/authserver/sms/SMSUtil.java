package com.example.authserver.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
@Slf4j
public class SMSUtil {

    @Autowired
    private SMSProperties smsProperties;

    private IAcsClient client;
    private Random random = new Random();


    @PostConstruct
    public void initIAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile("default", smsProperties.getAccessKeyId(), smsProperties.getSecret());
        client = new DefaultAcsClient(profile);
    }


    /**
     * @Description 发送短信
     * @Author mingyi ge
     * @Date 2019/8/21 18:24
     */
    public String send(String phoneNumbers, String code, String codeStr) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getDomain());
        request.setVersion(smsProperties.getVersion());
        request.setAction(smsProperties.getAction());
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", code);
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("TemplateParam", "{'code':'" + codeStr + "'}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("SmsUtil INFO : 发送短信至 {} - {} - {}", phoneNumbers, code, response.getData());
            return response.getData();
        } catch (ServerException e) {
            log.error("SmsUtil ServerException : {}", e);
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("SmsUtil ClientException : {}", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 生成随机数
     * @Author mingyi ge
     * @Date 2019/8/21 18:32
     */
    public String generateRandomNumber(int n) {
        String rn = "";
        for (int i = 0; i < n; i++) {
            rn += random.nextInt(10);
        }
        return rn;
    }
}
