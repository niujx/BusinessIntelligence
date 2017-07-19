package com.business.intelligence.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tcqq on 2017/7/18.
 */
public class DateUtils {
    public static String date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
