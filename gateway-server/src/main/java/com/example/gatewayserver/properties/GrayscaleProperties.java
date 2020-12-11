package com.example.gatewayserver.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 灰度路由 配置类
 */
@Data
public class GrayscaleProperties implements Serializable {
    // api_version
    private String version;
    //
    private String serverName;
    private String serverGroup;
    private String active;
    private double weight = 1.0D;
}