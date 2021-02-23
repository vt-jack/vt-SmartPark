package com.snk.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.ObjectUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author snk
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static Calendar getCalendar(String str) throws ParseException {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
    public static String getTimeByCalendar(Calendar calendar) {
        if (ObjectUtils.isEmpty(calendar)) {
            return null;
        }
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) < 9 ? "0" : "") + (calendar.get(Calendar.MONTH) + 1) +
                "-" + (calendar.get(Calendar.DATE) < 10 ? "0" : "") + calendar.get(Calendar.DATE) + " " +
                (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + calendar.get(Calendar.HOUR_OF_DAY) +
                ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE) +
                ":" + (calendar.get(Calendar.SECOND) < 10 ? "0" : "") + calendar.get(Calendar.SECOND);
    }

    public static String getTimeByLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return df.format(localDateTime);
    }

    public static LocalDateTime getLocalDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return LocalDateTime.parse(dateTime, df);
    }

    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(date, df);
    }

    /**
     * 获取某月第一天的00:00:00
     * @param yyyyMm 2020-10
     * @return
     */
    public static String getFirstDayOfMonth(String yyyyMm){
        if (StringUtils.isBlank(yyyyMm)) return  null;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date date = strToDateNotDD(yyyyMm);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }
    /**
     * 获取某月最后一天的23:59:59
     * @param yyyyMm 2020-10
     * @return
     */
    public static String getLastDayOfMonth(String yyyyMm){
        if (StringUtils.isBlank(yyyyMm)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date date = strToDateNotDD(yyyyMm);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM(2017-02)
     * @param strDate
     * @return
     */
    public static Date strToDateNotDD(String strDate) {
        if (StringUtils.isBlank(strDate)) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM);
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        return date;
    }

}
