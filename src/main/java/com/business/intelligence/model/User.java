package com.business.intelligence.model;

import lombok.Data;

/**
 * Created by zjy on 17/8/8.
 */
@Data
public class User {


    /**
     *商家id
     */
    String merchantId;
    /**
     * 用户名
     */
    String userName;
    /**
     * 密码
     */
    String passWord;
    /**
     * 是否启动
     */
    String isLaunch;
    /**
     * 平台
     */
    String platform;

    /**
     * 更新时间
     */
    String updateTime;
    /**
     * 商家名称
     */
    String merchantName;
    /**
     * 商家地址
     */
    String merchantAddress;
    /**
     * 商家电话
     */
    String phone;

    /**
     * 对接方账号
     */
    String source;
    /**
     * 秘钥
     */
    String secret;
    /**
     * 第三方商户ID
     */
    String shopId;

    /**
     * 爬取类型
     */
    String type;

}
