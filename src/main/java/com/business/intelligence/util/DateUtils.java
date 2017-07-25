package com.business.intelligence.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
     /**将时间按照yyyy-MM-dd的格式转换为String
     */
    public static String date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
     /**将毫秒值按照yyyy-MM-dd的格式转换为时间字符串
     */
    public static String long2Date(Long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(l);
        return sdf.format(date);
    }

    /**
     * @author wfk
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static final String PATTERN_YEAR_MONTH = "yyyy-MM";
    public static final String PATTERN_SHORT_YEAR_MONTH = "yyyyMM";

    private static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DEFAULT_PATTERN);
        }
    };

    private static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String PATTERN_DATETIME2 = "yyyyMMddHHmmss";
    private static final String PATTERN_YYYYMMDD = "yyyyMMdd";

    /**
     * 添加或者减小月份
     *
     * @param date
     * @param amount
     * @return
     * @author wfk
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    /**
     * 添加或者减小一天
     *
     * @param date
     * @param amount
     * @return
     * @author wfk
     */
    public static Date addDays(Date date, int amount) {
        return days(date, amount);
    }

    /**
     * 获取给定日期的月最后一天 如果是当前月就是当前天 yyyy-MM-dd
     *
     * @param date
     * @return
     * @author wfk
     */
    public static String getLastDayOfMonth(Date date) {
        return getLastDayOfMonth(date, DEFAULT_PATTERN);
    }

    /**
     * 获取给定日期的月最后一天 如果是当前月就是当前天 yyyy-MM-dd
     *
     * @param date
     * @param pattern
     * @return
     * @author wfk
     */
    public static String getLastDayOfMonth(Date date, String pattern) {
        if (formatDate(new Date(), "MM").equals(formatDate(date, "MM"))) {
            return formatDate(date, pattern);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime(), pattern);
    }

    /**
     * 获取指定时间的 当前年月 yyyyMM
     *
     * @param date
     * @return
     * @author wfk
     */
    public static String getMonthOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = calendar.get(Calendar.YEAR) + "";
        String month = calendar.get(Calendar.MONTH) + 1 + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        return year + month;
    }

    /**
     * 获取给定日期的月第一天
     *
     * @param date
     * @return
     * @author wfk
     */
    public static String getFirstDayOfMonth(Date date) {
        return getFirstDayOfMonth(date, DEFAULT_PATTERN);
    }

    /**
     * 获取给定日期的月第一天
     *
     * @param date
     * @param pattern
     * @return
     * @author wfk
     */
    public static String getFirstDayOfMonth(Date date, String pattern) {
        return formatDate(set(date, Calendar.DAY_OF_MONTH, 1), pattern);
    }

    public static Date add(Date date, int calendar_field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar_field, value);
        return calendar.getTime();
    }

    public static Date days(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, value);
        date = calendar.getTime();
        return date;
    }

    public static Date set(Date date, int calendar_field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar_field, value);
        return calendar.getTime();
    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @param date
     * @return
     * @author wfk
     */
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    /**
     * 格式化日期 如果 pattern 是空则按 yyyy-MM-dd
     *
     * @param date
     * @param pattern
     * @return
     * @author wfk
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat df = getDf(pattern);
        return df.format(date);
    }

    /**
     * @param pattern
     * @return
     * @author wfk
     */
    private static SimpleDateFormat getDf(String pattern) {
        SimpleDateFormat df = DEFAULT_DATE_FORMAT.get();
        if (!StringUtils.isBlank(pattern)) {
            df = new SimpleDateFormat(pattern);
        }
        return df;
    }

    /**
     * 获取当前日期的前几年
     *
     * @param value
     * @return
     * @author wfk
     */
    public static Date someDayAgo(int value) {
        return someDayAgo(Calendar.getInstance().getTime(), value);
    }

    /**
     * 获取给定日期的前几年
     *
     * @param date
     * @param value
     * @return
     * @author wfk
     */
    public static Date someDayAgo(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - value);
        return calendar.getTime();
    }

    /**
     * 获取当前日期的前几个月
     *
     * @param value
     * @return
     * @author wfk
     */
    public static Date someMonthAgo(int value) {
        return someMonthAgo(Calendar.getInstance().getTime(), value);
    }

    /**
     * 获取给定日期的前几个月
     *
     * @param date
     * @param value
     * @return
     * @author wfk
     */
    public static Date someMonthAgo(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - value);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        return calendar.getTime();
    }

    /**
     * 获取当前日期的前几年
     *
     * @param value
     * @return
     * @author wfk
     */
    public static Date someYearAgo(int value) {
        return someYearAgo(Calendar.getInstance().getTime(), value);
    }

    /**
     * 获取给定日期的前几年
     *
     * @param date
     * @param value
     * @return
     * @author wfk
     */
    public static Date someYearAgo(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - value);
        return calendar.getTime();
    }

    public static String nowString() {
        return formatDate(new Date());
    }

    /**
     * 获取当前时间的时分秒
     *
     * @return
     * @author wfk
     */
    public static String getTime() {
        Date date = new Date();
        DateFormat df2 = DateFormat.getDateTimeInstance();
        return df2.format(date).split(" ")[1];
    }

    /**
     * @Title: getDateTime @Description:
     * 获取当前时间，格式：PATTERN_DATETIME @param @return @return String
     */
    public static String getDateTime() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(PATTERN_DATETIME);
        String dateTime = df.format(date);
        return dateTime;
    }

    /**
     * 获取当前时间，格式:yyyyMMddHHmmss
     *
     * @return
     */
    public static String getDateTimeStr() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(PATTERN_DATETIME2);
        String dateTime = df.format(date);
        return dateTime;
    }

    /**
     * 获取当前时间，格式:yyyyMMdd
     *
     * @return
     */
    public static String getDateStr() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(PATTERN_YYYYMMDD);
        String dateTime = df.format(date);
        return dateTime;
    }

    public static Date parseDate(String dateString, String pattern) {
        try {
            return getDf(pattern).parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateForMills(long time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());

    }
}
