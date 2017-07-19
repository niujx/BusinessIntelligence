package com.business.intelligence.crawler;

import com.business.intelligence.crawler.core.HttpClientFactory;
import com.business.intelligence.model.Authenticate;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.HttpClient;

import java.util.Date;

/**
 * Created by yanshi on 2017/7/15.
 */
public abstract class BaseCrawler implements ICrawler {

    //默认抓取前一天的数据
    @Setter
    protected Date crawlerDate = DateUtils.addDays(new Date(), -1);
    @Setter
    protected Authenticate authenticate;

    protected HttpClient httpClient = HttpClientFactory.create();



}
