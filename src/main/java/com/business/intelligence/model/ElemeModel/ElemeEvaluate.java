package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/17.
 * 顾客评价
 */
@Data
@Component
public class ElemeEvaluate {
    //主键，网页名称：ratingId
    private Long id;
    //商户ID
    private Long shopId;
    //爬取时间
    private String crawlerDate;
    //评价内容
    private String evaValue;
    //星级或者是评价
    private String quality;
    //商品名称
    private String goods;
    //商户表主键
    private String merchantId;

}