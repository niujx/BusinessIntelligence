package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/26.
 * 商店活动
 */
@Data
@Component
public class ElemeActivity {
    //主键 id~shopId~crawlerDate
    private String pri;
    //爬取的时间
    private String crawlerDate;
    //网页名称：id
    private Integer id;
    //商户ID
    private Long shopId;
    //开始日期
    private String beginDate;
    //结束日期
    private String endDate;
    //活动名称
    private String name;
    //活动状态
    private String status;
    //活动内容
    private String content;
    //报名时间
    private String createTime;
    //活动简介
    private String description;
    //是否与其他优惠共享
    private String isShare;
    //商户表主键
    private String merchantId;


}
