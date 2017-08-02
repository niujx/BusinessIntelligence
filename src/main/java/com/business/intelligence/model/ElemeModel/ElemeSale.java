package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/21.
 * 营业统计
 */
@Data
@Component
public class ElemeSale {
    //主键，门店~日期
    private String saleId;
    //统计日期
    private String orderDate;
    //门店
    private Integer shop;
    //营业额
    private Double totalOrderAmount;
    //菜品销售
    private Double foodAmount;
    //餐盒收入
    private Double boxAmount;
    //配送收入
    private Double deliverAmount;
    //在线支付
    private Double onlinePaymentAmount;
    //货到付款
    private Double offlinePaymentAmount;
    //商户补贴
    private Double restaurantDiscount;
    //饿了么补贴
    private Double elemeDiscount;
    //有效单数
    private Integer validOrderCount;
    //平均客单价
    private Double averagePrice;
    //无效订单数
    private Integer invalidOrderCount;
    //预计损失
    private Double lossSaleAmount;


    @Override
    public String toString() {
        return "ElemeSale{" +
                "orderDate='" + orderDate + '\'' +
                ", shop='" + shop + '\'' +
                ", totalOrderAmount=" + totalOrderAmount +
                ", foodAmount=" + foodAmount +
                ", boxAmount=" + boxAmount +
                ", deliverAmount=" + deliverAmount +
                ", onlinePaymentAmount=" + onlinePaymentAmount +
                ", offlinePaymentAmount=" + offlinePaymentAmount +
                ", restaurantDiscount=" + restaurantDiscount +
                ", elemeDiscount=" + elemeDiscount +
                ", validOrderCount=" + validOrderCount +
                ", averagePrice=" + averagePrice +
                ", invalidOrderCount=" + invalidOrderCount +
                ", lossSaleAmount=" + lossSaleAmount +
                '}';
    }
}
