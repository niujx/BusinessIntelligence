package com.business.intelligence.model.ElemeModel;

import lombok.Data;

/**
 * Created by Tcqq on 2017/7/24.
 */
@Data
public class ElemeFlow {
    private String date;
    private String shopName;
    private int exposureTotalCount;
    private int visitorNum;
    private int buyerNum;

    @Override
    public String toString() {
        return "ElemeFlow{" +
                "date='" + date + '\'' +
                ", shop='" + shopName + '\'' +
                ", exposureTotalCount=" + exposureTotalCount +
                ", visitorNum=" + visitorNum +
                ", buyerNum=" + buyerNum +
                '}';
    }
}
