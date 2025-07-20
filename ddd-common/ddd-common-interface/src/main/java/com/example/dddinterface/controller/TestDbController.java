package com.example.dddinterface.controller;

import com.example.commons.result.RestResult;
import com.example.dddapplication.service.ITestDbService;
import com.example.platformboot.config.threadPool.MonitorThreadPool;
import com.example.platformboot.config.threadPool.ThreadPoolManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("testDb")
@Api(tags = "通用接口")
public class TestDbController {

    @Autowired
    private ITestDbService testDbService;

    @Autowired
    private ThreadPoolManager threadPoolManager;

//    @Autowired
//    @Qualifier("test1TtlExecutor")
//    private Executor test1Executor;


    @ApiOperation("save")
    @GetMapping("save")
    public RestResult<Object> save(HttpServletResponse response) throws Exception {
//        testDbService.save();
//        return RestResult.success(null);

        testDbService.saveMaster();
        return RestResult.success(null);
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

}
