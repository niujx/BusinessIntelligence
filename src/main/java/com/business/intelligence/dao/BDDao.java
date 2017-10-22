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
            log.info("百度热销菜品入库数据{}", JSONObject.toJSONString(hotDishes));
            sqlSessionTemplate.insert("com.business.intelligence.insertHotDishes", hotDishes);
        } catch (Exception e) {
            log.error("百度热销菜品入库数据已存在{}", JSONObject.toJSONString(hotDishes));
        }
    }

    /**
     * 已入账表
     *
     * @param bookedTable
     */
    public void insertBookedTable(BookedTable bookedTable) {
        try {
            log.info("百度现金账户流水明细入库数据{}", JSONObject.toJSONString(bookedTable));
            sqlSessionTemplate.insert("com.business.intelligence.insertBookedTable", bookedTable);
        } catch (Exception e) {
            log.error("百度现金账户流水明细入库数据已存在{}", JSONObject.toJSONString(bookedTable));
        }
    }

    /**
     * 经营数据
     *
     * @param businessData
     */
    public void insertBusinessData(BusinessData businessData) {
        try {
            log.info("百度曝光入库数据{}", JSONObject.toJSONString(businessData));
            sqlSessionTemplate.insert("com.business.intelligence.insertBusinessData", businessData);
        } catch (Exception e) {
            sqlSessionTemplate.update("com.business.intelligence.updateBusinessData",businessData);
            log.error("百度曝光入库数据已存在,进行更新{}", JSONObject.toJSONString(businessData));
        }
    }

    /**
     * 商户提现表
     *
     * @param shopWthdrawal
     */
    public void insertShopWthdrawal(ShopWthdrawal shopWthdrawal) {
        try {
            log.info("百度提现账户入库数据{}", JSONObject.toJSONString(shopWthdrawal));
            sqlSessionTemplate.insert("com.business.intelligence.insertShopWthdrawal", shopWthdrawal);
        } catch (Exception e) {
            log.error("百度提现账户入库数据已存在{}", JSONObject.toJSONString(shopWthdrawal));
        }
    }

    /**
     * 商户评论
     *
     * @param comment
     */
    public void insertComment(Comment comment) {
        try {
            log.info("百度评论入库数据{}", JSONObject.toJSONString(comment));
            sqlSessionTemplate.insert("com.business.intelligence.insertComment", comment);
        } catch (Exception e) {
            log.error("百度评论入库数据已存在{}", JSONObject.toJSONString(comment));
        }
    }

    /**
     * 订单详情
     *
     * @param orderDetails
     */
    public void insertOrderDetails(OrderDetails orderDetails) {
        try {
            log.info("百度订单详情入库数据{}", JSONObject.toJSONString(orderDetails));
            sqlSessionTemplate.insert("com.business.intelligence.insertOrderDetails", orderDetails);
        } catch (Exception e) {
            log.error("百度订单详情入库数据已存在{}", JSONObject.toJSONString(orderDetails));
        }
    }

}
