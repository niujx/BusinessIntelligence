package com.business.intelligence.controller;

import com.business.intelligence.dao.CrawlerStatus;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.CrawlerName;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by zjy on 17/7/19.
 */
public class WebController {


    @RequestMapping(value = "getStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取爬虫抓取状态", httpMethod = "GET")
    public String getStatus(@RequestParam String crawlerName) {
        CrawlerStatus status = new CrawlerStatus();


        switch (crawlerName){
            case "ELM_CRAWLER_ACTIVITY" :
                return status.getStatus(CrawlerName.ELM_CRAWLER_ACTIVITY);
            case "ELM_CRAWLER_BILL":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_BILL);
            case "ELM_CRAWLER_COMMODITY":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_COMMODITY);
            case "ELM_CRAWLER_EVALUATE":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_EVALUATE);
            case "ELM_CRAWLER_FLOW":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_FLOW);
            case "ELM_CRAWLER_ORDER":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_ORDER);
            case "ELM_CRAWLER_SALE":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_SALE);
        }

        return "状态获取失败";
    }


    @RequestMapping(value = "getAllStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取爬虫抓取状态", httpMethod = "GET")
    public String getAllStatus(@RequestParam String crawlerName) {
        CrawlerStatus status = new CrawlerStatus();


        switch (crawlerName){
            case "ELM_CRAWLER_ACTIVITY" :
                return status.getStatus(CrawlerName.ELM_CRAWLER_ACTIVITY);
            case "ELM_CRAWLER_BILL":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_BILL);
            case "ELM_CRAWLER_COMMODITY":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_COMMODITY);
            case "ELM_CRAWLER_EVALUATE":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_EVALUATE);
            case "ELM_CRAWLER_FLOW":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_FLOW);
            case "ELM_CRAWLER_ORDER":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_ORDER);
            case "ELM_CRAWLER_SALE":
                return  status.getStatus(CrawlerName.ELM_CRAWLER_SALE);
        }

        return "状态获取失败";
    }




}
