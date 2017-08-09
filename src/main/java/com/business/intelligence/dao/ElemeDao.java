package com.business.intelligence.dao;

import com.business.intelligence.model.ElemeModel.*;
import eleme.openapi.sdk.api.entity.order.OOrder;
import lombok.extern.slf4j.Slf4j;
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
            log.error("{} 数据已经存在",elemeEvaluate.getId());
        }
    }

    public void insertActivity(ElemeActivity elemeActivity){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertActivity",elemeActivity);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeActivity.getId());
        }
    }

    public void insertBill(ElemeBill elemeBill){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertBill",elemeBill);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeBill.getClosingDate());
        }
    }

    public void insertCommodity(ElemeCommodity elemeCommodity){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertCommodity",elemeCommodity);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeCommodity.getMessageDate());
        }
    }

    public void insertFlow(ElemeFlow elemeFlow){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertFlow",elemeFlow);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeFlow.getFlowId());
        }
    }

    public void insertSale(ElemeSale elemeSale){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertSale",elemeSale);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeSale.getSaleId());
        }
    }

    public void insertOrder(ElemeOrder elemeOrder){
        try{
            sqlSessionTemplate.insert("com.business.intelligence.insertOrder",elemeOrder);
        }catch(Exception e){
            log.error("{} 数据已经存在",elemeOrder.getOrderId());
        }
    }





}
