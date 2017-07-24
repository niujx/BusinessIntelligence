package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/21.
 */
@Data
@Component
public class ElemeSale {
    private String date;
    private String shop;
    private double totalOrderAmount;
    private double foodAmount;
    private double boxAmount;
    private double deliverAmount;
    private double onlinePaymentAmount;
    private double offlinePaymentAmount;
    private double restaurantDiscount;
    private double elemeDiscount;
    private double validOrderCount;
    private double averagePrice;
    private double invalidOrderCount;
    private double lossSaleAmount;

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
