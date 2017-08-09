package com.business.intelligence.model.baidu;

import java.util.Date;

/**
 * 商户评论列表
 */
public class Comment {
    /**
     * 自增长id
     */
    private int id;
    /**
     * 评论ID
     */
    private String commentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评分
     */
    private double score;
    /**
     * 配送评分
     */
    private double serviceScore;
    /**
     * 商品评分
     */
    private double dishScore;
    /**
     * 被赞的菜品
     */
    private String recommendDishes;
    /**
     * 差评的菜品
     */
    private String badDishes;
    /**
     * 配送评价标签
     */
    private String commentLabels;
    /**
     * 回复内容
     */
    private String replyContent;
    /**
     * 评价时间
     */
    private String createTime;
    /**
     * 评价人
     */
    private String userName;
    /**
     * 第三方商户ID
     */
    private String shopId;
    /**
     * 商户名称
     */
    private String shopName;
    /**
     * 配送时长,单位分钟
     */
    private String costTime;
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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public double getDishScore() {
        return dishScore;
    }

    public void setDishScore(double dishScore) {
        this.dishScore = dishScore;
    }

    public String getRecommendDishes() {
        return recommendDishes;
    }

    public void setRecommendDishes(String recommendDishes) {
        this.recommendDishes = recommendDishes;
    }

    public String getBadDishes() {
        return badDishes;
    }

    public void setBadDishes(String badDishes) {
        this.badDishes = badDishes;
    }

    public String getCommentLabels() {
        return commentLabels;
    }

    public void setCommentLabels(String commentLabels) {
        this.commentLabels = commentLabels;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
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
        return "Comment{" +
                "id=" + id +
                ", commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", serviceScore=" + serviceScore +
                ", dishScore=" + dishScore +
                ", recommendDishes='" + recommendDishes + '\'' +
                ", badDishes='" + badDishes + '\'' +
                ", commentLabels='" + commentLabels + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userName='" + userName + '\'' +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", costTime='" + costTime + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
