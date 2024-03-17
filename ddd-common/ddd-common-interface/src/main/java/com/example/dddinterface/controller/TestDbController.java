package com.example.dddinterface.controller;

import com.example.commons.result.RestResult;
import com.example.dddapplication.service.ITestDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("testDb")
@Api(tags = "通用接口")
public class TestDbController {

    @Autowired
    private ITestDbService testDbService;


    @ApiOperation("save")
    @GetMapping("save")
    public RestResult<Object> save(HttpServletResponse response) throws Exception {
        testDbService.save();
        return RestResult.success(null);
    }

}
