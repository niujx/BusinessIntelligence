package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/24.
 * 排名流量
 */
@Data
@Component
public class ElemeFlow {
    //主键：门店名称~日期
    private String flowId;
    //日期
    private String crawlerDate;
    //门店名称
    private String shopName;
    //曝光率
    private Integer exposureTotalCount;
    //到点数
    private Integer visitorNum;
    //下单数
    private Integer buyerNum;

    @Override
    public String toString() {
        return "ElemeFlow{" +
                "flowId='" + flowId + '\'' +
                ", crawlerDate='" + crawlerDate + '\'' +
                ", shopName='" + shopName + '\'' +
                ", exposureTotalCount=" + exposureTotalCount +
                ", visitorNum=" + visitorNum +
                ", buyerNum=" + buyerNum +
                '}';
    }

}
