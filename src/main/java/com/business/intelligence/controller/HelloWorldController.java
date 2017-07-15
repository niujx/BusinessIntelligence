package com.business.intelligence.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
@RestController
@RequestMapping("/bi/test")
public class HelloWorldController {

    @RequestMapping(value = "hello", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "测试用接口", httpMethod = "GET")
    public String hello() {
        return "hello world";
    }
}
