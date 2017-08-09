package com.business.intelligence.dao;

import com.business.intelligence.model.ElemeModel.*;
import eleme.openapi.sdk.api.entity.order.OOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/17.
 */

@Slf4j
@Component
public class ElemeDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertEvaluate(ElemeEvaluate elemeEvaluate){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertEvaluate",elemeEvaluate);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertActivity(ElemeActivity elemeActivity){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertActivity",elemeActivity);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertBill(ElemeBill elemeBill){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertBill",elemeBill);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertCommodity(ElemeCommodity elemeCommodity){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertCommodity",elemeCommodity);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertFlow(ElemeFlow elemeFlow){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertFlow",elemeFlow);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertSale(ElemeSale elemeSale){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertSale",elemeSale);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public void insertOrder(ElemeOrder elemeOrder){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertOrder",elemeOrder);
        }catch(Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }





}
