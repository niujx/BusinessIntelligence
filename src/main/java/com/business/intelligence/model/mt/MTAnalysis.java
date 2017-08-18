package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;
//营业统计流量分析
@Data
public class MTAnalysis {

    private String id;
    //日期
    private Date date;
    //exposureNum visitNum orderNum
    //曝光数
    private Integer exposureNum;
    //访问数
    private Integer visitNum;
    //下单数
    private Integer orderNum;
    //商品名称
    private String shopName;
}
