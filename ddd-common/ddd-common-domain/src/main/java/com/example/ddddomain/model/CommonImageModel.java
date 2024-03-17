package com.example.ddddomain.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

/**
 * <p>
 * 图片oss
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
public class CommonImageModel {

    private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private BigDecimal fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 是否删除 0否1是
     */
    private Integer isDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "generate_CommonImage{" +
        "id=" + id +
        ", fileType=" + fileType +
        ", fileSize=" + fileSize +
        ", filePath=" + filePath +
        ", businessType=" + businessType +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", isDelete=" + isDelete +
        "}";
    }
}
