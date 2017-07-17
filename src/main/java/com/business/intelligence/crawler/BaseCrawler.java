package com.business.intelligence.crawler;

import com.business.intelligence.crawler.core.HttpClientFactory;
import com.business.intelligence.model.Authenticate;
import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.http.HttpMethod;

import java.io.IOException;
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
