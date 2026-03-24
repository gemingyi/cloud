package com.example.commons.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Date;
import java.util.UUID;

public class OrderNoUtils {

    private static final int USER_ID_LENGTH = 10;

    @Autowired
    private Environment environment;


    /**
     * prefix(0-3) + env(4) + dateFormat(5-18) + userIdSuffix(19-28) + random(29-30) + sequence(31)
     *
     * @param businessPrefix 业务前缀
     * @param userId   用户ID
     * @return ID
     */
    public static String generateId(String businessPrefix, String userId, Environment environment) {
        // 环境 N:正式 G:灰度 P:预发布 T:测试
//        String env = KVStore.getProperty("common.properties").getProperty("env.tag", "T");
        String env = String.join(", ", environment.getActiveProfiles());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String dateFormat = DateFormatUtils.format(new Date(), "yyMMddHHmmss");
        String subUserId = getUserIdPart(userId);
        String timePrefix = uuid.substring(0, 2);
        String random = uuid.substring(uuid.length() - 3);
        return businessPrefix + env + timePrefix + dateFormat + subUserId + random;
    }

    private static String getUserIdPart(String userId) {
        StringBuilder userPart = new StringBuilder();
        char[] chars = userId.toCharArray();
        if (userId.length() > USER_ID_LENGTH) {
            for (int i = 0; i < USER_ID_LENGTH; i++) {
                userPart.append(chars[i]);
            }
            return userPart.toString();
        }
        // 不足指定长度 补齐
        for (int i = 0; i < chars.length; i++) {
            userPart.append(chars[i]);
        }
        int supplement = USER_ID_LENGTH - chars.length;
        for (int i = 0; i < supplement; i++) {
            userPart.append("0");
        }
        return userPart.toString();
    }

}
