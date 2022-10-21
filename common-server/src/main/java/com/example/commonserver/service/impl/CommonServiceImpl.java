package com.example.commonserver.service.impl;

import com.example.commonserver.service.ICommonService;
import com.example.pluginredis.util.RedisKeyBuildUtil;
import com.example.pluginredis.constant.RedisKeyConstant;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private DefaultKaptcha kaptcha;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public String generateValidateCode(String token) throws Exception {
        // 生成验证码code
        String text = kaptcha.createText();
        String key = RedisKeyBuildUtil.keyBuilder(RedisKeyConstant.COMMON_MODULE, "generateValidateCode", token);
        redisTemplate.opsForValue().set(key, text, 3, TimeUnit.MINUTES);
        // 生成图片验证码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = kaptcha.createImage(text);
        ImageIO.write(image, "jpg", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    @Override
    public boolean checkValidateCode(String token, String validateCode) {
        String key = RedisKeyBuildUtil.keyBuilder(RedisKeyConstant.COMMON_MODULE, "generateValidateCode", token);
        Boolean exist = redisTemplate.hasKey(key);
        if (exist == null || !exist) {
            return false;
        }
        String code = (String) redisTemplate.opsForValue().get(key);
        return validateCode.equalsIgnoreCase(code);
    }

}
