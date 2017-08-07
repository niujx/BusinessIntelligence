package com.business.intelligence.controller;

import com.business.intelligence.crawler.eleme.*;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private ElemeActivityCrawler elemeActivityCrawler;
    @Autowired
    private ElemeBillCrawler elemeBillCrawler;
    @Autowired
    private ElemeCommodityCrawler elemeCommodityCrawler;
    @Autowired
    private ElemeFlowCrawler elemeFlowCrawler;
    @Autowired
    private ElemeSaleCrawler elemeSaleCrawler;


    public List<ElemeBean> getBeans(){
        List<ElemeBean> elemeBeans = new ArrayList<>();
        ElemeBean elemeBean = new ElemeBean();
        elemeBean.setUsername("hwfzhongke");
        elemeBean.setPassword("abc123456");
        elemeBean.setShopId("204666");
        elemeBean.setKsId("NTNlMmI4OTItODhjMC00ZGYzLTg3YTNWI5MW");
        return elemeBeans;
    }

    @RequestMapping(value = "crawlerEvaluate", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String crawlerEvaluate() {
        for(ElemeBean elemeBean : getBeans()){
            elemeEvaluateCrawler.doRun(elemeBean);
        }
        return "CrawlerEvaluate is ok";
    }

    @RequestMapping(value = "crawlerActivity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部活动", httpMethod = "GET")
    public String crawlerActivity() {
        for(ElemeBean elemeBean : getBeans()){
            elemeActivityCrawler.doRun(elemeBean);
        }
        return "CrawlerActivity is ok";
    }

    @RequestMapping(value = "crawlerBill", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部账单", httpMethod = "GET")
    public String crawlerBill() {
        for(ElemeBean elemeBean : getBeans()){
            elemeBillCrawler.doRun(elemeBean);
        }
        return "CrawlerBill is ok";
    }
    @RequestMapping(value = "crawlerCommodity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部商品", httpMethod = "GET")
    public String crawlerCommodity() {
        for(ElemeBean elemeBean : getBeans()){
            elemeCommodityCrawler.doRun(elemeBean);
        }
        return "CrawlerCommodity is ok";
    }
    @RequestMapping(value = "crawlerFlow", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取流量统计", httpMethod = "GET")
    public String crawlerFlow() {
        for(ElemeBean elemeBean : getBeans()){
            elemeFlowCrawler.doRun(elemeBean);
        }
        return "CrawlerFlow is ok";
    }
    @RequestMapping(value = "crawlerSale", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取营业统计", httpMethod = "GET")
    public String crawlerSale() {
        for(ElemeBean elemeBean : getBeans()){
            elemeSaleCrawler.doRun(elemeBean);
        }
        return "CrawlerSale is ok";
    }

    @RequestMapping(value = "crawlerOrder", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取所以订单", httpMethod = "GET")
    public String crawlerOrder() {
        for(ElemeBean elemeBean : getBeans()){
            elemeOrderCrawler.doRun(elemeBean);
        }
        return "CrawlerOrder is ok";
    }
}
