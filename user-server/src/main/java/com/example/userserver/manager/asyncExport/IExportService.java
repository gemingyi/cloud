package com.example.userserver.manager.asyncExport;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2023/9/9 14:05
 */
public interface IExportService {

    String getTemplatePath();

    /**
     * 设置导出excel名称
     */
    String buidExcelName();

    /**
     *  设置导出excel头
     */
    Map<String, String> buidExcelHead();

    /**
     * 导出excel总条数
     */
    Long getExportTotal(SelectReq selectReq);

    /**
     * 导出excel总条数数据
     */
    List<Object> selectExportList(SelectReq selectReq, Integer currentPage, Integer pageSize);

}
