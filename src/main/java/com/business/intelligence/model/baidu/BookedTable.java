package com.business.intelligence.model.baidu;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 百度已入账表
 */
public class BookedTable {
    /**
     * 自增长id
     */
    private int id;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 损失承担方
     */
    private String lossBears;
    /**
     * 菜品(影响)
     */
    private double foodEffect;
    /**
     * 餐盒(影响)
     */
    private double boxesEffect;
    /**
     * 商户补贴(影响)
     */
    private double subsidieEffect;
    /**
     * 实收佣金(影响)
     */
    private double commissionEffect;
    /**
     * 配送费(影响)
     */
    private double shippEffect;
    /**
     * 补贴百度(影响)
     */
    private double bdSubsidieEffect;
    /**
     * 补贴代理商(影响)
     */
    private double subsidieAgentsEffect;
    /**
     * 用户支付(影响)
     */
    private double userPayEffect;
    /**
     * 菜品(不影响)
     */
    private double foodDonEffect;
    /**
     * 餐盒(不影响)
     */
    private double boxesDonEffect;
    /**
     * 商户补贴(不影响)
     */
    private double subsidieDonEffect;
    /**
     * 实收佣金(不影响)
     */
    private double commissionDonEffect;
    /**
     * 配送费(不影响)
     */
    private double shippDonEffect;
    /**
     * 补贴百度(不影响)
     */
    private double bdSubsidieDonEffect;
    /**
     * 补贴代理商(不影响)
     */
    private double subsidieAgentsDonEffect;
    /**
     * 用户支付(不影响)
     */
    private double userPayDonEffect;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 物流方
     */
    private String logistic;
    /**
     * 代理商
     */
    private String agent;
    /**
     * 骑士
     */
    private String knight;
    /**
     * 商户id
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
    /**
     *  订单序号
     */
    private double orderSortNumber;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 实际付款主体
     */
    private String actualPay;
    /**
     * 账务类型
     */
    private String financialType;
    /**
     * 交易流水号
     */
    private String serialNumber;
    /**
     * 业务流水号
     */
    private String businessNumbet;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 下单时间
     */
    private String orderTime;
    /**
     * 财务时间
     */
    private String financialTime;
    /**
     * 应收(大于0金额)
     */
    private double ysMax;
    /**
     * 应付(小于0金额)
     */
    private double yxMin;
    /**
     * 账户余额
     */
    private double accountBalance;
    /**
     * 备注
     */
    private String note;
    /**
     * 二级科目
     */
    private String secondarySubject;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 压款方式
     */
    private String pressure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getLossBears() {
        return lossBears;
    }

    public void setLossBears(String lossBears) {
        this.lossBears = lossBears;
    }

    public double getFoodEffect() {
        return foodEffect;
    }

    public void setFoodEffect(double foodEffect) {
        this.foodEffect = foodEffect;
    }

    public double getBoxesEffect() {
        return boxesEffect;
    }

    public void setBoxesEffect(double boxesEffect) {
        this.boxesEffect = boxesEffect;
    }

    public double getSubsidieEffect() {
        return subsidieEffect;
    }

    public void setSubsidieEffect(double subsidieEffect) {
        this.subsidieEffect = subsidieEffect;
    }

    public double getCommissionEffect() {
        return commissionEffect;
    }

    public void setCommissionEffect(double commissionEffect) {
        this.commissionEffect = commissionEffect;
    }

    public double getShippEffect() {
        return shippEffect;
    }

    public void setShippEffect(double shippEffect) {
        this.shippEffect = shippEffect;
    }

    public double getBdSubsidieEffect() {
        return bdSubsidieEffect;
    }

    public void setBdSubsidieEffect(double bdSubsidieEffect) {
        this.bdSubsidieEffect = bdSubsidieEffect;
    }

    public double getSubsidieAgentsEffect() {
        return subsidieAgentsEffect;
    }

    public void setSubsidieAgentsEffect(double subsidieAgentsEffect) {
        this.subsidieAgentsEffect = subsidieAgentsEffect;
    }

    public double getUserPayEffect() {
        return userPayEffect;
    }

    public void setUserPayEffect(double userPayEffect) {
        this.userPayEffect = userPayEffect;
    }

    public double getFoodDonEffect() {
        return foodDonEffect;
    }

    public void setFoodDonEffect(double foodDonEffect) {
        this.foodDonEffect = foodDonEffect;
    }

    public double getBoxesDonEffect() {
        return boxesDonEffect;
    }

    public void setBoxesDonEffect(double boxesDonEffect) {
        this.boxesDonEffect = boxesDonEffect;
    }

