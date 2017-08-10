package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/24.
 * 商品分析
 */
@Data
@Component
public class ElemeCommodity {
    //主键 date~id
    private String pri;
    //日期
    private String messageDate;
    //商铺ID
    private Long shopId;
    //商品名称
    private String foodName;
    //销售额
    private Double salesAmount;
    //销售额占比
    private String salesAmountRate;
    //销量
    private Integer salesVolume;
    //销量占比
    private String salesVolumeRate;

    @Override
    public String toString() {
        return "ElemeCommodity{" +
                "messageDate='" + messageDate + '\'' +
                ", shopId=" + shopId +
                ", foodName='" + foodName + '\'' +
                ", salesAmount=" + salesAmount +
                ", salesAmountRate=" + salesAmountRate +
                ", salesVolumeRate=" + salesVolumeRate +
                ", salesVolume=" + salesVolume +
                '}';
    }
}
