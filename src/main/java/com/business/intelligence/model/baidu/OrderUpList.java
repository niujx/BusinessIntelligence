package com.business.intelligence.model.baidu;

import com.alibaba.fastjson.annotation.JSONField;

public class OrderUpList {
    private String orderId;
    private int status;
    private String createTime;
    private String wid;
    private String userPhone;
    private String orderStatus;

    public String getOrderId() {
        return orderId;
    }
    @JSONField(name = "order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }
    @JSONField(name = "create_time")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getUserPhone() {
        return userPhone;
    }
    @JSONField(name = "user_phone")
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    @JSONField(name = "order_status")
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
