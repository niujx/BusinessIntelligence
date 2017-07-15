package com.business.intelligence.dao;

import com.business.intelligence.BaseTest;
import com.business.intelligence.model.TUser;
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
        TUser user = sqlSessionTemplate.selectOne("com.business.intelligence.model.TUser.selectUserById", 1);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUserName(), "yanshi");
    }


}
