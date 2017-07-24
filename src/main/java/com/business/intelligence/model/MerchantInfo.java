package com.business.intelligence.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by yanshi on 2017/7/24.
 */
@Data
public class MerchantInfo {

    private String id;
    private String name;
    private String address;
    private String phone;
    private String mtUser;
    private String mtPassword;
    private String elmUser;
    private String elmPassword;
    private String bduser;
    private String bdPassword;
    private Date createTime;
    private String createEmp;
    private Date updateTime;
    private String updateEmp;

}