    public double getSubsidieDonEffect() {
        return subsidieDonEffect;
    }

    public void setSubsidieDonEffect(double subsidieDonEffect) {
        this.subsidieDonEffect = subsidieDonEffect;
    }

    public double getCommissionDonEffect() {
        return commissionDonEffect;
    }

    public void setCommissionDonEffect(double commissionDonEffect) {
        this.commissionDonEffect = commissionDonEffect;
    }

    public double getShippDonEffect() {
        return shippDonEffect;
    }

    public void setShippDonEffect(double shippDonEffect) {
        this.shippDonEffect = shippDonEffect;
    }

    public double getBdSubsidieDonEffect() {
        return bdSubsidieDonEffect;
    }

    public void setBdSubsidieDonEffect(double bdSubsidieDonEffect) {
        this.bdSubsidieDonEffect = bdSubsidieDonEffect;
    }

    public double getSubsidieAgentsDonEffect() {
        return subsidieAgentsDonEffect;
    }

    public void setSubsidieAgentsDonEffect(double subsidieAgentsDonEffect) {
        this.subsidieAgentsDonEffect = subsidieAgentsDonEffect;
    }

    public double getUserPayDonEffect() {
        return userPayDonEffect;
    }

    public void setUserPayDonEffect(double userPayDonEffect) {
        this.userPayDonEffect = userPayDonEffect;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLogistic() {
        return logistic;
    }

    public void setLogistic(String logistic) {
        this.logistic = logistic;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getKnight() {
        return knight;
    }

    public void setKnight(String knight) {
        this.knight = knight;
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

    public double getOrderSortNumber() {
        return orderSortNumber;
    }

    public void setOrderSortNumber(double orderSortNumber) {
        this.orderSortNumber = orderSortNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getActualPay() {
        return actualPay;
    }

    public void setActualPay(String actualPay) {
        this.actualPay = actualPay;
    }

    public String getFinancialType() {
        return financialType;
    }

    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBusinessNumbet() {
        return businessNumbet;
    }

    public void setBusinessNumbet(String businessNumbet) {
        this.businessNumbet = businessNumbet;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getFinancialTime() {
        return financialTime;
    }

    public void setFinancialTime(String financialTime) {
        this.financialTime = financialTime;
    }

    public double getYsMax() {
        return ysMax;
    }

    public void setYsMax(double ysMax) {
        this.ysMax = ysMax;
    }

    public double getYxMin() {
        return yxMin;
    }

    public void setYxMin(double yxMin) {
        this.yxMin = yxMin;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSecondarySubject() {
        return secondarySubject;
    }

    public void setSecondarySubject(String secondarySubject) {
        this.secondarySubject = secondarySubject;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "BookedTable{" +
                "id=" + id +
                ", orderType='" + orderType + '\'' +
                ", lossBears='" + lossBears + '\'' +
                ", foodEffect=" + foodEffect +
                ", boxesEffect=" + boxesEffect +
                ", subsidieEffect=" + subsidieEffect +
                ", commissionEffect=" + commissionEffect +
                ", shippEffect=" + shippEffect +
                ", bdSubsidieEffect=" + bdSubsidieEffect +
                ", subsidieAgentsEffect=" + subsidieAgentsEffect +
                ", userPayEffect=" + userPayEffect +
                ", foodDonEffect=" + foodDonEffect +
                ", boxesDonEffect=" + boxesDonEffect +
                ", subsidieDonEffect=" + subsidieDonEffect +
                ", commissionDonEffect=" + commissionDonEffect +
                ", shippDonEffect=" + shippDonEffect +
                ", bdSubsidieDonEffect=" + bdSubsidieDonEffect +
                ", subsidieAgentsDonEffect=" + subsidieAgentsDonEffect +
                ", userPayDonEffect=" + userPayDonEffect +
                ", supplier='" + supplier + '\'' +
                ", logistic='" + logistic + '\'' +
                ", agent='" + agent + '\'' +
                ", knight='" + knight + '\'' +
                ", shopId='" + shopId + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", orderSortNumber=" + orderSortNumber +
                ", orderNumber=" + orderNumber +
                ", actualPay='" + actualPay + '\'' +
                ", financialType='" + financialType + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", businessNumbet='" + businessNumbet + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", financialTime='" + financialTime + '\'' +
                ", ysMax=" + ysMax +
                ", yxMin=" + yxMin +
                ", accountBalance=" + accountBalance +
                ", note='" + note + '\'' +
                ", secondarySubject='" + secondarySubject + '\'' +
                ", businessType='" + businessType + '\'' +
                ", pressure='" + pressure + '\'' +
                '}';
    }
}
