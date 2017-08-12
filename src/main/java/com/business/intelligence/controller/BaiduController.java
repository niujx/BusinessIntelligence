package com.business.intelligence.controller;

import com.business.intelligence.crawler.baidu.WaimaiApi;
import com.business.intelligence.crawler.baidu.WaimaiCrawler;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value = "/bi/v1/", description = "百度外卖api")
@RequestMapping("/bi/v1/")
public class BaiduController {
    private static final Logger logger = LoggerFactory.getLogger(BaiduController.class);
    @Autowired
    private WaimaiApi waimaiApi;

    @Autowired
    private WaimaiCrawler waimaiCrawler;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/getOrderList/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取订单信息", httpMethod = "GET", notes = "根据时间段获取订单信息")
    public Object getOrderList(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                               @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end, @ApiParam(required = true, name = "userName", value = "用户名") @RequestParam String userName) {
        User u = userDao.ifExists(userName, Platform.MT.name());
        if (u != null) {
            waimaiApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), star.replace("/", "-"), end.replace("/", "-"));
            return "OrderList is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }

    @RequestMapping(value = "/getComment/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取评论信息", httpMethod = "GET", notes = "根据时间段获取评论信息")
    public Object getComment(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                             @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end, @ApiParam(required = true, name = "userName", value = "用户名") @RequestParam String userName) {
        User u = userDao.ifExists(userName, Platform.MT.name());
        if (u != null) {
            waimaiApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), star.replace("/", "-"), end.replace("/", "-"));
            return "Comment is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }

    @RequestMapping(value = "/getBaiduCarwler/", method = RequestMethod.GET)
    @ApiOperation(value = "根据时间段获取百度外卖商户数据", httpMethod = "GET", notes = "根据时间段获取百度外卖商户数据")
    public Object getBaiduCarwler(HttpServletRequest request, @ApiParam(required = true, name = "star", value = "起始时间") @RequestParam String star,
                                  @ApiParam(required = true, name = "end", value = "结束时间") @RequestParam String end, @ApiParam(required = true, name = "userName", value = "用户名") @RequestParam String userName) {
        User u = userDao.ifExists(userName, Platform.MT.name());
        if (u != null) {
            waimaiCrawler.logins(u.getUserName(), u.getPassWord(), star.replace("/", "-"), end.replace("/", "-"), u.getMerchantId());
            return "BaiduCarwler is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }
}

