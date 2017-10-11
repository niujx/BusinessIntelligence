package com.business.intelligence.crawler;

import com.business.intelligence.BaseTest;
import com.business.intelligence.crawler.mt.MTCrawler;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.task.CrawlerTasks;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MtCrawlerTest extends BaseTest {

    @Autowired
    private MTCrawler crawler;
    @Autowired
    private CrawlerTasks crawlerTasks;


    @Test
    public void testOrder() throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.bizDataReport("2017-07-2", "2017-08-07", true);
    }

    //wmxsty11599   2017/7/15   2017/8/15  wmmswM418379 7597739
    @Test
    public void testBusiness() throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.businessStatistics("20170820", "20170919", false);
    }

    @Test
    public void insertAnalysis() {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.flowanalysis("30", false);
    }

    @Test
    public void hotSales() {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.hotSales("2017-07-31", "2017-08-06", true);
    }

    @Test
    public void comment() {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("xsxl6");
        authenticate.setPassword("xiashi");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.comment("2017-09-23", "2017-09-24", true);
    }


    @Test
    public void bill() {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmjgzs1201776");
        authenticate.setPassword("13552175136");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.historySettleBillList("2017-10-10", "2017-10-10", true);
    }

    @Test
    public void acts() {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        authenticate.setMerchantId("TEST-ID");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.acts(true);
    }

    @Test
    public void taskScheduled() {
        crawlerTasks.runAllMtCrawler();
    }

}
