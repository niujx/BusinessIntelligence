package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;

@Data
public class MTOrder {

    //订单编号
    private String appNo;
    //下单时间
    private Date orderTime;
    //接单时间
    private Date hourLong;
    //店铺名称
    private String name;
    //店铺ID
    private String id;
    //店铺所在城市
    private String city;
    //订单支付类型
    private String type;
    //订单状态
    private String status;
    //订单配送状态
    private String disStatus;
    //是否预定
    private String isSchedule;
    //订单总金额
    private String totalPrice;
    //订单优惠后金额
    private String postDiscount;
    //订单美团承担活动金额
    private String mtPrice;
    //订单商家承担活动金额
    private String merchantPrice;
    //菜品信息
    private String dishInfo;
    //配送费用
    private String deliveryfee;
    //是否活动订单
    private String isDiscount;
    //优惠活动
    private String preferential;
    //是否催单
    private String isPress;
    //回复状态
    private String replyStatus;
    //商店回复内容
    private String merchantReplay;
    //用户投诉时间
    private Date complaintTime;
    //用户投诉内容
    private String complaintInfo;
    //用户评价时间
    private Date appraiseTime;
    //配送时间
    private Date deliveryTime;
    //评价星级
    private String star;
    //评价内容
    private String appraiseInfo;
    //餐盒费总费用
    private String foodBoxPrice;
    //餐盒总数量
    private String foodBoxQuantity;
    //订单完成时间
    private Date orderDoneTime;
    //订单取消原因
    private String orderCancelInfo;
}
