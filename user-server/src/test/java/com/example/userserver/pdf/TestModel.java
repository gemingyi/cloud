package com.example.userserver.pdf;

import com.example.commons.utils.pdf.AbstractTemplate;
import lombok.Data;
import org.thymeleaf.TemplateEngine;

import java.util.List;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/13 20:10
 */
@Data
class Model extends AbstractTemplate {
    // 构造函数
    public Model(TemplateEngine engine, String templateName) {
        super(engine, templateName);
    }
    // 名称
    private String name;
    // 保险记录
    private List<InsuranceInfo> insuranceInfos;


    @Data
    public static class InsuranceInfo{
        /** 出险日期 */
        private String expirationDate;
        /** 描述 */
        private String description;
    }
}

