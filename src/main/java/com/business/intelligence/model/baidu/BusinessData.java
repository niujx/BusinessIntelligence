package com.business.intelligence.model.baidu;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 百度经营数据
 */
@Data
@Component
public class BusinessData {
    /**
     * id
     */
    private String id;
    /**
     * 时间
     */
    private String time;
    /**
     * 来访人数
     */
    private long visitingRs;
    /**
     * 来访次数
     */
    private long visitingCs;
    /**
     * 人均页面访问次数
     */
    private double visitingPer;
    /**
     * 曝光人数
     */
    private long exposureRs;
    /**
     * 曝光次数
     */
    private long exposureCs;
    /**
     * 下单量
     */
    private long orderAmount;
    /**
     * 下单转化率
     */
    private double orderConvert;
    /**
     * 店铺排名PK
     */
    private double shopRanking;
    /**
     * 店铺id
     */
    private String shopId;
    /**
     * 创建时间
     */
    private Date creatTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getVisitingRs() {
        return visitingRs;
    }

    public void setVisitingRs(long visitingRs) {
        this.visitingRs = visitingRs;
    }

    public long getVisitingCs() {
        return visitingCs;
    }

    public void setVisitingCs(long visitingCs) {
        this.visitingCs = visitingCs;
    }

    public double getVisitingPer() {
        return visitingPer;
    }

    public void setVisitingPer(double visitingPer) {
        this.visitingPer = visitingPer;
    }

    public long getExposureRs() {
        return exposureRs;
    }

    public void setExposureRs(long exposureRs) {
        this.exposureRs = exposureRs;
    }

    public long getExposureCs() {
        return exposureCs;
    }

    public void setExposureCs(long exposureCs) {
        this.exposureCs = exposureCs;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getOrderConvert() {
        return orderConvert;
    }

    public void setOrderConvert(double orderConvert) {
        this.orderConvert = orderConvert;
    }

    public Double getShopRanking() {
        return shopRanking;
    }

    public void setShopRanking(double shopRanking) {
        this.shopRanking = shopRanking;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BusinessData{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", visitingRs=" + visitingRs +
                ", visitingCs=" + visitingCs +
                ", visitingPer=" + visitingPer +
                ", exposureRs=" + exposureRs +
                ", exposureCs=" + exposureCs +
                ", orderAmount=" + orderAmount +
                ", orderConvert=" + orderConvert +
                ", shopRanking=" + shopRanking +
                ", shopId='" + shopId + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
