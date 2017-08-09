package com.business.intelligence.UserTest;

import com.business.intelligence.BaseTest;
import com.business.intelligence.dao.UserDao;
import com.business.intelligence.model.Platform;
import com.business.intelligence.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zjy on 17/8/8.
 */
public class UserDaoTest extends BaseTest{

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetUser(){
        List<User> list = userDao.getUsersForPlatform(Platform.BD);

        for(User user : list){
            System.out.println(user.getPlatform());
        }


    }

}
