package com.business.intelligence.controller;

import com.business.intelligence.crawler.eleme.ElemeEvaluateCrawler;
import com.business.intelligence.crawler.eleme.ElemeOrderCrawler;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @Autowired
    private ElemeEvaluateCrawler elemeEvaluateCrawler;
    @Autowired
    private ElemeOrderCrawler elemeOrderCrawler;

    @RequestMapping(value = "crawlerOrder", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取所以订单", httpMethod = "GET")
    public String crawlerOrder() {
        elemeOrderCrawler.doRun();
        return "CrawlerOrder is ok";
    }

    @RequestMapping(value = "crawlerEvaluate", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String crawlerEvaluate() {
        elemeEvaluateCrawler.doRun();
        return "CrawlerEvaluate is ok";
    }
}
