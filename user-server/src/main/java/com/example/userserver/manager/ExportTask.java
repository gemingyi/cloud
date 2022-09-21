package com.example.userserver.manager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 16:09
 */
@Setter
@Getter
public class ExportTask {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "导出编码")
    private String selectCode;

    @ApiModelProperty(value = "导出名称")
    private String selectName;

    @ApiModelProperty(value = "导出链接路径")
    private String exportLinkUrl;

    @ApiModelProperty(value = "导出状态：0-开始，1-导出中，2-导出完成，3-导出失败, 4-导出取消")
    private Integer exportState;

    @ApiModelProperty(value = "导出条件")
    private String exportConditionStr;


    @ApiModelProperty(value = "操作人id")
    private String operationId;

    @ApiModelProperty(value = "操作人名称")
    private String operationName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
