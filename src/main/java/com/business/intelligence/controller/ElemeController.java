package com.business.intelligence.controller;

import com.business.intelligence.crawler.ElemeCrawler.ElemeOrderCrawler;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tcqq on 2017/7/15.
 */
@Slf4j
@RestController
@RequestMapping("/bi/eleme")
public class ElemeController {

    @RequestMapping(value = "crawlerEvaluate", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String crawlerEvaluate(@RequestBody String date) {
        ElemeOrderCrawler elemeOrderCrawler = new ElemeOrderCrawler();
//        if(){
//            elemeOrderCrawler.setCrawlerDate();
//        }
        elemeOrderCrawler.doRun();
        return "Crawler is ok";
    }
}
