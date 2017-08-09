package com.business.intelligence.controller;

import com.business.intelligence.crawler.baidu.WaimaiApi;
import com.business.intelligence.crawler.baidu.WaimaiCrawler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "/bi/v1/", description = "百度外卖api")
@RequestMapping("/bi/v1/")
public class BaiduController {
    private static final Logger logger = LoggerFactory.getLogger(BaiduController.class);
    @Autowired
    private WaimaiApi waimaiApi;

    @Autowired
    private WaimaiCrawler waimaiCrawler;

    @RequestMapping(value = "/getOrderList/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取订单信息", httpMethod = "GET", notes = "根据时间段获取订单信息")
    public Object getOrderList(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                               @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end) {
        String content = waimaiApi.ouderListGet();
        return content;
    }

    @RequestMapping(value = "/getComment/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取评论信息", httpMethod = "GET", notes = "根据时间段获取评论信息")
    public Object getComment(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                             @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end) {
        String content = waimaiApi.commentGet();
        return content;
    }

    @RequestMapping(value = "/getHotDishes/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取热销菜品", httpMethod = "GET", notes = "根据时间段获取热销菜品")
    public void getHotDishes(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                             @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end) {
        waimaiCrawler.logins("twfhscywjd","wang170106",star,end,"100010");
    }
}

