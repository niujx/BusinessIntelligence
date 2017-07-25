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
    private String date;
    private String shop;
    private Double totalOrderAmount;
    private Double foodAmount;
    private Double boxAmount;
    private Double deliverAmount;
    private Double onlinePaymentAmount;
    private Double offlinePaymentAmount;
    private Double restaurantDiscount;
    private Double elemeDiscount;
    private Double validOrderCount;
    private Double averagePrice;
    private Double invalidOrderCount;
    private Double lossSaleAmount;

    @Override
    public String toString() {
        return "ElemeSale{" +
                "date='" + date + '\'' +
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
