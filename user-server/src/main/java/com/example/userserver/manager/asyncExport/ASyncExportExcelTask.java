package com.example.userserver.manager.asyncExport;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONArray;
import com.example.commons.utils.DateUtil8;
import com.example.commons.utils.ExcelUtil;
import com.example.commons.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 14:28
 */
@Slf4j
@Service
public class ASyncExportExcelTask implements Callable<Boolean> {


    private Long taskId;
    private SelectTypeEnum selectType;
    private SelectReq selectReq;

    public ASyncExportExcelTask() {}

    public ASyncExportExcelTask(Long taskId, SelectTypeEnum selectType, SelectReq selectReq) {
        this.taskId = taskId;
        this.selectType = selectType;
        this.selectReq = selectReq;
    }


    @Override
    public Boolean call() {
        try {
            // 更新任务 导出中

            // 写数据到 文件
            File uploadFile = this.handlerData(selectType, selectReq);

            if (uploadFile == null || Thread.currentThread().isInterrupted()) {
                return false;
            }

            // 上传aliyun服务器  更新任务 导出完成
            boolean result = true;
            if (!result) {
                throw new RuntimeException("上传aliyun服务器失败");
            }

            // 更新任务成功

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


    /**
     * 导出 处理数据
     * @param selectType    导出查询类型
     * @param selectReq 查询条件
     */
    private File handlerData(SelectTypeEnum selectType, SelectReq selectReq) throws Exception {
        //
        String excelTitleName = DateUtil8.getNowDate_CN();
        // excel表头
        IExportService exportService = HomeInfoHandlerFactory.get(selectType.getCode());
        String templatePath = exportService.getTemplatePath();
        String excelFileName = exportService.buidExcelName();
        Map<String, String> headerMap = exportService.buidExcelHead();
        long total = exportService.getExportTotal(selectReq);


        //
        File uploadFile = null;
        // 数据量大  导出为zip文件
        if (total >= 100) {
            String zipName = "E:\\test\\tozip" + "_" + DateUtil8.getNowDate_CN() + ".zip";
            File zipFile = new File(zipName);
            // 分批处理
            int pageSize = 50;
            long pageCount = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
            for (int i = 1; i <= pageCount; i++) {
                // 查询数据数据
                List<Object> dataList = exportService.selectExportList(selectReq, i, pageSize);
                excelFileName = excelFileName + "_" + i + ".xlsx";
                writeDataToZipFile(templatePath, zipFile, excelFileName, dataList);
//                JSONArray jsonArray = new JSONArray(dataList);
//                writeDataToZipFile(zipFile, excelFileName, excelTitleName, headerMap, jsonArray);
            }
            uploadFile = zipFile;
        } else {
            // 查询数据数据
            List<Object> dataList = exportService.selectExportList(selectReq, 0, (int) total);
            File excelFile = new File(excelFileName);
            writeDataToExcelTemplate(templatePath, excelFile, dataList);
//            JSONArray jsonArray = new JSONArray(dataList);
//            writeDataToExcel(excelFile, excelTitleName, headerMap, jsonArray);
            uploadFile = excelFile;
        }

        return uploadFile;
    }

//    /**
//     * 根据类型 获取导出头
//     * @param selectType    类型
//     */
//    public Map<String, String> getExcelHeadByEnum(SelectTypeEnum selectType) {
//        Map<String, String> resultMap;
//        switch (selectType) {
//            case TEST_ONE:
//                resultMap = exportService.buidExcelHead();
//                break;
//            case TEST_TWO:
//                resultMap = exportService.buidExcelHead();
//                break;
//            default:
//                resultMap = Collections.emptyMap();
//                break;
//        }
//        return resultMap;
//    }


    private void writeDataToExcel(File excelFile, String excelTitleName, Map<String, String> headerMap, JSONArray jsonArray) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
        ExcelUtil.exportExcel(excelTitleName, headerMap, jsonArray, null, 0, fileOutputStream);
    }

    /**
     * 导出数据到excel
     * @param templateFilePath  excel导出模板路径
     * @param excelFile excel目标文件
     * @param data  写入数据
     */
    private void writeDataToExcelTemplate(String templateFilePath, File excelFile, Object data) {
        ExcelWriter excelWriter = EasyExcel.write(excelFile).withTemplate(templateFilePath).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data, writeSheet);
        excelWriter.finish();
    }

    private void writeDataToZipFile(File zipFile, String excelName, String excelTitleName, Map<String, String> headerMap, JSONArray jsonArray) throws Exception {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        ExcelUtil.exportExcel(excelTitleName, headerMap, jsonArray, null, 0, aos);

        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipUtil.toZip(aos, excelName, fileOutputStream);
    }

    /**
     * 导出数据到 zip文件
     * @param templateFilePath  excel导出模板路径
     * @param zipFile   zip目标文件
     * @param excelName excel名称
     * @param data  写入数据
     * @throws Exception
     */
    private void writeDataToZipFile(String templateFilePath, File zipFile, String excelName, Object data) throws Exception {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(aos).withTemplate(templateFilePath).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data, writeSheet);
        excelWriter.finish();

        FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
        ZipUtil.toZip(aos, excelName, fileOutputStream);
    }
}
