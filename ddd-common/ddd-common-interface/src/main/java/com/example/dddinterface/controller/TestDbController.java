package com.example.dddinterface.controller;

import com.alibaba.fastjson.JSON;
import com.example.commons.result.RestResult;
import com.example.dddapplication.service.ITestDbService;
import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.req.TestDbModelDetailReq;
import com.example.ddddomain.req.TestDbModelQueryReq;
import com.example.platformboot.config.threadPool.MonitorThreadPool;
import com.example.platformboot.config.threadPool.ThreadPoolManager;
import com.example.pluginmysql.model.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.plugindesensitize.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("testDb")
@Api(tags = "通用接口")
public class TestDbController {

    @Autowired
    private LoggingSystem loggingSystem;

    @Autowired
    private ITestDbService testDbService;

    @Autowired
    private ThreadPoolManager threadPoolManager;

//    @Autowired
//    @Qualifier("test1TtlExecutor")
//    private Executor test1Executor;

    @PostMapping("find-page")
    public RestResult<PageVO<TestDbModel>> findPage(@RequestBody TestDbModelQueryReq req) {
        PageVO<TestDbModel> page = testDbService.findPage(req);
        RestResult<PageVO<TestDbModel>> success = RestResult.success(page);
        String s = JsonUtils.toJsonStringWithSensitivity(success);
        System.out.println(s);
        return success;
    }

    @PostMapping("save")
    public RestResult<Integer> delete(@RequestBody TestDbModel req) {
        Integer insert = testDbService.insert(req);
        return RestResult.success(insert);
    }

    @PostMapping("delete")
    public RestResult<Integer> delete(@RequestBody TestDbModelDetailReq req) {
        Integer delete = testDbService.delete(req);
        return RestResult.success(delete);
    }

    @PostMapping("update")
    public RestResult<Integer> update(@RequestBody TestDbModel req) {
        Integer delete = testDbService.update(req);
        return RestResult.success(delete);
    }

    @PostMapping("detail")
    public RestResult<TestDbModel> detail(@RequestBody TestDbModelDetailReq req) {
        TestDbModel detail = testDbService.findDetail(req);
        return RestResult.success(detail);
    }



    @ApiOperation("save")
    @GetMapping("save")
    public RestResult<Object> save(HttpServletResponse response) throws Exception {
        testDbService.save();
        return RestResult.success(null);

//        testDbService.saveMaster();
//        return RestResult.success(null);
    }


    @GetMapping("aaa")
    public RestResult<Object> aaa(HttpServletResponse response) {
        MonitorThreadPool test1 = threadPoolManager.getThreadPoolExecutor("test1");
        for (int i = 0; i < 10; i++) {
            test1.execute(() -> {
                Thread thread = Thread.currentThread();
                System.out.println(thread.getName());
            });
        }
        System.out.println(test1.getAverageCostTime());
        return RestResult.success(null);
    }

//    @GetMapping("aaa")
//    public RestResult<Object> aaa(HttpServletResponse response) {
//        for (int i = 0; i < 10; i++) {
//            test1Executor.execute(() -> {
//                Thread thread = Thread.currentThread();
//                System.out.println(thread.getName());
//            });
//        }
//        Thread thread = Thread.currentThread();
//        System.out.println(thread.getName());
//        return RestResult.success(null);
//    }



    @RequestMapping(value = "/testLog", method = RequestMethod.GET)
    public String  testLog(String level) {
        if ("debug".equals(level)){
            log.debug("this is test debug level");
        }else if ("info".equals(level)){
            log.info("this is test info level");
        }else if ("warn".equals(level)){
            log.warn("this is test warn level");
        }else if ("error".equals(level)){
            log.error("this is test error level");
        }else {
            return "输入必须是debug, info, warn, error 之一";
        }
        return "OK";
    }

    @RequestMapping(value = "/changeLogLevel", method = RequestMethod.GET)
    public String changeLogLevel(String loggerName, String newLevel) {
        log.info("更新日志级别:{}", newLevel);
        LogLevel level = LogLevel.valueOf(newLevel.toUpperCase());
        loggingSystem.setLogLevel(loggerName, level);
        log.info("更新日志级别:{} 更新完毕", newLevel);
        List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
        return JSON.toJSONString(loggerConfigurations);
    }

    @RequestMapping(value = "/getAllConfiguration", method = RequestMethod.GET)
    public String getAllConfiguration() {
        List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
        log.info(JSON.toJSONString(loggerConfigurations));
        return JSON.toJSONString(loggerConfigurations);
    }


}
