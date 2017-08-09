package com.business.intelligence.model.baidu;

import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 百度热销菜品表
 */
@Data
@Component
public class HotDishes {
    /**
     * 自增长id
     */
    private int id;
    /**
     * 排序
     */
    private int sort;
    /**
     * 菜品名称
     */
    private String dishesName;
    /**
     * 销量
     */
    private int sales;
    /**
     * 销售金额
     */
    private double salesAmount;
    /**
     * 单价
     */
    private double price;
    /**
     * 销量占比
     */
    private double salesAccounted;
    /**
     * 销售额占比
     */
    private double salesNumberAccounted;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalesAccounted() {
        return salesAccounted;
    }

    public void setSalesAccounted(double salesAccounted) {
        this.salesAccounted = salesAccounted;
    }

    public double getSalesNumberAccounted() {
        return salesNumberAccounted;
    }

    public void setSalesNumberAccounted(double salesNumberAccounted) {
        this.salesNumberAccounted = salesNumberAccounted;
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
        return "HotDishes{" +
                "id=" + id +
                ", sort=" + sort +
                ", dishesName='" + dishesName + '\'' +
                ", sales=" + sales +
                ", salesAmount=" + salesAmount +
                ", price=" + price +
                ", salesAccounted=" + salesAccounted +
                ", salesNumberAccounted=" + salesNumberAccounted +
                ", shopId='" + shopId + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
