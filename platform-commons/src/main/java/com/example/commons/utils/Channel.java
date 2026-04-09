package com.example.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.utils.encryption.AESUtils;
import com.example.commons.utils.encryption.RSAUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Channel {

    private static final String BASE_STRING = "0123456789";

    private static String MY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzUPRAwsTrZ3Il5gfdXjN3hqivNCWb5wL+E+lL" +
            "fx+t2mdP43SAV0qlUqff9R+gCn2HrzmOUHL5w616MrWH6h0RNcg1G0QpleEPpCe2do591b0kzIwG" +
            "Syc8yOJwN6ninSEIOdJ3DCGyZrMi+IUEOKv6CRNVNOzc4XA9rnCGcPwSAwIDAQAB";
    private static String MY_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALNQ9EDCxOtnciXmB91eM3eGqK80" +
            "JZvnAv4T6Ut/H63aZ0/jdIBXSqVSp9/1H6AKfYevOY5QcvnDrXoytYfqHRE1yDUbRCmV4Q+kJ7Z2" +
            "jn3VvSTMjAZLJzzI4nA3qeKdIQg50ncMIbJmsyL4hQQ4q/oJE1U07NzhcD2ucIZw/BIDAgMBAAEC" +
            "gYEApkzx+g2oa1mo95T6vkbbHIL6dmK6bng0wkbdGnNvgeEH54NfTJeoVuLtZjsDdX3FJc1QZ7gc" +
            "qZ7ciHP2/3IUOZAzrsIWcsBo3rt9vBNKUgJxq4AitazH/QGgoutfttrzTQ8SZRrg7Ir0jXZbXdI3" +
            "j4YPJN7ECODecoC5Afu9LOECQQDdYL/nYA/aY/cfRUgYFWVri7SYhVLz883lsL1ayydcbGRFX1Zy" +
            "LBzMTrdFEunlbAfgDA2kQxAjtv5sjeybVHQPAkEAz1ww7bcqEAPQLHug7KyGmF+16UOL76X0t+fW" +
            "F1ZOgb7Mrrg4A6ck6OF+8hCfIOc4CZipk5/pZSiLmPkDhEi+zQJAav3pga/BubD6rJhVYZ5lp/ab" +
            "T+OP1euNJWen/IuW+GpCg+yx4PhE6tFYbf/YH1N48uWgajUnRbuZd2mYYvIMLQJBAKGXtomJs73H" +
            "OvNvZHkL+dfEvn6JpQOc0JiAktac65Ewn1GJPh2ao6jYEZWSXm/FwGW6Emx4semjLyaG57au+gEC" +
            "QQDRWICV3Zz9ELPlDhHAVWK61ZXQNWeCDKGRaCs2trY9unogCEgwsIzIabH+5f2vkiuWQ6BCSl9F" +
            "nNN6GLvU1yPV";

    private static String CHANNEL_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGSMUcajwbGBcGNCTlHjQTofX6K0k8rGEPRuRv" +
            "OY/wKYeIndK307Z2dxTy91IbJtIib7rX2Lz9DjytvpxTZNyu5fmNYMiSQZbZkK5Vgr4bvz//b9mL" +
            "qUC9a48ssb3nkd/cDUc9W8VC2KchDA6H0SUJc9MswBZsdLil/sGmaND2UQIDAQAB";
    private static String CHANNEL_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIZIxRxqPBsYFwY0JOUeNBOh9for" +
            "STysYQ9G5G85j/Aph4id0rfTtnZ3FPL3Uhsm0iJvutfYvP0OPK2+nFNk3K7l+Y1gyJJBltmQrlWC" +
            "vhu/P/9v2YupQL1rjyyxveeR39wNRz1bxULYpyEMDofRJQlz0yzAFmx0uKX+waZo0PZRAgMBAAEC" +
            "gYAZzg4i4b9fLYfvJ0IXzXAQfcU5J+xQ/odAWk03moYwjqp+9GUz8jlUFpot/p0GnrUu7O2o4Pug" +
            "XYYHlWORGwbVRgUs9h8qupBGEhnGFRxr29VJmYFHM6a1sINQSIPng2ymdlmZ5iRjCLXcXU0/qYP7" +
            "76C+lRLwd3eMaaJ1n2ez4QJBAMGA2UynnkbvklsNt8pGFLkJvnSbGKz1KPR+OO9moLA+d51vbr9b" +
            "2XwgKAioqHFEjyICoTMFMwuWB8YofO2Ur10CQQCxp5k4bi3Lg8YqSbvzCptoPysss82/ZZyxbOVe" +
            "+mGa2RdLnmXuckATNZoFcEKtaWmeWlF1s2dNFV4BAZPLipeFAkEAuFXmq/+B6FGROux2iVr5osyF" +
            "gs+9Uga6XS9hTa6WpU7c5D2jYug85uI7QQRqcny9Xcjjh5cMaZ0jCsLHQAzEgQJABVUBbUcpSrVu" +
            "yjAiWv4TggJ7WkEfSMJU/KJHFB2xtkMTxuo/JvP45lqyf71J0wiZ6e3OChlTXWXpx1/zvqUvPQJB" +
            "AKR51nKzpccRiRfJ07nGuKlNmUaXT8188gwJjK8cqyjdz9WypIbMQv2HaqP4KKpif1DfWEIAodzj" +
            "JSfnEw2dtME=";

//    private static String MY_PUBLIC_KEY = null;
//    private static String MY_PRIVATE_KEY = null;
//    private static String CHANNEL_PUBLIC_KEY = null;
//    private static String CHANNEL_PRIVATE_KEY = null;
//    static {
//        Map<String, Object> keyMap = EncryptionUtil.RSA.initKey();
//        MY_PRIVATE_KEY = EncryptionUtil.RSA.getPrivateKeyStr(keyMap);
//        MY_PUBLIC_KEY = EncryptionUtil.RSA.getPublicKeyStr(keyMap);
//
//        CHANNEL_PRIVATE_KEY = EncryptionUtil.RSA.getPrivateKeyStr(keyMap);
//        CHANNEL_PUBLIC_KEY = EncryptionUtil.RSA.getPublicKeyStr(keyMap);
//    }



    private static final String CHANNEL_CODE = "testChannel";
    
    @Getter
    @Setter
    static class UserAccess {
        private String name;
        private String idNo;
        private String mobileNo;
        private String accessType;
    }

    @Getter
    @Setter
    static class ChannelStandardReqDto {
        private String appId;
        private String flowNo;
        private String method;
        private String version;
        private String params;
        private String key;
        private String timestamp;
    }

    @Getter
    @Setter
    static class StandardRes {
        private boolean success;
        private Integer code;
        private String  desc;
        private String data;
        private String key;
        private String timestamp;
        private String sign;
    }

    public static void main(String[] args) {
        //RsaKeyGenerate.main(args); //RSA 秘钥生成util

        UserAccess req = new UserAccess();
        //姓名 身份证号码  手机号 可配置MD5，如是MD5则默认大写，身份证最后 X 则是大写
        req.setName("name");
        req.setIdNo("123");
        req.setMobileNo("456");
        req.setAccessType(null);


        //用户准入
        String method = "user.access";

        //域名由资金方提供
        String url = "";
        try {

            String params = encryptParams(JSON.toJSONString(req), CHANNEL_CODE, method, "1.0.0", "", MY_PRIVATE_KEY, CHANNEL_PUBLIC_KEY);

            System.out.println(params);

            //下面post方法是模拟渠道方返回
            String response = post(params, method, url);

            StandardRes standardRes = JSONObject.parseObject(response, StandardRes.class);
            //验签or解密
            handleChannelResp(response, standardRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void handleChannelResp(String response, StandardRes standardRes) {
        String decryptData = "";
//        boolean signFlag = EncryptionUtil.RSA.publicVerify((response, EncryptionUtil.RSA.getPrivateKey(CHANNEL_PUBLIC_KEY));
//        boolean signFlag = ChannelSignUtil.verifySign(response, CHANNEL_PUBLIC_KEY);
        boolean signFlag = channelVerifySign(response, CHANNEL_PUBLIC_KEY);
        if (signFlag) {
            // 签名验证通过,开始进行解密
            try {
                // 判断数据是否需要解密
                if (StringUtils.isNotEmpty(standardRes.getData())) {
                    String aseKey = RSAUtils.decrypt(standardRes.getKey(), RSAUtils.getPrivateKey(MY_PRIVATE_KEY));
                    log.debug("response ase key:{}", aseKey);
                    decryptData = AESUtils.decrypt(standardRes.getData(), aseKey);
                }
                standardRes.setData(decryptData);
                log.info("verifyDecryption isSuccess responseData:{}", JSONObject.toJSONString(standardRes));

            } catch (Exception e) {
                // 解密失败
                log.error("verifyDecryption isError responseData:{}", JSONObject.toJSONString(standardRes));
            }
        }
    }


    public static String post(String param, String method, String url) throws Exception {
        //以下为渠道方收到oppo相关请求后操作

        String postMethod = url + "?method=" + method;
        System.out.println(postMethod);
        System.out.println(param);

        //================模拟渠道解密参数================
        // 1. 验签
        if (channelVerifySign(param, MY_PUBLIC_KEY)) {
            // 2. 参数解密  解密业务参数
            ChannelStandardReqDto standardReqDto = JSONObject.parseObject(param, ChannelStandardReqDto.class);
            log.info("before decry param:{}", standardReqDto.getParams());
            String channelParam = channelDecryptParam(standardReqDto.getParams(), standardReqDto.getKey(), CHANNEL_PRIVATE_KEY);
            log.info("after decry param:{}", channelParam);
        }
        //================END================



        //================模拟渠道方返回OPPO================
        Map<String, Object> channelResp = mockChannelResp();
        //================END================

        //渠道方业务数据返回
        return JSON.toJSONString(channelResp);
    }

    private static Map<String, Object> mockChannelResp() throws Exception {
        //这里是渠道方业务数据
        Map<String,Object > dataMap = new HashMap<>();
        dataMap.put("access", true);
        //准入失败原因  ||  成功无需返回
        dataMap.put("failReason", null);
        //按需返回以需求为准，OLD：老用户， NEW_FLOW: 新流程用户, NEW_FLOW2: 新流程2， NOTSPENT:授信未支用，NOLOAN：已支用未在贷
        dataMap.put("identity", "");
        //如果有，尽量返回
        dataMap.put("openId", "abc123456");

        //随机生成aes秘钥
//        String aesKey = randomString(BASE_STRING, 16);
        String aesKey = AESUtils.generateKey();
        // AES加密Params
        String encryptParams = AESUtils.encrypt(JSON.toJSONString(dataMap), aesKey);
        // 生成的RSA公钥加密的密文
        String rsaKey = RSAUtils.encrypt(aesKey, RSAUtils.getPublicKey(MY_PUBLIC_KEY));
        //组装resp
        Map<String, Object> channelResp = new TreeMap<>();
        channelResp.put("success", Boolean.TRUE);
        channelResp.put("code", 0);
        channelResp.put("desc", "成功");
        channelResp.put("data", encryptParams);
        channelResp.put("key", rsaKey);
        channelResp.put("timestamp", System.currentTimeMillis());
        StringBuilder beforeSign = new StringBuilder();
        for (Map.Entry<String, Object> string : channelResp.entrySet()) {
            beforeSign.append(string).append("&");
        }
        String beforeSign2 = beforeSign.substring(0, beforeSign.length() - 1);
        log.debug("beforeSign={}", beforeSign2);
        // 私钥加签
        String sign = RSAUtils.privateSinge(beforeSign2, RSAUtils.getPrivateKey(CHANNEL_PRIVATE_KEY));
        channelResp.put("sign", sign);
        return channelResp;
    }

    public static String encryptParams(String param, String appId, String method, String version, String agreeId, String oppoPrivateKey, String channelPublicKey) throws Exception {

//        String aesKey = randomString(BASE_STRING, 16);
        String aesKey = AESUtils.generateKey();
        // AES加密Params
        String encryptParams = AESUtils.encrypt(param, aesKey);
        // 生成的RSA公钥加密的密文
        String rsaKey = RSAUtils.encrypt(aesKey, RSAUtils.getPublicKey(channelPublicKey));
        String flowNo = appId + randomString(BASE_STRING, 29);
        // treeMap保证字段按照key排序
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appId", appId);
        treeMap.put("flowNo", flowNo);
        treeMap.put("method", method);
        treeMap.put("version", version);

        treeMap.put("params", encryptParams);
        treeMap.put("key", rsaKey);
        treeMap.put("timestamp", System.currentTimeMillis());

        StringBuilder beforeSign = new StringBuilder();
        for (Map.Entry<String, Object> string : treeMap.entrySet()) {
            beforeSign.append(string).append("&");
        }
        String beforeSign2 = beforeSign.substring(0, beforeSign.length() - 1);
        log.debug("beforeSign={}", beforeSign2);
        // 私钥加签
        String sign = RSAUtils.privateSinge(beforeSign2, RSAUtils.getPrivateKey(oppoPrivateKey));
        treeMap.put("sign", sign);
        if (StringUtils.isNotEmpty(agreeId)) {
            treeMap.put("agreeId", agreeId);
        }
        log.info("channel encryptParams:{}", JSONObject.toJSONString(treeMap));
        return JSONObject.toJSONString(treeMap);

    }


    private static String randomString(String baseString, int length) {
        if (StringUtils.isEmpty(baseString)) {
            return StringUtils.EMPTY;
        }
        final StringBuilder sb = new StringBuilder(length);

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    private static boolean channelVerifySign(String response, String publicKey) {
        try {
            // 解析JSON字符串
            JSONObject jsonObject = JSONObject.parseObject(response);

            // 移除sign字段
            String sign = jsonObject.getString("sign");
            jsonObject.remove("sign");

            // 按参数字典顺序排序参数
            TreeMap<String, Object> sortedMap = new TreeMap<>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            // 构建签名前的字符串
            StringBuilder beforeSign = new StringBuilder();
            for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
                beforeSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            // 移除最后一个&
            if (beforeSign.length() > 0) {
                beforeSign.setLength(beforeSign.length() - 1);
            }

            log.debug("verify sign string: {}", beforeSign.toString());

            // 使用公钥方公钥验签
            return RSAUtils.publicVerify(beforeSign.toString(), sign, RSAUtils.getPublicKey(publicKey));
        } catch (Exception e) {
            log.error("channel verify sign error", e);
            return false;
        }
    }

    private static String channelDecryptParam(String params, String rsaKey, String channelPrivateKey) throws Exception {
        try {
            // 使用渠道私钥解密RSA密钥
            String aesKey = RSAUtils.decrypt(rsaKey, RSAUtils.getPrivateKey(channelPrivateKey));
            log.debug("decrypt aes key: {}", aesKey);

            // 使用AES密钥解密业务参数
            return AESUtils.decrypt(params, aesKey);
        } catch (Exception e) {
            log.error("channel decrypt param error", e);
            throw e;
        }
    }
}


