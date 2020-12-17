package com.example.plugingray.gateway;


import java.io.Serializable;

/**
 * 网关灰度路由 配置类
 */
public class GrayscaleProperties implements Serializable {
    // api_version
    private String version;
    //
    private String serverName;
    private String serverGroup;
    private String active;
    private double weight = 1.0D;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}