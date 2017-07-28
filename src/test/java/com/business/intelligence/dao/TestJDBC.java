package com.business.intelligence.dao;

import com.business.intelligence.BaseTest;
import com.business.intelligence.model.MerchantInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yanshi on 2017/7/15.
 */
@Slf4j
public class TestJDBC extends BaseTest {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Test
    public void testJDBC() {
        MerchantInfo merchantInfo = sqlSessionTemplate.selectOne("com.business.intelligence.model.MerchantInfo.selectById", "95133ec4-c04a-474c-8d94-750cb3c2c039");
        Assert.assertNotNull(merchantInfo);
        Assert.assertEquals(merchantInfo.getName(),"兄鸡店面");
    }


}
