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
    public void test() throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
        loginBean.setAuthenticate(authenticate);
        crawler.setLoginBean(loginBean);
        crawler.bizDataReport("2017-07-20", "2017-08-07", false);
    }

}
