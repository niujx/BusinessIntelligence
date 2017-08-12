package com.business.intelligence.crawler;

import com.business.intelligence.BaseTest;
import com.business.intelligence.crawler.mt.MTCrawler;
import com.business.intelligence.model.Authenticate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MtCrawlerTest extends BaseTest {

    @Autowired
    private MTCrawler crawler;


    @Test
    public void testOrder() throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.bizDataReport("2017-07-20", "2017-08-07", false);
    }



    @Test
    public void testBusiness() throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.businessStatistics("20170707", "20170805", false);
    }

    @Test
    public void insertAnalysis(){
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.flowanalysis("30", false);
    }

    @Test
    public void hotSales(){
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.hotSales("2017-07-31","2017-08-06",true);
    }

    @Test
    public void comment(){
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.comment("2017-08-01", "2017-08-06", true);
    }


    @Test
    public void bill(){
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.historySettleBillList("2017-07-05", "2017-08-02", true);
    }

}
