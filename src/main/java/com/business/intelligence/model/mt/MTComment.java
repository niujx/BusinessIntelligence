package com.business.intelligence.model.mt;

import lombok.Data;

import java.util.Date;

@Data
public class MTComment {

    private String id;
    //用户名
    private String userName;
    //创建时间
    private Date createTime;
    //评论内容
    private String comment;
    //外卖列表
    private String foods;
    //分数含义
    private String scoreMeaning;
    //送达时间
    private Integer shipTime;
    //口味评价
    private Integer tasteScore;
    //配送评价
    private Integer shipScore;
    //包装评价
    private Integer packagingScore;
    //商家评分
    private Integer orderCommentScore;
    //商家回复
    private String eComment;
    //商户名称
    private String name;

}
