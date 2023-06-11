package com.example.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 16:48
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     *
     * @param title       标题行
     * @param headMap     属性-列头
     * @param jsonArray   数据集
     * @param datePattern 日期格式，传null值则默认 年月日
     * @param colWidth    列宽 默认 至少17个字节
     * @param out         输出流
     */
    public static void exportExcel(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern, int colWidth, OutputStream out) {
        if (headMap.isEmpty()) return;
        if (datePattern == null) {
            //默认日期格式
            String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";
            datePattern = DEFAULT_DATE_PATTERN;
        }
        // 声明一个工作薄
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000)) {//缓存
            workbook.setCompressTempFiles(true);
            //表头样式
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short) 20);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            // 列头样式
            CellStyle headerStyle = setHeatherCellStyle(workbook);
            // 单元格样式
            CellStyle cellStyle = setCellStyle(workbook);

            // 生成一个(带标题)表格
            SXSSFSheet sheet = workbook.createSheet();
            //设置列宽
            int DEFAULT_COLOUMN_WIDTH = 17;
            int minBytes = Math.max(colWidth, DEFAULT_COLOUMN_WIDTH);//至少字节数
            int[] arrColWidth = new int[headMap.size()];
            // 产生表格标题行,以及设置列宽
            String[] properties = new String[headMap.size()];
            String[] headers = new String[headMap.size()];
            int index = 0;
            for (String fieldName : headMap.keySet()) {
                properties[index] = fieldName;
                headers[index] = headMap.get(fieldName);

                int bytes = headMap.get(fieldName).getBytes().length;
                arrColWidth[index] = Math.max(bytes, minBytes);
                sheet.setColumnWidth(index, arrColWidth[index] * 256);
                index++;
            }
            // 遍历集合数据，产生数据行
            int rowIndex = 0;
            for (Object obj : jsonArray) {
                if (rowIndex == 65535 || rowIndex == 0) {
                    if (rowIndex != 0) {
                        sheet = workbook.createSheet();
                        index = 0;
                        for (String fieldName : headMap.keySet()) {
                            properties[index] = fieldName;
                            headers[index] = headMap.get(fieldName);

                            int bytes = fieldName.getBytes().length;
                            arrColWidth[index] = Math.max(bytes, minBytes);
                            sheet.setColumnWidth(index, arrColWidth[index] * 256);
                            index++;
                        }
                    }//如果数据超过了，则在第二页显示

                    SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                    titleRow.createCell(0).setCellValue(title);
                    titleRow.getCell(0).setCellStyle(titleStyle);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                    SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                    for (int i = 0; i < headers.length; i++) {
                        headerRow.createCell(i).setCellValue(headers[i]);
                        headerRow.getCell(i).setCellStyle(headerStyle);

                    }
                    rowIndex = 2;//数据内容从 rowIndex=2开始
                }

                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                SXSSFRow dataRow = sheet.createRow(rowIndex);
                for (int i = 0; i < properties.length; i++) {
                    SXSSFCell newCell = dataRow.createCell(i);

                    Object o = jo.get(properties[i]);
                    String cellValue;
                    if (o == null) {
                        cellValue = "";
                    } else if (o instanceof Date) {
                        cellValue = new SimpleDateFormat(datePattern).format(o);
                    } else if (o instanceof Float || o instanceof Double) {
                        cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    } else {
                        cellValue = o.toString();
                    }

                    newCell.setCellValue(cellValue);
                    newCell.setCellStyle(cellStyle);
                }
                rowIndex++;
            }
            workbook.write(out);
            workbook.dispose();
        } catch (Exception e) {
            log.error("导出Excel异常", e);
        }
    }

    /**
     * 设置单元格样式
     *
     * @param workbook workbook
     * @return 单元格样式对象
     */
    private static CellStyle setCellStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        // cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBold(true);
        cellStyle.setFont(cellFont);
        return cellStyle;

    }

    /**
     * 设置头部单元格样式
     *
     * @param workbook workbook
     * @return 单元格样式对象
     */
    private static CellStyle setHeatherCellStyle(SXSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        // headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 字体样式
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;

    }


}
