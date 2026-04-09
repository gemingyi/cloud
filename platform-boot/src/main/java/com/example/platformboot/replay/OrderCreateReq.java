package com.example.platformboot.replay;

import com.alibaba.fastjson.JSON;
import com.example.commons.utils.encryption.HmacSha256Utils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.UUID;

@Data
public class OrderCreateReq {

    private String userId;

    private String orderNo;

    private BigDecimal amount;


    public static void main(String[] args) {
        String SECRET = "my-secret-2025";
        ReplayValidator replayValidator = new ReplayValidator();

        OrderCreateReq request = new OrderCreateReq();
        request.setOrderNo("a12345");
        request.setUserId("123");
        request.setAmount(new BigDecimal(100));
        ApiRequest<OrderCreateReq> orderCreateReqApiRequest = buildRequest(request);
        replayValidator.validate(orderCreateReqApiRequest, SECRET);
    }

    /**
     * 生成合法的 ApiRequest（带 sign、timestamp、nonce）
     */
    public static <T> ApiRequest<T> buildRequest(T bizContent) {
        String SECRET = "my-secret-2025";

        ApiRequest<T> req = new ApiRequest<>();
        req.setTimestamp(System.currentTimeMillis() / 1000);
        req.setNonce(UUID.randomUUID().toString());
        req.setBizContent(bizContent);

        // 签名
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("timestamp", req.getTimestamp());
        map.put("nonce", req.getNonce());
        map.put("bizContent", JSON.toJSONString(req.getBizContent()));

        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (v != null) sb.append(k).append("=").append(v).append("&");
        });
        if (StringUtils.isNotEmpty(sb)) sb.setLength(sb.length() - 1);

        String serverSign = HmacSha256Utils.hmacSha256(sb.toString(), SECRET);
        req.setSign(serverSign);

        return req;
    }

}
