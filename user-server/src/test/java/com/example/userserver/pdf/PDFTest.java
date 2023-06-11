package com.example.userserver.pdf;

import com.itextpdf.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/13 20:38
 */
public class PDFTest {

    public static void main(String[] args) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // 设置模版前缀，相当于需要在资源文件夹中创建一个html2pdfTemplate文件夹，所有的模版都放在这个文件夹中
        resolver.setPrefix("/template/");
        // 设置模版后缀
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        // 设置模版模型为HTML
        resolver.setTemplateMode("HTML");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);


        // 创建model，需要指定模版引擎和具体的模版，“报告模版”指的是资源目录下/html2pdfTemplate/报告模版.html文件。如果是springboot项目，那么就是在resources文件夹下面
        Model model = new Model(engine,"test");
        model.setName("名称");
        List<Model.InsuranceInfo> insuranceInfos = new ArrayList<>(10);
        Model.InsuranceInfo record1 = new Model.InsuranceInfo();
        record1.setExpirationDate("2021-01-19");
        record1.setDescription("刹车失灵");
        insuranceInfos.add(record1);
        Model.InsuranceInfo record2 = new Model.InsuranceInfo();
        record2.setExpirationDate("2021-03-06");
        record2.setDescription("挡风玻璃破裂");
        insuranceInfos.add(record2);
        model.setInsuranceInfos(insuranceInfos);
        //生成pdf，指定目标文件路径
        try {
            model.parseToPdf("E:\\test\\pdf\\test.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
