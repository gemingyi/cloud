package com.example.userserver.manager;

import com.alibaba.fastjson.JSONArray;
import com.example.commons.utils.DateUtil8;
import com.example.commons.utils.ExcelUtil;
import com.example.commons.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 14:28
 */
@Slf4j
public class ASyncExportExcelTask implements Callable<Boolean> {

    private Long taskId;
    private SelectTypeEnum selectType;
    private SelectReq selectReq;

    private ASyncExportExcelTask() {
    }

    public ASyncExportExcelTask(Long taskId, SelectTypeEnum selectType, SelectReq selectReq) {
        this.taskId = taskId;
        this.selectType = selectType;
        this.selectReq = selectReq;
    }


    @Override
    public Boolean call() throws Exception {
        try {
            // 更新任务 导出中

            // 写数据到 文件
            File uploadFile = this.handlerData(selectType, selectReq);

            if (uploadFile == null || Thread.currentThread().isInterrupted()) {
                return false;
            }

            // 上传aliyun服务器
            boolean result = true;
            if (!result) {
                throw new RuntimeException("上传aliyun服务器失败");
            }

            return true;
        } catch (Exception e) {
            // 更新任务 导出失败
            ExportTask exportTask = new ExportTask();
            exportTask.setId(taskId);
            exportTask.setExportState(3);
            log.error("失败e:", e);
            return false;
        }
    }


    private File handlerData(SelectTypeEnum selectType, SelectReq selectReq) throws Exception {
        Map<String, String> headerMap = null;
        File uploadFile = null;

        // 测试导出类型1
        // 抽方法
        if (SelectTypeEnum.TEST_ONE.equals(selectType)) {
            String excelTitleName = DateUtil8.getNowDate_CN();
            headerMap = this.getExcelHeadByEnum(selectType);

            // 查询数据
            int total = 1;
            if (total == 1) {
                String zipName = "E:\\test\\tozip" + "_" + DateUtil8.getNowDate_CN() + ".zip";
                File zipFile = new File(zipName);
                // 分批处理
                int pageSize = 50;
                long pageCount = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
                for (int i = 0; i < pageCount; i++) {
                    // 查询数据数据
                    ExportTask exportTask = new ExportTask();
                    exportTask.setId(1L);
                    exportTask.setSelectCode("1");
                    exportTask.setSelectName("测试导出类型1");
                    List<Object> dataList = new ArrayList<>();
                    dataList.add(exportTask);
                    JSONArray jsonArray = new JSONArray(dataList);

                    String excelName = "测试导出类型1_" + i + ".xlsx";
                    writeDataToZipFile(zipFile, excelName, excelTitleName, headerMap, jsonArray);
                }
                uploadFile = zipFile;
            } else {
                // 查询数据数据
                List<Object> dataList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(dataList);

                String excelName = "E:\\test\\tozip" + "_" + DateUtil8.getNowDate_CN() + ".zip";
                File excelFile = new File(excelName);
                writeDataToExcel(excelFile, excelTitleName, headerMap, jsonArray);
                uploadFile = excelFile;
            }
        }

        // 测试导出类型2
        if (SelectTypeEnum.TEST_TWO.equals(selectType)) {

        }

        return uploadFile;
    }

    public Map<String, String> getExcelHeadByEnum(SelectTypeEnum selectType) {
        Map<String, String> resultMap;
        switch (selectType) {
            case TEST_ONE:
                resultMap = new HashMap<>();
                resultMap.put("id", "id");
                resultMap.put("selectCode", "导出编码");
                resultMap.put("selectName", "导出名称");
                break;
            case TEST_TWO:
                resultMap = new HashMap<>();
                resultMap.put("id", "id");
                resultMap.put("selectCode", "导出编码");
                resultMap.put("selectName", "导出名称");
                break;
            default:
                // 当前日期是当前年的第几天。例如：2019/1/3是2019年的第三天
                resultMap = Collections.emptyMap();
                break;
        }
        return resultMap;
    }


    private void writeDataToExcel(File excelFile, String excelTitleName, Map<String, String> headerMap, JSONArray jsonArray) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
        ExcelUtil.exportExcel(excelTitleName, headerMap, jsonArray, null, 0, fileOutputStream);
    }

    private void writeDataToZipFile(File zipFile, String excelName, String excelTitleName, Map<String, String> headerMap, JSONArray jsonArray) throws Exception {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        ExcelUtil.exportExcel(excelTitleName, headerMap, jsonArray, null, 0, aos);

        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipUtil.toZip(aos, excelName, fileOutputStream);
    }

}
