package com.business.intelligence.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
public class CrawlerTasks {

    @Scheduled(cron = "* * 3 * *")
    public void doRun() {
        log.info("test.......");
    }
}
