package com.business.intelligence.controller;

import com.business.intelligence.crawler.mt.MTCrawler;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bi/mt")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MTController {

    @Autowired
    private MTCrawler mtCrawler;
    @Autowired
    private UserDao userdao;

    /**
     * 获取某一个用户名的各种信息
     *
     * @param userName
     * @return
     */
    public Authenticate getUser(String userName) {
        log.info("开始获取 {} 的信息", userName);
        User user = userdao.ifExists(userName, Platform.MT.name());
        if (user == null) {
            return null;
        } else {
            Authenticate authenticate = new Authenticate();
            authenticate.setUserName(user.getUserName());
            authenticate.setPassword(user.getPassWord());
            return authenticate;
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
            list.add(authenticate);
        }
        log.info("所有美团商户信息已经加载完成");
        return list;
    }


    @RequestMapping(value = "bizDataReport", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "经营分析----报表下载", httpMethod = "GET")
    public String bizDataReport(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String userName) throws InterruptedException {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.bizDataReport(startTime, endTime, true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.bizDataReport(startTime, endTime, true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "bizDataReport is ok";
    }


    @RequestMapping(value = "businessStatistics", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "经营分析----营业统计", httpMethod = "GET")
    public String businessStatistics(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String userName) throws InterruptedException, ParseException {
        startTime = DateFormatUtils.format(DateUtils.parseDate(startTime, "yyyy-MM-dd"), "yyyyMMdd");
        endTime = DateFormatUtils.format(DateUtils.parseDate(endTime, "yyyy-MM-dd"), "yyyyMMdd");
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.businessStatistics(startTime, endTime, true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.businessStatistics(startTime, endTime, true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "businessStatistics is ok";
    }


    @RequestMapping(value = "flow", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "经营分析----流量分析", httpMethod = "GET")
    public String flowanalysis(@RequestParam String userName) throws InterruptedException {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.flowanalysis("30", true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.flowanalysis("30", true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "flowanalysis is ok";
    }


    @RequestMapping(value = "hotSales", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "经营分析----热门商品", httpMethod = "GET")
    public String hotSales(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String userName) throws InterruptedException {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.hotSales(startTime, endTime, true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.hotSales(startTime, endTime, true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "hotSales is ok";
    }

    @RequestMapping(value = "comment", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "爬取全部评论", httpMethod = "GET")
    public String comment(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String userName) {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.comment(startTime, endTime, true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.comment(startTime, endTime, true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "comment is ok";
    }


    @RequestMapping(value = "bill", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "财务管理---订单对账", httpMethod = "GET")
    public String bill(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String userName) {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.historySettleBillList(startTime, endTime, true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.historySettleBillList(startTime, endTime, true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "bill is ok";
    }

    @RequestMapping(value = "acts", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "营销活动---活动", httpMethod = "GET")
    public String acts(@RequestParam String userName) {
        if (userName.isEmpty()) {
            List<Authenticate> authenticates = getAllUser();
            for (Authenticate authenticate : authenticates) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.acts(true);
            }
        } else {
            Authenticate authenticate = getUser(userName);
            if (authenticate != null) {
                mtCrawler.setAuthenticate(authenticate);
                mtCrawler.acts(true);
            } else {
                log.info("没有找到 {} 用户的信息", userName);
            }
        }
        return "bill is ok";
    }
}
