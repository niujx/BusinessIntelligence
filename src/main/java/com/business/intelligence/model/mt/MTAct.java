package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;

@Data
public class MTAct {

    private String id;
    //店铺名称
    private String name;
    //活动开始时间
    private Date startTime;
    //活动结束时间
    private Date endTime;
    //活动类型
    private String type;
    //活动名称
    private String actName;
    //是否结束
    private  Integer isEnd;
    //活动内容
    private String context;

    private String merchantId;


}
