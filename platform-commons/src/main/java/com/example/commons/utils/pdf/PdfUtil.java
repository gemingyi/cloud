package com.example.commons.utils.pdf;


import com.alibaba.fastjson.JSON;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * https://zhuanlan.zhihu.com/p/31697208
 * https://zhuanlan.zhihu.com/p/378852796
 */
public class PdfUtil {
    private static Logger log = LoggerFactory.getLogger(PdfUtil.class);

    public PdfUtil() {
    }


    public static String createPdfByHtml(String htmlText, String pdfTargetPath) throws IOException, DocumentException {


        Document document = null;
        try {
            File file = new File(pdfTargetPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//            PDFBuilder pdfBuilder = new PDFBuilder();
//            writer.setPageEvent(pdfBuilder);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8, new PdfFont());
        } finally {
            if (document != null) {
                document.close();
            }
        }

        return pdfTargetPath;
    }

    public static String createPdfByHtml(String htmlTemplatePath, String pdfTargetPath, Map<String, String> params) throws IOException, DocumentException {
        Document document = null;

        try {
            if (StringUtils.isEmpty(htmlTemplatePath) || StringUtils.isEmpty(pdfTargetPath) || StringUtils.isEmpty(params)) {
                throw new NullPointerException("pdf生成参数缺失htmlTemplatePath=" + htmlTemplatePath + "，pdfTargetPath=" + pdfTargetPath + "，params=" + JSON.toJSONString(params));
            }
            // 设置模板内容
            String templateText = processHtmlTemplateText(htmlTemplatePath, params);
            if (StringUtils.isEmpty(templateText)) {
                throw new NullPointerException("pdf生成获取模板string为空htmlTemplatePath=" + htmlTemplatePath + "，pdfTargetPath=" + pdfTargetPath + "，params=" + JSON.toJSONString(params));
            }

            File file = new File(pdfTargetPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            PDFBuilder pdfBuilder = new PDFBuilder();
            writer.setPageEvent(pdfBuilder);
            document.open();
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(templateText.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8, new PdfFont());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(templateText.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } finally {
            if (document != null) {
                document.close();
            }
        }

        return pdfTargetPath;
    }

    /**
     * 可改成这个thymeleaf   或   Freemarker
     * 设置模板内容
     * @param htmlTemplatePath  html模板
     * @param params    参数
     * @return  text
     */
    public static String processHtmlTemplateText(String htmlTemplatePath, Map<String, String> params) throws IOException {
        String templateText = "";
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(htmlTemplatePath), StandardCharsets.UTF_8);
        Throwable var4 = null;

        try {
            if (!StringUtils.isEmpty(htmlTemplatePath) && !StringUtils.isEmpty(params)) {
                StringBuffer pdfStr = new StringBuffer();
                String tempStr;
                BufferedReader br = new BufferedReader(inStream);

                while ((tempStr = br.readLine()) != null) {
                    pdfStr.append(tempStr);
                }

                templateText = pdfStr.toString();
                if (StringUtils.isEmpty(templateText)) {
                    throw new NullPointerException("pdf生成模板文件为空htmlTemplatePath=" + htmlTemplatePath + "，params=" + JSON.toJSONString(params));
                } else {
                    Set<Map.Entry<String, String>> entries = params.entrySet();

                    // 替换参数
                    String key;
                    String value;
                    for (Iterator<Map.Entry<String, String>> var9 = entries.iterator(); var9.hasNext(); templateText = templateText.replace(key, value)) {
                        Map.Entry<String, String> temp = var9.next();
                        key = temp.getKey();
                        value = temp.getValue();
                    }

                    return templateText;
                }
            } else {
                throw new NullPointerException("pdf生成模板文件为空参数缺失htmlTemplatePath=" + htmlTemplatePath + "，params=" + JSON.toJSONString(params));
            }
        } catch (Throwable var20) {
            var4 = var20;
            throw var20;
        } finally {
            if (var4 != null) {
                try {
                    inStream.close();
                } catch (Throwable var19) {
                    var4.addSuppressed(var19);
                }
            } else {
                inStream.close();
            }

        }
    }

    public static void main(String[] aa) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("###sourceOrderNo###", "88888888");
            map.put("###logisticsNo###", "666666");
            map.put("###realName###", "冰冰");
            map.put("###idNo###", "888666");
            map.put("###materielModelName###", "魅族17");
            map.put("###pickQuantity###", "1");
            map.put("###imieNo###", "666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123,666888,8888888888,789456123");
            map.put("###sourceOrderNo###", "88888888");
            System.out.println(createPdfByHtml("C:\\Users\\dell\\Downloads\\444\\555\\gerenzu123.html", "E:\\zhou\\ttttt.pdf", map));
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }


    static class PdfFont extends FontFactoryImp {
        PdfFont() {
        }

        @Override
        public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
            BaseFont baseFont = null;

            try {
                baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            } catch (DocumentException var10) {
                PdfUtil.log.error("生产PDF异常：", var10);
            } catch (IOException var11) {
                PdfUtil.log.error("，生产PDF异常：", var11);
            }

            return new Font(baseFont, size, style, color);
        }
    }

}
