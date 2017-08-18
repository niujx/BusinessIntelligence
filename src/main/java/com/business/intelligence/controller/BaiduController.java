package com.business.intelligence.controller;

import com.business.intelligence.crawler.baidu.WaimaiApi;
import com.business.intelligence.crawler.baidu.WaimaiCrawler;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value = "/bi/v1/", description = "百度外卖api")
@RequestMapping("/bi/v1/")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaiduController {
    private static final Logger logger = LoggerFactory.getLogger(BaiduController.class);
    @Autowired
    private WaimaiApi waimaiApi;

    @Autowired
    private WaimaiCrawler waimaiCrawler;

    @Autowired
    private UserDao userDao;

    /**
     * 获取某一个用户名的各种信息
     *
     * @param userName
     * @return
     */
    public User getUser(String userName) {
        return userDao.ifExists(userName, Platform.BD.name());
    }

    /**
     * 获取百度所有用户
     *
     * @return
     */
    public List<User> getAllUser() {
        List<User> users = userDao.getUsersForPlatform(Platform.BD);
        return users;
    }

    @RequestMapping(value = "getOrderList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取订单信息", httpMethod = "GET", notes = "根据时间段获取订单信息")
    public Object getOrderList(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {
        if (userName.isEmpty()) {
            List<User> listUser = getAllUser();
            for (User u : listUser) {
                waimaiApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
                return "OrderList is ok";
            }
        } else {
            User u = getUser(userName);
            waimaiApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
            return "OrderList is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }

    @RequestMapping(value = "getComment", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取评论信息", httpMethod = "GET", notes = "根据时间段获取评论信息")
    public Object getComment(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {
        if (userName.isEmpty()) {
            List<User> listUser = getAllUser();
            for (User u : listUser) {
                waimaiApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
                return "Comment is ok";
            }
        } else {
            User u = getUser(userName);
            waimaiApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
            return "Comment is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }

    @RequestMapping(value = "getBaiduCarwler", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取百度外卖商户数据", httpMethod = "GET", notes = "根据时间段获取百度外卖商户数据")
    public Object getBaiduCarwler(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {

        if (userName.isEmpty()) {
            List<User> listUser = getAllUser();
            for (User u : listUser) {
                waimaiCrawler.logins(u.getUserName(), u.getPassWord(), startTime, endTime, u.getMerchantId());
                return "BaiduCarwler is ok";
            }
        } else {
            User u = getUser(userName);
            waimaiCrawler.logins(u.getUserName(), u.getPassWord(), startTime, endTime, u.getMerchantId());
            return "BaiduCarwler is ok";
        }
        String content = "没有找到 {" + userName + "} 用户的信息";
        return content;
    }
}

