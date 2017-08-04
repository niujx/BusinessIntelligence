package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/8/4.
 */
@Data
@Component
public class ElemeBean {
    //用户名
    private String username;
    //密码
    private String password;
    //商铺id
    private String shopId;
    //登录标识id
    private String ksId;

}
