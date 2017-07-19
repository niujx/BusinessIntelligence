package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.util.DateUtils;
import eleme.openapi.sdk.api.entity.order.OOrder;
import eleme.openapi.sdk.api.entity.order.OrderList;
import eleme.openapi.sdk.api.exception.ServiceException;
import eleme.openapi.sdk.api.service.OrderService;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.response.Token;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Tcww on 2017/7/18.
 */
@Component
public class ElemeOrderCrawler extends ElemeCrawler{
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    private HttpClient httpClient = super.httpClient;
    //用于登录的Token
    private Token token = super.token;
    //用于登录用的配置
    private Config config = super.config;

    private static final long SHOPID = 156716462;
    private static final int PAGENO = 1;
    private static final int PAGESIZE = 10;

    @Override
    public void doRun() {
        String date = DateUtils.date2String(crawlerDate);
        try {
            OrderList allOrders = getAllOrders(date);
            System.out.println(allOrders.getTotal());
            for(OOrder order:allOrders.getList()){
//                elemeDao.insertOrder(order);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public OrderList getAllOrders(String date) throws ServiceException {
        OrderService orderService = new OrderService(config,token);
        return orderService.getAllOrders(SHOPID,PAGENO,PAGESIZE,date);
    }



}
