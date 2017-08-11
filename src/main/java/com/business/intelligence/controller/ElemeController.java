package com.business.intelligence.controller;

import com.business.intelligence.crawler.eleme.*;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.Platform;
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
        log.info("开始获取 {} 的信息", userName);
        User elmUser = userdao.ifExists(userName, "ELM");
        if(elmUser == null){
            return null;
        }else{
            ElemeBean elemeBean = new ElemeBean();
            elemeBean.setUsername(elmUser.getUserName());
            elemeBean.setPassword(elmUser.getPassWord());
            elemeBean.setShopId(elmUser.getShopId());
            return elemeBean;
        }
    }

    /**
     * 获取所有的商户信息
     */
    public List<ElemeBean> getAllElemeBeans(){
        log.info("开始获取饿了么所有商户信息");
        List<ElemeBean> list = new ArrayList<>();
        List<User> elmUserList = userdao.getUsersForPlatform(Platform.ELM);
        for(User elmUser : elmUserList){
            ElemeBean elemeBean = new ElemeBean();
            elemeBean.setUsername(elmUser.getUserName());
            elemeBean.setPassword(elmUser.getPassWord());
            elemeBean.setShopId(elmUser.getShopId());
            list.add(elemeBean);
        }
        log.info("所有饿了么商户信息已经加载完成");
        return list;
    }

    @RequestMapping(value = "crawlerEvaluate", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String crawlerEvaluate(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeEvaluateCrawler.doRun(eb,startTime,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeEvaluateCrawler.doRun(bean,startTime,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerEvaluate is ok";
    }

    @RequestMapping(value = "crawlerActivity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部活动", httpMethod = "GET")
    public String crawlerActivity( @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeActivityCrawler.doRun(eb);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeActivityCrawler.doRun(bean);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerActivity is ok";
    }

    @RequestMapping(value = "crawlerBill", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部账单", httpMethod = "GET")
    public String crawlerBill(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeBillCrawler.doRun(eb,startTime,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeBillCrawler.doRun(bean,startTime,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerBill is ok";
    }
    @RequestMapping(value = "crawlerCommodity", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部商品", httpMethod = "GET")
    public String crawlerCommodity( @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeCommodityCrawler.doRun(eb,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeCommodityCrawler.doRun(bean,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerCommodity is ok";
    }
    @RequestMapping(value = "crawlerFlow", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取流量统计", httpMethod = "GET")
    public String crawlerFlow(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeFlowCrawler.doRun(eb,startTime,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeFlowCrawler.doRun(bean,startTime,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerFlow is ok";
    }
    @RequestMapping(value = "crawlerSale", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取营业统计", httpMethod = "GET")
    public String crawlerSale(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeSaleCrawler.doRun(eb,startTime,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeSaleCrawler.doRun(bean,startTime,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerSale is ok";
    }

    @RequestMapping(value = "crawlerOrder", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取所以订单", httpMethod = "GET")
    public String crawlerOrder(@RequestParam String startTime, @RequestParam String  endTime, @RequestParam String userName) {
        if(userName.isEmpty()){
            List<ElemeBean> allElemeBeans = getAllElemeBeans();
            for(ElemeBean eb : allElemeBeans){
                elemeOrderCrawler.doRun(eb,startTime,endTime);
            }
        }else{
            ElemeBean bean = getBeans(userName);
            if(bean != null){
                elemeOrderCrawler.doRun(bean,startTime,endTime);
            }else{
                log.info("没有找到 {} 用户的信息",userName);
            }
        }
        return "CrawlerOrder is ok";
    }
}
