package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/17.
 * 顾客评价
 */
@Data
@Component
public class ElemeEvaluate {
    private Long shopId;
    private String date;
    private String evaValue;
    private String quality;
    private String goods;


    @Override
    public String toString() {
        return "ElemeEvaluate{" +
                "shopId=" + shopId +
                ", date='" + date + '\'' +
                ", evaValue='" + evaValue + '\'' +
                ", quality=" + quality +
                ", goods='" + goods + '\'' +
                '}';
    }
}