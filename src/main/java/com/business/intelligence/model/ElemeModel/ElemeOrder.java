package com.business.intelligence.model.ElemeModel;

/**
 * Created by Tcqq on 2017/8/1.
 * 订单数据
 */

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ElemeOrder {
    //主键，订单ID
    private String orderId;
    //店铺Id
    private Integer shopId;
    //顾客送餐地址
    private String address;
    //下单时间
    private String createdAt;
    //订单生效时间
    private String activeAt;
    //配送费
    private Double deliverFee;
    //预计送达时间
    private String deliverTime;
    //订单备注
    private String description;
    //订单详细类目的列表 OGoodsGroup
    private String groups;
    //发票抬头
    private String invoice;
    //是否预订单
    private String book;
    //是否在线支付
    private String onlinePaid;
    //顾客联系电话 List<String>
    private String phoneList;
    //店铺绑定的外部ID
    private String openId;
    //店铺名称
    private String shopName;
    //店铺当日订单流水号
    private Integer daySn;
    //订单状态 OOrderStatus
    private String status;
    //退单状态 OOrderRefundStatus
    private String refundStatus;
    //下单用户的Id
    private Integer userId;
    //订单总价，用户实际支付的金额，单位：元
    private Double totalPrice;
    //订单原始价格
    private Double originalPrice;
    //订单收货人姓名
    private String consignee;
    //订单收货地址经纬度
    private String deliveryGeo;
    //送餐地址
    private String deliveryPoiAddress;
    //顾客是否需要发票
    private String invoiced;
    //店铺实收
    private Double income;
    //饿了么服务费率
    private Double serviceRate;
    //饿了么服务费
    private Double serviceFee;
    //订单中红包金额
    private Double hongbao;
    //餐盒费
    private Double packageFee;
    //订单活动总额
    private Double activityTotal;
    //店铺承担活动费用
    private Double shopPart;
    //饿了么承担活动费用
    private Double elemePart;
    //降级标识
    private String downgraded;
    //保护小号失效时间
    private String secretPhoneExpireTime;
    //订单参加活动信息  List<OActivity>
    private String orderActivities;
    //发票类型  InvoiceType
    private String invoiceType;
    //纳税人识别号
    private String taxpayerId;
    //商户表主键
    private String merchantId;


}
