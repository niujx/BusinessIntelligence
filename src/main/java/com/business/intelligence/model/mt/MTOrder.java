package com.business.intelligence.model.mt;

import lombok.Data;

@Data
public class MTOrder {

    private String appNo;
    private String orderTime;
    private String hourLong;
    private String name;
    private String id;
    private String city;
    private String type;
    private String status;
    private String disStatus;
    private String isSchedule;
    private String totalPrice;
    private String postDiscount;
    //订单美团承担活动金额
    private String mtPrice;
    //订单商家承担活动金额
    private String merchantPrice;
    private String dishInfo;
    private String deliveryfee;
    private String isDiscount;
    private String preferential;
    private String isPress;
    private String replyStatus;
    private String merchantReplay;
    private String complaintTime;
    private String complaintInfo;
    private String appraiseTime;
    private String deliveryTime;
    private String star;
    private String appraiseInfo;
    private String foodBoxPrice;
    private String foodBoxQuantity;
    private String orderDoneTime;
    private String orderCancelInfo;
}
