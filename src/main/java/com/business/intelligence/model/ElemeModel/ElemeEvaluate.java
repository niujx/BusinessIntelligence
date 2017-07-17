package com.business.intelligence.model.ElemeModel;

import lombok.Data;

/**
 * Created by Tcqq on 2017/7/17.
 */
@Data
public class ElemeEvaluate {
    private long shopId;
    private String date;
    private String evaValue;
    private int star;
    private String goods;
    private int index;
    private String type;


    @Override
    public String toString() {
        return "ElemeEvaluate{" +
                "shopId=" + shopId +
                ", date='" + date + '\'' +
                ", evaValue='" + evaValue + '\'' +
                ", star=" + star +
                ", goods='" + goods + '\'' +
                ", index=" + index +
                ", type='" + type + '\'' +
                '}';
    }
}