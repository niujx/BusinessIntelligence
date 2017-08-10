package com.business.intelligence.controller;

import com.business.intelligence.crawler.eleme.*;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserDao userdao;


    /**
     * 获取某一个用户名的各种信息
     * @param userName
     * @return
     */
    public ElemeBean getBeans(String userName){
        User elmUser = userdao.ifExists(userName, "ELM");
        ElemeBean elemeBean = new ElemeBean();
        elemeBean.setUsername(elmUser.getUserName());
        elemeBean.setPassword(elmUser.getPassWord());
        elemeBean.setShopId(elmUser.getShopId());
        return elemeBean;
    }

    @RequestMapping(value = "crawlerEvaluate", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String crawlerEvaluate(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        elemeEvaluateCrawler.doRun(getBeans(userName),startTime,endTime);
        return "CrawlerEvaluate is ok";
    }

    @RequestMapping(value = "crawlerActivity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部活动", httpMethod = "GET")
    public String crawlerActivity( @RequestParam String userName) {
        elemeActivityCrawler.doRun(getBeans(userName));
        return "CrawlerActivity is ok";
    }

    @RequestMapping(value = "crawlerBill", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部账单", httpMethod = "GET")
    public String crawlerBill(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        elemeBillCrawler.doRun(getBeans(userName),startTime,endTime);
        return "CrawlerBill is ok";
    }
    @RequestMapping(value = "crawlerCommodity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部商品", httpMethod = "GET")
    public String crawlerCommodity( @RequestParam String  endTime, @RequestParam String userName) {
        elemeCommodityCrawler.doRun(getBeans(userName),endTime);
        return "CrawlerCommodity is ok";
    }
    @RequestMapping(value = "crawlerFlow", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取流量统计", httpMethod = "GET")
    public String crawlerFlow(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        elemeFlowCrawler.doRun(getBeans(userName),startTime,endTime);
        return "CrawlerFlow is ok";
    }
    @RequestMapping(value = "crawlerSale", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取营业统计", httpMethod = "GET")
    public String crawlerSale(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        elemeSaleCrawler.doRun(getBeans(userName),startTime,endTime);
        return "CrawlerSale is ok";
    }

    @RequestMapping(value = "crawlerOrder", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取所以订单", httpMethod = "GET")
    public String crawlerOrder(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        elemeOrderCrawler.doRun(getBeans(userName),startTime,endTime);
        return "CrawlerOrder is ok";
    }
}
