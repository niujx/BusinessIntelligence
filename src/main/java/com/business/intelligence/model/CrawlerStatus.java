package com.business.intelligence.model;

import lombok.Data;

/**
 * Created by zjy on 17/8/11.
 */
@Data
public enum  CrawlerStatus {
    ELM_CRAWLER_EVALUATE,
    ELM_CRAWLER_ACTIVITY,
    ELM_CRAWLER_BILL,
    ELM_CRAWLER_COMMODITY,
    ELM_CRAWLER_FLOW,
    ELM_CRAWLER_SALE,
    ELM_CRAWLER_ORDER
}
