package com.business.intelligence.dao;

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
        sqlSessionTemplate.insert("com.business.intelligence.insertHotDishes", hotDishes);
    }

    /**
     * 已入账表
     *
     * @param bookedTable
     */
    public void insertBookedTable(BookedTable bookedTable) {
        sqlSessionTemplate.insert("com.business.intelligence.insertBookedTable", bookedTable);
    }

    /**
     * 经营数据
     *
     * @param businessData
     */
    public void insertBusinessData(BusinessData businessData) {
        sqlSessionTemplate.insert("com.business.intelligence.insertBusinessData", businessData);
    }

    /**
     * 商户提现表
     *
     * @param shopWthdrawal
     */
    public void insertShopWthdrawal(ShopWthdrawal shopWthdrawal) {
        sqlSessionTemplate.insert("com.business.intelligence.insertShopWthdrawal", shopWthdrawal);
    }

    /**
     * 商户评论
     *
     * @param comment
     */
    public void insertComment(Comment comment) {
        sqlSessionTemplate.insert("com.business.intelligence.insertComment", comment);
    }

    /**
     * 订单详情
     *
     * @param orderDetails
     */
    public void insertOrderDetails(OrderDetails orderDetails) {
        sqlSessionTemplate.insert("com.business.intelligence.insertOrderDetails", orderDetails);
    }

}
