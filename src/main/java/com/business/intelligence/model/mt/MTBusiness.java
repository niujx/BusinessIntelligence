package com.business.intelligence.model.mt;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
//营业统计
@Data
public class MTBusiness {

    //日期
    private String date;
    //营业总额
    private String total;
    //商家补贴
    private String shopSubsidy;
    //美团补贴
    private String meiTuanSubsidy;
    //预计收入
    private String estimate;
    //有效订单
    private String validateOrder;
    //无效订单
    private String invalidateOrder;

    private String id;

    private String shopName;

}
