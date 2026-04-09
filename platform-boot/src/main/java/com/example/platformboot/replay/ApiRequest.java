package com.example.platformboot.replay;

import lombok.Data;

@Data
public class ApiRequest<T> {

    /**
     * 秒级时间戳
     */
    private Long timestamp;

    /**
     * 唯一随机串
     */
    private String nonce;

    /**
     * 签名
     */
    private String sign;

    /**
     * 业务数据（任意业务对象）
     */
    private T bizContent;
}