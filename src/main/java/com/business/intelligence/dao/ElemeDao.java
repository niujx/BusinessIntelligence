package com.business.intelligence.dao;

import com.business.intelligence.model.ElemeModel.ElemeActivity;
import com.business.intelligence.model.ElemeModel.ElemeBill;
import com.business.intelligence.model.ElemeModel.ElemeCommodity;
import com.business.intelligence.model.ElemeModel.ElemeEvaluate;
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
        sqlSessionTemplate.insert("com.business.intelligence.insertEvaluate",elemeEvaluate);
    }
    public void insertActivity(ElemeActivity elemeActivity){
        sqlSessionTemplate.insert("com.business.intelligence.insertActivity",elemeActivity);
    }
    public void insertBill(ElemeBill elemeBill){
        sqlSessionTemplate.insert("com.business.intelligence.insertBill",elemeBill);
    }
    public void insertCommodity(ElemeCommodity elemeCommodity){
        sqlSessionTemplate.insert("com.business.intelligence.insertCommodity",elemeCommodity);
    }



    public void insertOrder(OOrder order){
        sqlSessionTemplate.insert("com.business.intelligence.insertOrder",order);
    }





}
