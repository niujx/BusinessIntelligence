package com.business.intelligence.model;

import com.business.intelligence.model.ElemeModel.ELMCrawlerStatus;
import com.business.intelligence.model.baidu.BDCrawlerStatus;
import com.business.intelligence.model.mt.MTCrawlerStatus;
import lombok.Data;

/**
 * Created by zjy on 17/8/11.
 */
@Data
public class CrawlerStatus {


    private BDCrawlerStatus bd;
    private BDCrawlerStatus mt;
    private BDCrawlerStatus elm;



}
