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
    //日期
    private String date;
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
                "date='" + date + '\'' +
                ", shopId=" + shopId +
                ", foodName='" + foodName + '\'' +
                ", salesAmount=" + salesAmount +
                ", salesAmountRate=" + salesAmountRate +
                ", salesVolumeRate=" + salesVolumeRate +
                ", salesVolume=" + salesVolume +
                '}';
    }
}
