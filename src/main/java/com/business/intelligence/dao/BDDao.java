package com.business.intelligence.dao;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.model.ElemeModel.*;
import com.business.intelligence.model.baidu.*;
import eleme.openapi.sdk.api.entity.order.OOrder;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BDDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 热销菜品
     *
     * @param hotDishes
     */
    public void insertHotDishes(HotDishes hotDishes) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertHotDishes", hotDishes);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(hotDishes), e);
        }
    }

    /**
     * 已入账表
     *
     * @param bookedTable
     */
    public void insertBookedTable(BookedTable bookedTable) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertBookedTable", bookedTable);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(bookedTable), e);
        }
    }

    /**
     * 经营数据
     *
     * @param businessData
     */
    public void insertBusinessData(BusinessData businessData) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertBusinessData", businessData);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(businessData), e);
        }
    }

    /**
     * 商户提现表
     *
     * @param shopWthdrawal
     */
    public void insertShopWthdrawal(ShopWthdrawal shopWthdrawal) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertShopWthdrawal", shopWthdrawal);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(shopWthdrawal), e);
        }
    }

    /**
     * 商户评论
     *
     * @param comment
     */
    public void insertComment(Comment comment) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertComment", comment);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(comment), e);
        }
    }

    /**
     * 订单详情
     *
     * @param orderDetails
     */
    public void insertOrderDetails(OrderDetails orderDetails) {
        try {
            sqlSessionTemplate.insert("com.business.intelligence.insertOrderDetails", orderDetails);
        } catch (Exception e) {
            log.error("数据已存在{}", JSONObject.toJSONString(orderDetails), e);
        }
    }

}
