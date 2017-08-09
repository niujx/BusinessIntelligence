package com.business.intelligence.model.baidu;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 百度商户提现表
 */
public class ShopWthdrawal {
    /**
     * 自增长id
     */
    private int id;
    /**
     * 账单日期
     */
    private String billDate;
    /**
     * 账务时间
     */
    private String accountDate;
    /**
     * 交易流水号
     */
    private String serialNumber;
    /**
     * 转提流水号
     */
    private String turnSerialNumber;
    /**
     * 账务类型
     */
    private String accountType;
    /**
     * 金额
     */
    private double amount;
    /**
     * 账户余额
     */
    private double accountBalance;
    /**
     * 当日监察冻结金额
     */
    private double freezeAmount;
    /**
     * 监察冻结总额
     */
    private double sumFreezeAmount;
    /**
     * 收款账户
     */
    private String paymentAccount;
    /**
     * 收款户名
     */
    private String paymentName;
    /**
     * 付款状态
     */
    private String paymentStatus;
    /**
     * 备注
     */
    private String note;
    /**
     * 打款申请时间
     */
    private String applicationDate;
    /**
     * 商户ID
     */
    private String shopId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date creatTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTurnSerialNumber() {
        return turnSerialNumber;
    }

    public void setTurnSerialNumber(String turnSerialNumber) {
        this.turnSerialNumber = turnSerialNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(double freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public double getSumFreezeAmount() {
        return sumFreezeAmount;
    }

    public void setSumFreezeAmount(double sumFreezeAmount) {
        this.sumFreezeAmount = sumFreezeAmount;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "ShopWthdrawal{" +
                "id=" + id +
                ", billDate='" + billDate + '\'' +
                ", accountDate='" + accountDate + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", turnSerialNumber='" + turnSerialNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", amount=" + amount +
                ", accountBalance=" + accountBalance +
                ", freezeAmount=" + freezeAmount +
                ", sumFreezeAmount=" + sumFreezeAmount +
                ", paymentAccount='" + paymentAccount + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", note='" + note + '\'' +
                ", applicationDate='" + applicationDate + '\'' +
                ", shopId='" + shopId + '\'' +
                ", updateTime=" + updateTime +
                ", creatTime=" + creatTime +
                '}';
    }
}
