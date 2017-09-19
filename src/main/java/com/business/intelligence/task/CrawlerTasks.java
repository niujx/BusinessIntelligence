package com.business.intelligence.task;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.crawler.baidu.WaimaiApi;
import com.business.intelligence.crawler.baidu.WaimaiCrawler;
import com.business.intelligence.crawler.eleme.ElemeCrawlerAll;
import com.business.intelligence.crawler.mt.MTCrawler;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import com.business.intelligence.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.ApplicationContextTestUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
@Component
public class CrawlerTasks {
    @Autowired
    private ElemeCrawlerAll elemeCrawlerAll;

    @Autowired
    private UserDao userdao;

    @Autowired
    private MTCrawler mtCrawler;

    @Autowired
    private WaimaiCrawler bdCrawler;

    @Autowired
    private WaimaiApi bdApi;

    @Scheduled(cron = "0 0 13 * * *")
    public void doRun() {
        elemeCrawlerAll.runAllCrawler();
    }

    @Scheduled(cron = "0 30 13 * * *")
    public void runAllMtCrawler() throws InterruptedException {
        List<Authenticate> authenticates = getAllUser();
        Date endDate = new Date();
        Date startDate = DateUtils.addDays(new Date(), -30);

        String startTime = DateFormatUtils.format(startDate, "yyyy-MM-dd");
        String endTime = DateFormatUtils.format(endDate, "yyyy-MM-dd");
        String st = DateFormatUtils.format(startDate, "yyyyMMdd");
        String et = DateFormatUtils.format(endDate, "yyyyMMdd");


        for (Authenticate authenticate : authenticates) {
            MTCrawler.LoginBean loginBean = new MTCrawler.LoginBean();
            loginBean.setAuthenticate(authenticate);
            mtCrawler.setLoginBean(loginBean);
            mtCrawler.login(true);
          //  mtCrawler.bizDataReport(startTime, endTime, false);
            mtCrawler.businessStatistics(st, et, false);
            mtCrawler.flowanalysis("30", false);
            mtCrawler.hotSales(startTime, endTime, false);
            mtCrawler.acts(false);
            mtCrawler.comment(startTime, endTime, false);
            mtCrawler.historySettleBillList(startTime, endTime, false);
        }
    }

    @Scheduled(cron = "0 0 14 * * *")
    public void runAllBdCrawler() {
        List<User> users = getAllBdUser();
        Date startDate = new Date();
        Date endDate = DateUtils.addDays(new Date(), -30);

        String startTime = DateFormatUtils.format(startDate, "yyyy-MM-dd");
        String endTime = DateFormatUtils.format(endDate, "yyyy-MM-dd");
        for (User u : users) {
            if (u.getType().equals("1")) {
                log.info("百度当前执行商户{}", JSONObject.toJSONString(u));
                bdApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
                bdApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
            } else {
                bdCrawler.logins(u.getUserName(), u.getPassWord(), startTime, endTime, u.getMerchantId());
            }
        }
    }

    public List<Authenticate> getAllUser() {
        log.info("开始获取美团所有商户信息");
        List<Authenticate> list = new ArrayList<>();
        List<User> users = userdao.getUsersForPlatform(Platform.MT);
        for (User user : users) {
            Authenticate authenticate = new Authenticate();
            authenticate.setUserName(user.getUserName());
            authenticate.setPassword(user.getPassWord());
            authenticate.setMerchantId(user.getMerchantId());
            list.add(authenticate);
        }
        log.info("所有美团商户信息已经加载完成");
        return list;
    }

    public List<User> getAllBdUser() {
        List<User> users = userdao.getUsersForPlatform(Platform.BD);
        log.info("百度定时任务需要执行的商户{}", JSONObject.toJSONString(users));
        return users;
    }
}
