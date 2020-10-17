package com.example.platformgenerator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器
 * https://blog.csdn.net/baiyan3212/article/details/84876654
 * https://baomidou.gitee.io/mybatis-plus-doc/#/generate-code
 */
public class CodeGenerator {

    // 模块信息
    private static List<ModuleInfo> moduleInfoList;
    // 数据库信息
    private static final String dataSource = "127.0.0.1:3306/";
    private static final String user = "root";
    private static final String password = "123456";
    private static final String dataBaseName = "test1";
    // 需要生成的表 ,分割
    private static final String tables = "test";

    static {
        // 设置模块信息
        moduleInfoList = new ArrayList<>();
        // 工程名，包名，表前缀
        moduleInfoList.add(ModuleInfo.instance("platform-generator", "com.example.platformgenerator", "test_1"));
    }

    public static void main(String[] args) {
        for (ModuleInfo info : moduleInfoList) {
            autoGenerator(info);
        }
    }

    private static void autoGenerator(ModuleInfo moduleInfo) {
        // 代码生成器
        AutoGenerator gen = new AutoGenerator();
        String projectPath = System.getProperty("user.dir");
        projectPath = projectPath + File.separator + moduleInfo.getProjectName();

        // -----------------------------数据源配置-----------------------------
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mysql://" + dataSource + dataBaseName + "?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        // dsc.setSchemaName("public");
        dataSourceConfig.setUsername(user);
        dataSourceConfig.setPassword(password);
        gen.setDataSource(dataSourceConfig);


        // -----------------------------全局配置-----------------------------
        GlobalConfig globalConfig = initGlobalConfig(projectPath);
        gen.setGlobalConfig(globalConfig);

        // -----------------------------包配置-----------------------------
        PackageConfig packageConfig = initPackageConfig(moduleInfo);
        gen.setPackageInfo(packageConfig);

        // -----------------------------自定义配置-----------------------------
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置 调整 xml 生成目录
        List<FileOutConfig> fileOutConfigs = new ArrayList<>();
        String finalProjectPath = projectPath;
        fileOutConfigs.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String preMapperName = tableInfo.getEntityName();
                if (preMapperName.contains("Model")) {
                    preMapperName = preMapperName.replace("Model", "");
                }
                return finalProjectPath + "/src/main/resources/mappers/" + preMapperName + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(fileOutConfigs);
        gen.setCfg(injectionConfig);

        // -----------------------------配置模板-----------------------------
        TemplateConfig templateConfig = initTemplateConfig();
        gen.setTemplate(templateConfig);

        // -----------------------------策略配置-----------------------------
        StrategyConfig strategyConfig = initStrategyConfig();
        gen.setStrategy(strategyConfig);

//		gen.setTemplateEngine(new FreemarkerTemplateEngine());
        gen.setTemplateEngine(new VelocityTemplateEngine());

        // 执行
        gen.execute();
    }

    public static GlobalConfig initGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "\\src\\main\\java");   //生成文件的输出目录
        globalConfig.setAuthor("GMY_GENERATE");  //作者
        globalConfig.setOpen(true);    //是否打开输出目录 默认值:true
        globalConfig.setFileOverride(true); //是否覆蓋已有文件 默认值：false
//        globalConfig.setSwagger2(true); //开启 swagger2 模式 默认false
//        globalConfig.setEntityName("%sEntity");			//实体命名方式  默认值：null 例如：%sEntity 生成 UserEntity
//        globalConfig.setMapperName("%sMapper");			//mapper 命名方式 默认值：null 例如：%sDao 生成 UserDao
//        globalConfig.setXmlName("%sMapper");				//Mapper xml 命名方式   默认值：null 例如：%sDao 生成 UserDao.xml
//        globalConfig.setServiceName("%sService");			//service 命名方式   默认值：null 例如：%sBusiness 生成 UserBusiness
//        globalConfig.setServiceImplName("%sServiceImpl");	//service impl 命名方式  默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
//        globalConfig.setControllerName("%sController");	//controller 命名方式    默认值：null 例如：%sAction 生成 UserAction
        return globalConfig;
    }

    private static PackageConfig initPackageConfig(ModuleInfo moduleInfo) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(null);
        packageConfig.setParent(moduleInfo.getPackageInfo());
        packageConfig.setMapper("dao");
        packageConfig.setEntity("model");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setController("controller");
        return packageConfig;
    }

    private static TemplateConfig initTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//        templateConfig.setEntity(null);
//        templateConfig.setService(null);
//        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        templateConfig.setXml(null);
        return templateConfig;
    }

    private static StrategyConfig initStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);    //表名生成策略
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);  //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
//        strategyConfig.setEntityLombokModel(true);    //【实体】是否为lombok模型（默认 false

        strategyConfig.setSuperEntityClass("com.example.commons.model.BaseModel");   // 公共父类
//        strategyConfig.setSuperEntityColumns("id");   // 父类中的公共字段

//        strategyConfig.setSuperControllerClass("你自己的父类控制器,没有就不用设置!"); //自定义继承的Controller类全称，带包名
//        strategyConfig.setRestControllerStyle(true);  //生成 @RestController 控制器
//        strategyConfig.setControllerMappingHyphenStyle(true);	//驼峰转连字符

        strategyConfig.setInclude(tables.split(","));
//        strategyConfig.setExclude(new String[]{"test"}); // 排除生成的表

//        strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_"); //表前缀
        return strategyConfig;
    }

}
