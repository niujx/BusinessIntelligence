package com.business.intelligence.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2017-10-27
 * Time: 下午2:50
 */

@Slf4j
public class CrawlerLogger {
    String name;

    public CrawlerLogger(String name) {
        this.name = name;
    }

    public void log(String msg) {
        log.info("爬虫 {} 日志信息:{}", name, msg);
    }
}
