package com.example.commons.utils.pdf;

import com.google.common.collect.Maps;
import com.itextpdf.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/13 20:00
 */
public class AbstractTemplate {

    // 使用thymeleaf模版引擎
    private TemplateEngine engine;
    // 模版名称
    private String templateName;

    private AbstractTemplate() {
    }

    public AbstractTemplate(TemplateEngine engine, String templateName) {
        this.engine = engine;
        this.templateName = templateName;
    }

    /**
     * 模版名称
     */
    protected String templateName() {
        return this.templateName;
    }

    /**
     * 所有的参数数据
     */
    private Map<String, Object> variables() {
        Map<String, Object> variables = Maps.newHashMap();
        // 对应html模版中的template变量，取值的时候就按照“${template.字段名}”格式，可自行修改
        variables.put("template", this);
        return variables;
    }

    /**
     * 解析模版，生成html
     */
    public String process() {
        Context ctx = new Context();
        // 设置model
        ctx.setVariables(variables());
        // 根据model解析成html字符串
        return engine.process(templateName(), ctx);
    }


    public void parseToPdf(String targetPdfFilePath) throws IOException, DocumentException {
        String html = process();
        // 通过html转换成pdf
        PdfUtil.createPdfByHtml(html, targetPdfFilePath);
    }

}
