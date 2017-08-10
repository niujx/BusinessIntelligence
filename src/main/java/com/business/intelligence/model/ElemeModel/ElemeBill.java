package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by Tcqq on 2017/7/25.
 * 账单记录
 */
@Data
@Component
public class ElemeBill {
    //主键，日期~id
    private  String pri;
    //账单日期
    private String closingDate;
    //本期订单收入
    private String income;
    //本期订单支出
    private String expense;
    //本期抵扣欠款
    private String deductAmount;
    //入账金额
    private String dueAmount;
    //剩余欠款
    private String payAmount;
    //入账日期
    private String paymentDate;
    //商铺ID
    private Long shopId;


}
