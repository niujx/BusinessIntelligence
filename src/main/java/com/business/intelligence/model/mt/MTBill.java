package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;

@Data
public class MTBill {

    private String id;
    //名称
    private String name;
    //交易类型
    private String tradeType;
    //交易描述
    private String desc;
    //支付方式
    private String payType;
    //订单序列号
    private String appSeq;
    //订单号
    private String appNo;
    //下单时间
    private Date orderCreateTime;
    //完成时间
    private Date doneTime;
    //退款时间
    private Date refundTime;
    //订单状态
    private String orderStatus;
    //配送方式
    private String shipType;
    //配送状态
    private String shipStatus;
    //结算状态
    private String settleStatus;
    //账单日
    private Date payDay;
    //归属账期
    private Date vestingDay;
    //商家应收
    private String shopPrice;
    //商品总价
    private String totalPrice;
    //商家活动支出
    private String promotionPrice;
    //美团活动补贴
    private String meiTuanSubsidy;
    //平台服务费
    private String serviceCharge;
    //用户支付配送费
    private String shipPrice;
    //用户线上支付金额
    private String onlinePrice;
    //用户线下支付金额
    private String offlnePirce;
    //费率
    private String rate;
    //保底
    private String guarantees;
    //分成折扣
    private String discount;

    private String merchantId;


}
