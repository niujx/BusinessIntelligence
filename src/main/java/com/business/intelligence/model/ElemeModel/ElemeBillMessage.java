package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/10/25.
 */
@Data
@Component
public class ElemeBillMessage {
    //本期订单收入
    private Double income;
    //本期订单支出
    private Double expense;
    //入账金额
    private Double payAmount;
}
