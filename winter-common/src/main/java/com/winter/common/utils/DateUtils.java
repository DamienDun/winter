package com.winter.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author winter
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String YYYY = "yyyy";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    public static final String YYYY_MM_DD_T_HH_MM_SS_SSSZ = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            YYYY_MM_DD_T_HH_MM_SS_SSS, YYYY_MM_DD_T_HH_MM_SS_SSSZ
    };

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endTime   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endTime, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endTime.getTime() - startTime.getTime();
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

    /**
     * 计算相差秒数
     */
    public static long timeDistanceSecond(Date endDate, Date nowDate) {
        return (endDate.getTime() - nowDate.getTime()) / 1000;
    }

    /**
     * 计算两个时间差多少小时
     */
    public static Integer timeDistanceHour(Date endDate, Date nowDate) {
        long nh = 1000 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        long hour = diff / nh;
        return Integer.valueOf(String.valueOf(hour));
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param date 本地时间
     * @return 返回指定日期的 00:00:00.0000000
     */
    public static LocalDateTime getDayBeginLocalDateTime(Date date) {
        return getDayBeginLocalDateTime(toLocalDateTime(date));
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param localDateTime 本地时间
     * @return 返回指定日期的 00:00:00.0000000
     */
    public static LocalDateTime getDayBeginLocalDateTime(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(),
                0, 0, 0, 0);
    }

    /**
     * 获取指定日期的结束时间
     *
     * @param date 本地时间
     * @return 返回指定日期的 23:59:59.999999999
     */
    public static LocalDateTime getDayEndLocalDateTime(Date date) {
        return getDayEndLocalDateTime(toLocalDateTime(date));
    }

    /**
     * 获取指定日期的结束时间
     *
     * @param localDateTime 本地时间
     * @return 返回指定日期的 23:59:59.999999999
     */
    public static LocalDateTime getDayEndLocalDateTime(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(),
                23, 59, 59, 999999999);
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param date 本地时间
     * @return 返回指定日期的 00:00:00.0000000
     */
    public static Date getDayBeginDateTime(Date date) {
        return from(getDayBeginLocalDateTime(date));
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param date 本地时间
     * @return 返回指定日期的 23:59:59.999999999
     */
    public static Date getDayEndDateTime(Date date) {
        return from(getDayEndLocalDateTime(date));
    }

    /**
     * 获取指定时间的结束时间,区别于getDayEndDateTime(Date date)方法(这个方法得到的23:59.59.999,插入数据库,会自动变成第二天的凌晨)
     *
     * @param date
     * @return
     */
    public static Date getEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 转换为 LocalDateTime
     *
     * @param date 日期
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 由 LocalDateTime 转换为 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date from(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        return Date.from(localDateTime.atZone(zone).toInstant());
    }

    /**
     * 本地日期时间解析
     *
     * @param dateString 日期字符窜
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateString) {
        Date date = parseDate(dateString);
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 获取昨天
     *
     * @return
     */
    public static String getYesterdayStr() {
        return DateUtils.dateTime(getYesterday());
    }

    /**
     * 获取昨天
     *
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取本周的周一
     *
     * @param format
     * @return
     */
    public static String getMondayOfThisWeek(String format) {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 1);
        return parseDateToStr(format, c.getTime());
    }

    /**
     * 获取指定日期的周一
     *
     * @param date
     * @param format
     * @return
     */
    public static String getMondayOfWeek(Date date, String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 1);
        return parseDateToStr(format, c.getTime());
    }

    /**
     * 获取当月的第一天
     *
     * @param format
     * @return
     */
    public static String getFirstDayOfThisMonth(String format) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return parseDateToStr(format, c.getTime());
    }

    /**
     * 获取指定时间的月第一天
     *
     * @param date
     * @param format
     * @return
     */
    public static String getFirstDayOfMonth(Date date, String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return parseDateToStr(format, c.getTime());
    }

    /**
     * 获取当年的1月1号
     *
     * @param format
     * @return
     */
    public static String getFirstDayOfThisYear(String format) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return parseDateToStr(format, c.getTime());
    }
}
