package com.business.intelligence.dao;

import com.business.intelligence.model.mt.MTOrder;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MTDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertOrder(MTOrder order) {
        sqlSessionTemplate.insert("com.business.intelligence.mt.insertOrder", order);
    }
}
