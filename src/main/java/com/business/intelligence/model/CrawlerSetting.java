package com.business.intelligence.model;

import lombok.Data;

/**
 * Created by yanshi on 2017/7/15.
 * 爬虫配置信息保存位置
 */
@Data
public class CrawlerSetting {

    private String crawlerDate;
    private String userName;
    private String password;
    private String crawlerType;
}
