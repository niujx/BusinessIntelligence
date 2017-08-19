package com.business.intelligence.model;

import lombok.Data;

/**
 * Created by yanshi on 2017/7/15.
 *
 */
//后台登录的账号认证信息
@Data
public class Authenticate {

    private String userName;
    private String password;
    private String merchantId;
}
