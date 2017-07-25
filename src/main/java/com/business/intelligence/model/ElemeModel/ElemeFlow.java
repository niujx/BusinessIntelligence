package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/24.
 */
@Data
@Component
public class ElemeFlow {
    private String date;
    private String shopName;
    private Integer exposureTotalCount;
    private Integer visitorNum;
    private Integer buyerNum;

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
