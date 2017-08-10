package com.business.intelligence.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
@RestController
@RequestMapping("/bi/test")
public class HelloWorldController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "测试用接口", httpMethod = "GET")
    public String hello(@RequestParam String userName , @RequestParam String startTime, @RequestParam String endTime) {

        System.out.println(userName);
        System.out.println(startTime);
        System.out.println(endTime);

        return "hello world";
    }
}
