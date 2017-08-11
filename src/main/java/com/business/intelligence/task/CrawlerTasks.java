package com.business.intelligence.task;

import com.business.intelligence.crawler.eleme.ElemeCrawlerAll;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
@Component
public class CrawlerTasks {
    @Autowired
    private ElemeCrawlerAll elemeCrawlerAll;

    @Scheduled(cron = "* * 3 * * *")
    public void doRun() {
        elemeCrawlerAll.runAllCrawler();
    }
}
