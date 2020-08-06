package com.example.userserver.controller;


import com.example.commons.result.RestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
public class TestController {

    @PostMapping(value = "/import/excel")
    public RestResult<Object> testImport(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getName());
        return RestResult.success();
    }

}
