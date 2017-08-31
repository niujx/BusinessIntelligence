package com.business.intelligence.controller;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.crawler.baidu.WaimaiApi;
import com.business.intelligence.crawler.baidu.WaimaiCrawler;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
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
        logger.info("获取全部百度用户成功,共计 " + users.size() + " 个用户，用户信息： " + JSONObject.toJSONString(users));
        return users;
    }

    @RequestMapping(value = "getOrderList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取订单信息", httpMethod = "GET", notes = "根据时间段获取订单信息")
    public Object getOrderList(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {
        String content = "没有找到 {" + userName + "} 用户的信息";
        if (!StringUtils.isNotEmpty(userName)) {
            List<User> listUser = getAllUser();
            int index = listUser.size();
            for (User u : listUser) {
                try {
                    logger.info("当前查询用户{}", JSONObject.toJSONString(u));
                    logger.info("排队待查询用户数：" + index-- + " ," + JSONObject.toJSONString(listUser));
                    waimaiApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
                    content = "BaiduOrder is ok";
                } catch (Exception e) {
                    logger.error("百度订单获取异常", e);
                }
            }
        } else {
            User u = getUser(userName);
            logger.info("当前查询用户{}", JSONObject.toJSONString(u));
            waimaiApi.ouderListGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
            return "OrderList is ok";
        }
        return content;
    }

    @RequestMapping(value = "getComment", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取评论信息", httpMethod = "GET", notes = "根据时间段获取评论信息")
    public Object getComment(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {
        String content = "没有找到 {" + userName + "} 用户的信息";
        if (!StringUtils.isNotEmpty(userName)) {
            List<User> listUser = getAllUser();
            int index = listUser.size();
            for (User u : listUser) {
                try {
                    logger.info("当前查询用户{}", JSONObject.toJSONString(u));
                    logger.info("排队待查询用户数：" + index-- + " ," + JSONObject.toJSONString(listUser));
                    waimaiApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
                    content = "BaiduComment is ok";
                } catch (Exception e) {
                    logger.error("百度评论获取异常", e);
                }
            }
        } else {
            User u = getUser(userName);
            logger.info("当前查询用户{}", JSONObject.toJSONString(u));
            waimaiApi.commentGet(u.getSource(), u.getSecret(), u.getShopId(), u.getMerchantId(), startTime, endTime);
            return "Comment is ok";
        }

        return content;
    }

    @RequestMapping(value = "getBaiduCarwler", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ApiOperation(value = "根据时间段获取百度外卖商户数据", httpMethod = "GET", notes = "根据时间段获取百度外卖商户数据")
    public Object getBaiduCarwler(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String userName) {
        String content = "没有找到 {" + userName + "} 用户的信息";
        if (!StringUtils.isNotEmpty(userName)) {
            List<User> listUser = getAllUser();
            int index = listUser.size();
            for (User u : listUser) {
                logger.info("当前查询用户{}", JSONObject.toJSONString(u));
                logger.info("排队待查询用户数：" + index--);
                waimaiCrawler.logins(u.getUserName(), u.getPassWord(), startTime, endTime, u.getMerchantId());
                content = "BaiduCarwler is ok";
            }
        } else {
            User u = getUser(userName);
            logger.info("当前查询用户{}", JSONObject.toJSONString(u));
            waimaiCrawler.logins(u.getUserName(), u.getPassWord(), startTime, endTime, u.getMerchantId());
            return "BaiduCarwler is ok";
        }

        return content;
    }

}

