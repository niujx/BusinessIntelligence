package com.business.intelligence.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tcqq on 2017/7/18.
 */
public class DateUtils {

    /**将时间按照yyyy-MM-dd的格式转换为String
     */
    public static String date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
