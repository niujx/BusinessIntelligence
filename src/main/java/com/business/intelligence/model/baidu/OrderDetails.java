package com.business.intelligence.model.baidu;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 百度订单详情表
 */
public class OrderDetails {
    /**
     * 自增长id
     */
    private String id;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 是否立即送餐，1 是 2 否
     */
    private String sendImmediately;
    /**
     * 订单当日流水号
     */
    private int orderIndex;
    /**
     * 订单状态
     */
    private int status;
    /**
     * 送达时间类型 1定时达 2限时达（错峰配送）
     */
    private int expectTimeMode;
    /**
     * 期望送达时间
     */
    private String sendTime;
    /**
     * 取餐时间
     */
    private String pickupTime;
    /**
     * 到店时间
     */
    private String atshopTime;
    /**
     * 送餐时间
     */
    private String deliveryTime;
    /**
     * 骑士手机号
     */
    private String deliveryPhone;
    /**
     * 完成时间
     */
    private String finishedTime;
    /**
     * 确认时间
     */
    private String confirmTime;
    /**
     * 取消时间
     */
    private String cancelTime;
    /**
     * 配送费，单位：分
     */
    private int sendFee;
    /**
     * 餐盒费，单位：分
     */
    private int packageFee;
    /**
     * 优惠总金额，单位：分
     */
    private int discountFee;
    /**
     * 商户应收金额（百度物流），单位：分（自配送为用户实付）
     */
    private int shopFee;
    /**
     * 订单总金额，单位：分
     */
    private int totalFee;
    /**
     * 用户实付金额，单位：分
     */
    private int userFee;
    /**
     * 付款类型 1 下线 2 在线
     */
    private String payType;
    /**
     * 是否需要发票 1 是 2 否
     */
    private String needInvoice;
    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 纳税人识别号
     */
    private String taxerId;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 物流类型 1 百度 2 自配送
     */
    private String deliveryParty;
    /**
     * 创建时间(baidu)
     */
    private String createTime;
    /**
     * 餐具数量
     */
    private String mealNum;
    /**
     * 取消订单责任承担方
     */
    private String responsibleParty;
    /**
     * 佣金，单位：分
     */
    private String commission;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 客户所在省份
     */
    private String province;
    /**
     * 客户所在城市
     */
    private String city;
    /**
     * 客户所在区
     */
    private String district;
    /**
     * 客户电话
     */
    private String phone;
    /**
     * 顾客性别 1,男 2 女
     */
    private int gender;
    /**
     * 客户地址
     */
    private String address;
    /**
     * 送餐地址百度经度
     */
    private String longitude;
    /**
     * 送餐地址百度经度
     */
    private String latitude;
    /**
     * 商户ID
     */
    private String shopId;
    /**
     * 百度商户ID
     */
    private String baiduShopId;
    /**
     * 百度商户名称
     */
    private String baiduName;
//    /**
//     * 订单客户信息
//     */
//    private String userInfo;
//    /**
//     * 客户经纬度
//     */
//    private String coord;
//    /**
//     * 商户信息
//     */
//    private String shop;
    /**
     * 订单商品信息数组，数组内第一个层数组为分袋，第二层数组为商品信息
     */
    private String products;
    /**
     * 优惠信息
     */
    private String discount;
    /**
     * 部分退款
     */
    private String partEefundInfo;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendImmediately() {
        return sendImmediately;
    }

    public void setSendImmediately(String sendImmediately) {
        this.sendImmediately = sendImmediately;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExpectTimeMode() {
        return expectTimeMode;
    }

    public void setExpectTimeMode(int expectTimeMode) {
        this.expectTimeMode = expectTimeMode;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getAtshopTime() {
        return atshopTime;
    }

    public void setAtshopTime(String atshopTime) {
        this.atshopTime = atshopTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public int getSendFee() {
        return sendFee;
    }

    public void setSendFee(int sendFee) {
        this.sendFee = sendFee;
    }

    public int getPackageFee() {
        return packageFee;
    }

    public void setPackageFee(int packageFee) {
        this.packageFee = packageFee;
    }

    public int getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(int discountFee) {
        this.discountFee = discountFee;
    }

    public int getShopFee() {
        return shopFee;
    }

    public void setShopFee(int shopFee) {
        this.shopFee = shopFee;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getUserFee() {
        return userFee;
    }

    public void setUserFee(int userFee) {
        this.userFee = userFee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getNeedInvoice() {
        return needInvoice;
    }

    public void setNeedInvoice(String needInvoice) {
        this.needInvoice = needInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getTaxerId() {
        return taxerId;
    }

    public void setTaxerId(String taxerId) {
        this.taxerId = taxerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeliveryParty() {
        return deliveryParty;
    }

    public void setDeliveryParty(String deliveryParty) {
        this.deliveryParty = deliveryParty;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMealNum() {
        return mealNum;
    }

    public void setMealNum(String mealNum) {
        this.mealNum = mealNum;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBaiduShopId() {
        return baiduShopId;
    }

    public void setBaiduShopId(String baiduShopId) {
        this.baiduShopId = baiduShopId;
    }

    public String getBaiduName() {
        return baiduName;
    }

    public void setBaiduName(String baiduName) {
        this.baiduName = baiduName;
    }

//    public String getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(String userInfo) {
//        this.userInfo = userInfo;
//    }
//
//    public String getCoord() {
//        return coord;
//    }
//
//    public void setCoord(String coord) {
//        this.coord = coord;
//    }
//
//    public String getShop() {
//        return shop;
//    }
//
//    public void setShop(String shop) {
//        this.shop = shop;
//    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPartEefundInfo() {
        return partEefundInfo;
    }

    public void setPartEefundInfo(String partEefundInfo) {
        this.partEefundInfo = partEefundInfo;
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
        return "OrderDetails{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", sendImmediately='" + sendImmediately + '\'' +
                ", orderIndex=" + orderIndex +
                ", status=" + status +
                ", expectTimeMode=" + expectTimeMode +
                ", sendTime='" + sendTime + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", atshopTime='" + atshopTime + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", deliveryPhone='" + deliveryPhone + '\'' +
                ", finishedTime='" + finishedTime + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", cancelTime='" + cancelTime + '\'' +
                ", sendFee=" + sendFee +
                ", packageFee=" + packageFee +
                ", discountFee=" + discountFee +
                ", shopFee=" + shopFee +
                ", totalFee=" + totalFee +
                ", userFee=" + userFee +
                ", payType='" + payType + '\'' +
                ", needInvoice='" + needInvoice + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", taxerId='" + taxerId + '\'' +
                ", remark='" + remark + '\'' +
                ", deliveryParty='" + deliveryParty + '\'' +
                ", createTime='" + createTime + '\'' +
                ", mealNum='" + mealNum + '\'' +
                ", responsibleParty='" + responsibleParty + '\'' +
                ", commission='" + commission + '\'' +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", shopId='" + shopId + '\'' +
                ", baiduShopId='" + baiduShopId + '\'' +
                ", baiduName='" + baiduName + '\'' +
                ", products='" + products + '\'' +
                ", discount='" + discount + '\'' +
                ", partEefundInfo='" + partEefundInfo + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
