package com.example.commons.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/6/13 19:57$
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 20200314;


    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
