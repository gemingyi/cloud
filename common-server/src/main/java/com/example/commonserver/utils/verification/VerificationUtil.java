package com.example.commonserver.utils.verification;

import com.example.pluginredis.util.RedisKeyBuildUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VerificationUtil {

    private static final String COMMON_MODULE = "common";

    @Autowired
    private DefaultKaptcha kaptcha;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public String generateValidateCode(String token) throws Exception {
        // 生成验证码code
        String text = kaptcha.createText();
        String key = RedisKeyBuildUtil.keyBuilder(COMMON_MODULE, "generateValidateCode", token);
        redisTemplate.opsForValue().set(key, text, 3, TimeUnit.MINUTES);
        // 生成图片验证码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = kaptcha.createImage(text);
        ImageIO.write(image, "jpg", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public boolean checkValidateCode(String token, String validateCode) {
        String key = RedisKeyBuildUtil.keyBuilder(COMMON_MODULE, "generateValidateCode", token);
        Boolean exist = redisTemplate.hasKey(key);
        if (exist == null || !exist) {
            return false;
        }
        String code = (String) redisTemplate.opsForValue().get(key);
        return validateCode.equalsIgnoreCase(code);
    }

}
