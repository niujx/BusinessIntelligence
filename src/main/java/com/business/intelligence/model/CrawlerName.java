package com.business.intelligence.model;

import lombok.Data;

/**
 * Created by zjy on 17/8/11.
 */


public enum CrawlerName {

    ELM_CRAWLER_EVALUATE,  //抓取全部评论状态
    ELM_CRAWLER_ACTIVITY,  //抓取全部活动状态
    ELM_CRAWLER_BILL,  //爬取全部账单状态
    ELM_CRAWLER_COMMODITY,  //爬取全部商品状态
    ELM_CRAWLER_FLOW,  //爬取流量统计状态
    ELM_CRAWLER_SALE,  //爬取营业统计状态
    ELM_CRAWLER_ORDER   //爬取所有订单状态
}
