package com.business.intelligence.dao;

import com.business.intelligence.model.mt.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MTDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertOrder(MTOrder order) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertOrder", order);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertBusiness(MTBusiness business) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertBusiness", business);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertAnalysis(MTAnalysis analysis) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertAnalysis", analysis);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertSales(MTHotSales sales) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertSales", sales);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }
    public void insertComment(MTComment comment) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertComment", comment);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertBill(MTComment comment) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.mt.insertBill", comment);
        } catch (Exception e) {
            log.warn("insert error info {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
