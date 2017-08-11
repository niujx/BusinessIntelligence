package com.business.intelligence.dao;

import com.business.intelligence.model.CrawlerName;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zjy on 17/8/11.
 */
public class CrawlerStatus {


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int updateStatus(CrawlerName name){

        switch (name){
            case ELM_CRAWLER_ACTIVITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_ACTIVITY");
            case ELM_CRAWLER_BILL:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_BILL");
            case ELM_CRAWLER_COMMODITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_COMMODITY");
            case ELM_CRAWLER_EVALUATE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_EVALUATE");
            case ELM_CRAWLER_FLOW:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_FLOW");
            case ELM_CRAWLER_ORDER:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_ORDER");
            case ELM_CRAWLER_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatus","ELM_CRAWLER_SALE");

        }

        return 0;
    }


    public String getStatus(CrawlerName name){

        switch (name){
            case ELM_CRAWLER_ACTIVITY:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_ACTIVITY");
            case ELM_CRAWLER_BILL:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_BILL");
            case ELM_CRAWLER_COMMODITY:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_COMMODITY");
            case ELM_CRAWLER_EVALUATE:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_EVALUATE");
            case ELM_CRAWLER_FLOW:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_FLOW");
            case ELM_CRAWLER_ORDER:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_ORDER");
            case ELM_CRAWLER_SALE:
                return sqlSessionTemplate.selectOne("com.business.intelligence.model.getStatus","ELM_CRAWLER_SALE");

        }

        return null;
    }

}
