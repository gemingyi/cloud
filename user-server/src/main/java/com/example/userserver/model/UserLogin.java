package com.example.userserver.model;

import java.util.Date;

public class UserLogin {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 微信平台下唯一id
     */
    private String wechatUnionid;

    /**
     * 账号状态
     */
    private Integer isLocked;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号码
     *
     * @return phone - 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取微信平台下唯一id
     *
     * @return wechat_unionid - 微信平台下唯一id
     */
    public String getWechatUnionid() {
        return wechatUnionid;
    }

    /**
     * 设置微信平台下唯一id
     *
     * @param wechatUnionid 微信平台下唯一id
     */
    public void setWechatUnionid(String wechatUnionid) {
        this.wechatUnionid = wechatUnionid;
    }

    /**
     * 获取账号状态
     *
     * @return is_locked - 账号状态
     */
    public Integer getIsLocked() {
        return isLocked;
    }

    /**
     * 设置账号状态
     *
     * @param isLocked 账号状态
     */
    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_date - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", phone=").append(phone);
        sb.append(", wechatUnionid=").append(wechatUnionid);
        sb.append(", isLocked=").append(isLocked);
        sb.append(", createDate=").append(createDate);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserLogin other = (UserLogin) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getWechatUnionid() == null ? other.getWechatUnionid() == null : this.getWechatUnionid().equals(other.getWechatUnionid()))
            && (this.getIsLocked() == null ? other.getIsLocked() == null : this.getIsLocked().equals(other.getIsLocked()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getWechatUnionid() == null) ? 0 : getWechatUnionid().hashCode());
        result = prime * result + ((getIsLocked() == null) ? 0 : getIsLocked().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        return result;
    }
}