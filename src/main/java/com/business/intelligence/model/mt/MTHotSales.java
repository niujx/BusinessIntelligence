package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;

@Data
public class MTHotSales {

    private String id;
    //日期
    private Date date;
    //店铺名称
    private String name;
    //商品名称
    private String productName;
    // 销售量
    private Integer sellNum;
    //销售额
    private String sell;
    //销售量百分比
    private String percentageNum;
    //销售额百分比
    private String percentagePrice;

}
