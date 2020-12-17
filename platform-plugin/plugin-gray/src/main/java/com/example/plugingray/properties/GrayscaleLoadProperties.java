package com.example.plugingray.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author mingyi ge
 * @Description: 开启灰度负载 配置类
 * @date 2020/12/14 14:01
 */
@Component
@ConfigurationProperties(prefix = "loverent.gray")
public class GrayscaleLoadProperties {

    /**
     * feign ribbon 灰度负载
     */
    private Boolean serviceOpen;

    /**
     * gateway 灰度负载
     */
    private Boolean gatewayOpen;

    /**
     * api请求头版本
     */
    private String apiVersionMark;

    @PostConstruct
    public void init() {
        //
        if (null == this.getGatewayOpen()) {
            this.setGatewayOpen(Boolean.FALSE);
        }
        if (null == this.getServiceOpen()) {
            this.setServiceOpen(Boolean.FALSE);
        }
    }

    public Boolean getServiceOpen() {
        return serviceOpen;
    }

    public void setServiceOpen(Boolean serviceOpen) {
        this.serviceOpen = serviceOpen;
    }

    public Boolean getGatewayOpen() {
        return gatewayOpen;
    }

    public void setGatewayOpen(Boolean gatewayOpen) {
        this.gatewayOpen = gatewayOpen;
    }

    public String getApiVersionMark() {
        return apiVersionMark;
    }

    public void setApiVersionMark(String apiVersionMark) {
        this.apiVersionMark = apiVersionMark;
    }
}
