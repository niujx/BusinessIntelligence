package com.business.intelligence.dao;

import com.business.intelligence.model.CrawlerName;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by zjy on 17/8/11.
 */
@Component
public class CrawlerStatusDao {


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int updateStatusING(CrawlerName name){

        switch (name){
            case ELM_CRAWLER_ACTIVITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_ACTIVITY");
            case ELM_CRAWLER_BILL:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_BILL");
            case ELM_CRAWLER_COMMODITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_COMMODITY");
            case ELM_CRAWLER_EVALUATE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_EVALUATE");
            case ELM_CRAWLER_FLOW:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_FLOW");
            case ELM_CRAWLER_ORDER:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_ORDER");
            case ELM_CRAWLER_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","ELM_CRAWLER_SALE");
            case BD_COMMENT:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","BD_COMMENT");
            case BD_CRAWLER:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","BD_CRAWLER");
            case BD_ORDERDETAILS:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","BD_ORDERDETAILS");
            case MT_CRAWLER_EVALUATE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_CRAWLER_EVALUATE");
            case MT_CRAWLER_FLOW:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_CRAWLER_FLOW");
            case MT_CRAWLER_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_CRAWLER_SALE");
            case MT_GOODS_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_GOODS_SALE");
            case MT_ORDER_CHECKING:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_ORDER_CHECKING");
            case MT_REPORT_FORMS:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_REPORT_FORMS");
            case MT_SALE_ACTIVITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusING","MT_SALE_ACTIVITY");
        }

        return 0;
    }


    public int updateStatusFinal(CrawlerName name){

        switch (name){
            case ELM_CRAWLER_ACTIVITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_ACTIVITY");
            case ELM_CRAWLER_BILL:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_BILL");
            case ELM_CRAWLER_COMMODITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_COMMODITY");
            case ELM_CRAWLER_EVALUATE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_EVALUATE");
            case ELM_CRAWLER_FLOW:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_FLOW");
            case ELM_CRAWLER_ORDER:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_ORDER");
            case ELM_CRAWLER_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","ELM_CRAWLER_SALE");
            case BD_COMMENT:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","BD_COMMENT");
            case BD_CRAWLER:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","BD_CRAWLER");
            case BD_ORDERDETAILS:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","BD_ORDERDETAILS");
            case MT_CRAWLER_EVALUATE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_CRAWLER_EVALUATE");
            case MT_CRAWLER_FLOW:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_CRAWLER_FLOW");
            case MT_CRAWLER_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_CRAWLER_SALE");
            case MT_GOODS_SALE:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_GOODS_SALE");
            case MT_ORDER_CHECKING:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_ORDER_CHECKING");
            case MT_REPORT_FORMS:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_REPORT_FORMS");
            case MT_SALE_ACTIVITY:
                return sqlSessionTemplate.update("com.business.intelligence.model.updateStatusFinal","MT_SALE_ACTIVITY");

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
