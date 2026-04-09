package com.example.platformboot.replay;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.utils.encryption.HmacSha256Utils;
import org.apache.commons.lang3.StringUtils;
import java.util.TreeMap;

public class ReplayValidator {

    public static final long TIMESTAMP_EXPIRE = 60L;
    // nonce 过期时间
    public static final long NONCE_EXPIRE = 70L;

    public static final String REDIS_KEY_PREFIX = "replay:nonce:";
    

//    private final StringRedisTemplate redisTemplate;
//
//    public ReplayValidator(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    /**
     * 统一校验：时间戳 + 防重 + 验签
     */
    public <T> void validate(ApiRequest<T> request, String secret) {
        checkTimestamp(request.getTimestamp());
//        checkNonce(request.getNonce());
        checkSign(request, secret);
    }

    /**
     * 时间戳校验
     */
    private void checkTimestamp(Long timestamp) {
        if (timestamp == null) {
            throw new RuntimeException("timestamp不能为空");
        }
        long nowSec = System.currentTimeMillis() / 1000;
        if (Math.abs(nowSec - timestamp) > TIMESTAMP_EXPIRE) {
            throw new RuntimeException("请求已过期");
        }
    }

    /**
     * 防重放 nonce
     */
//    private void checkNonce(String nonce) {
//        if (StringUtils.isBlank(nonce)) {
//            throw new RuntimeException("nonce不能为空");
//        }
//        String key = REDIS_KEY_PREFIX + nonce;
//        Boolean ok = redisTemplate.opsForValue()
//                .setIfAbsent(key, "1", NONCE_EXPIRE, TimeUnit.SECONDS);
//        if (Boolean.FALSE.equals(ok)) {
//            throw new RuntimeException("请求重复");
//        }
//    }

    /**
     * 验签（核心）
     * 签名规则：
     * 1. 把 timestamp、nonce、bizContent 放入 TreeMap 字典排序
     * 2. 拼接 k=v&k=v
     * 3. bizContent 使用 JSON 字符串
     * 4. HmacSHA256 签名
     */
    private <T> void checkSign(ApiRequest<T> request, String secret) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("timestamp", request.getTimestamp());
        map.put("nonce", request.getNonce());
        map.put("bizContent", JSONObject.toJSONString(request.getBizContent()));

        // 拼接
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (v != null) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        if (StringUtils.isNotEmpty(sb)) {
            sb.setLength(sb.length() - 1);
        }

        // 签名
        String serverSign = HmacSha256Utils.hmacSha256(sb.toString(), secret);

        if (!serverSign.equalsIgnoreCase(request.getSign())) {
            throw new RuntimeException("验签失败");
        }
    }

}