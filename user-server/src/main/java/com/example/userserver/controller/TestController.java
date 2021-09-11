package com.example.userserver.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.commons.exceptionHandle.exceptions.DataNotFoundException;
import com.example.commons.result.RestResult;
import com.example.platformboot.QueryCallable;
import com.example.platformboot.handler.GlobalSentinelBlockHandler;
import com.example.platformboot.handler.GlobalSentinelFallBackHandler;
import com.example.pluginredis.javascript.JavascriptTemplate;
import com.example.userserver.feign.TestFeign;
import com.example.userserver.dao.entity.Test;
import com.example.userserver.service.ITestService;
import com.example.userserver.service.IUserInfoService;
import com.example.userserver.service.impl.TestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("user")
@Api(tags = "测试接口")
public class TestController {

    @Autowired
    private TestFeign testFeign;
    @Autowired
    private ITestService testService;

    @Autowired
    private JavascriptTemplate javascriptTemplate;
    @Autowired
    private IUserInfoService userInfoService;

    ExecutorService executorService = Executors.newFixedThreadPool(4);


    @ApiOperation("测试异常")
    @GetMapping("/testException")
    public RestResult<Object> testException() {
        if (true) {
            throw new DataNotFoundException("找不到数据");
        }
        return RestResult.success();
    }

    @ApiOperation("测试重复提交接口")
    @GetMapping("/repeatSubmission")
    public RestResult<?> repeatSubmission() {
        Integer execute = javascriptTemplate.execute("repeatSubmission", 2, () -> {
            return userInfoService.testResult();
        });
        return RestResult.success(execute);

//        return javascriptTemplate.execute("repeatSubmission", 2, () -> {
//            Integer result = userInfoService.testResult();
//            return RestResult.success(result);
//        });
    }

    @ApiOperation("测试响应对象字段顺序")
    @GetMapping("/test")
//    @SentinelResource(value="/test")
    public RestResult<Object> test() {
        RestResult<Object> restResult = new RestResult<>();
        Test test = new Test();
        test.setTestId(1);
        test.setName("name");
        test.setPrice(BigDecimal.ZERO);
        return restResult.data(test);
    }

    @GetMapping("/callableTest")
    public RestResult<Object> callableTest() throws ExecutionException, InterruptedException {
        int maxLoopCount = 1;
        QueryCallable testCallable = new QueryCallable(
                TestServiceImpl.class, "getTestById", new Object[]{1}, new Class[]{Integer.class});
        Future<Object> future1 = executorService.submit(testCallable);

        QueryCallable testCallable2 = new QueryCallable(
                TestServiceImpl.class, "getById", new Object[]{3}, new Class[]{Integer.class});
        Future<Object> future2 = executorService.submit(testCallable2);

        boolean flag = false;
        do {
            if (future1.isDone() && future2.isDone()) {
                flag = true;
                break;
            }
            Thread.sleep(200);
            maxLoopCount ++ ;
        } while (maxLoopCount <= 5 );
        if (!flag) {
            return RestResult.failure("请求超时");
        }
        System.out.println(future1.get());
        System.out.println(future2.get());
        return RestResult.success();
    }


    //--------------------------------------------------------------------------

    @ApiOperation("测试远程接口")
    @GetMapping("/test1")
    @SentinelResource(value="test1",
            fallbackClass = {GlobalSentinelFallBackHandler.class}, fallback = "exceptionHandler",
            blockHandlerClass = {GlobalSentinelBlockHandler.class}, blockHandler = "blockHandler")
    public RestResult<Object> test1() {
        return testFeign.test1();
    }

    @ApiOperation("测试远程异常接口")
    @GetMapping("/test2")
//    @SentinelResource(value="/test2")
    public RestResult<Object> test2() {
        return testFeign.test2();
    }

    @ApiOperation("测试seataAT事务接口")
    @PostMapping("/test3")
//    @SentinelResource(value="/test3")
    public RestResult<Object> test3() {
        testService.seataTest();
        return RestResult.success();
    }

    @ApiOperation("测试seataTCC事务接口")
    @PostMapping("/test4")
//    @SentinelResource(value="/test3")
    public RestResult<Object> test4() {
        testService.seataTest2();
        return RestResult.success();
    }

    /**
     * 	@PostMapping(value = "image/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     * 	@RequestPart(value = "upfile") MultipartFile file
     *
     *  @PostMapping(value = "image/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     *  @RequestPart(value = "upfile") MultipartFile file
     */
    @ApiOperation("测试远程文件上传")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file")
    })
    public RestResult<Object> testImport(@RequestPart(value = "file", required = false) MultipartFile file) {
        return testFeign.testImport(file);
    }

    @ApiOperation("校验图片验证码")
    @GetMapping(value = "/checkValidateCode")
    RestResult<Object> checkValidateCode(String token, String validateCode) {
        return testFeign.checkValidateCode(token, validateCode);
    }

}
