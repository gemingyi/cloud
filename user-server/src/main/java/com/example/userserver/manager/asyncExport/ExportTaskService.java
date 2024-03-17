package com.example.userserver.manager.asyncExport;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2023/9/9 14:10
 */
@Service
public class ExportTaskService implements IExportService, InitializingBean {

    @Override
    public String getTemplatePath() {
        return "";
    }

    @Override
    public String buidExcelName() {
        return "测试导出类型";
    }

    @Override
    public Map<String, String> buidExcelHead() {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("id", "id");
        resultMap.put("selectCode", "导出编码");
        resultMap.put("selectName", "导出名称");
        return resultMap;
    }

    @Override
    public Long getExportTotal(SelectReq selectReq) {
        return 50L;
    }

    @Override
    public List<Object> selectExportList(SelectReq selectReq, Integer currentPage, Integer pageSize) {
        List<Object> dataList = new ArrayList<>(100);
        for (int i = 0; i < 50; i++) {
            // 查询数据数据
            ExportTask exportTask = new ExportTask();
            exportTask.setId(1L);
            exportTask.setSelectCode("1");
            exportTask.setSelectName("测试导出类型1");
            dataList.add(exportTask);
        }
        return dataList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HomeInfoHandlerFactory.register(SelectTypeEnum.TEST_ONE.getCode(), this);
    }
}
