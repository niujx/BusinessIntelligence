package com.business.intelligence.dao;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjy on 17/8/8.
 */
@Component
public class UserDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<User> getUsersForPlatform(Platform plstformName){

        switch (plstformName){
            case BD:
                return sqlSessionTemplate.selectList("com.business.intelligence.model.getUsers","BD");
            case ELM:
                return sqlSessionTemplate.selectList("com.business.intelligence.model.getUsers","ELM");
            case MT:
                return sqlSessionTemplate.selectList("com.business.intelligence.model.getUsers","MT");
        }

        return null;

    }



    public User ifExists(String userName,String platformName){

        Map sqlParam = new HashMap();
        sqlParam.put("userName",userName);
        sqlParam.put("platformName",platformName);
        return sqlSessionTemplate.selectOne("com.business.intelligence.model.getUser",sqlParam);

    }


}
