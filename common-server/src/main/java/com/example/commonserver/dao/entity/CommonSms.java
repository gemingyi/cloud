package com.example.commonserver.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * <p>
 * 短信
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
@TableName("common_sms")
public class CommonSms {

    private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 发送手机
     */
    private String smsPhone;

    /**
     * 发送内容
     */
    private String smsContent;

    /**
     * 验证码
     */
    private String smsCode;

    /**
     * 验证码类型
     */
    private Integer smsType;

    /**
     * 是否使用 0否1是
     */
    private Integer isUser;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    /**
     * 请求ip
     */
    private String clientIp;

    /**
     * 请求头
     */
    private String clientAgent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSmsPhone() {
        return smsPhone;
    }

    public void setSmsPhone(String smsPhone) {
        this.smsPhone = smsPhone;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public Integer getIsUser() {
        return isUser;
    }

    public void setIsUser(Integer isUser) {
        this.isUser = isUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientAgent() {
        return clientAgent;
    }

    public void setClientAgent(String clientAgent) {
        this.clientAgent = clientAgent;
    }

    @Override
    public String toString() {
        return "generate_CommonSms{" +
        "id=" + id +
        ", smsPhone=" + smsPhone +
        ", smsContent=" + smsContent +
        ", smsCode=" + smsCode +
        ", smsType=" + smsType +
        ", isUser=" + isUser +
        ", createTime=" + createTime +
        ", clientIp=" + clientIp +
        ", clientAgent=" + clientAgent +
        "}";
    }
}
