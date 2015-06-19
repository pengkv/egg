package com.pengkv.egg.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具
 * <p/>
 * Created by Liam on 2014/8/23.
 */
public class DateUtil {

    /**
     * 得到当前日期String(格式：yyyy-MM-dd HH:mm:ss)
     *
     * @return 当前日期String(格式：yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentCnTimeString() {
        Date date = new Date();
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return fm.format(date);
    }

    /**
     * 按一定的格式得到当前日期String
     *
     * @param format
     * @return 当前日期String(格式：yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentTimeString(String format) {
        Date date = new Date();
        SimpleDateFormat fm = new SimpleDateFormat(format, Locale.CHINA);

        return fm.format(date);
    }

    public static String getPreviewTimeString(Date date) {
        SimpleDateFormat fm = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return fm.format(date);
    }

    /**
     * 返回相差几天,-1代表出错
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 相差几天
     */
    public static int compareDays(String beginTime, String endTime) {
        int days = -1;

        if (beginTime == null || endTime == null) {
            return days;
        }

        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            Date beginDate = f.parse(beginTime);
            Date endDate = f.parse(endTime);

            long timeLong;
            if (beginDate.getTime() > endDate.getTime()) {
                timeLong = beginDate.getTime() - endDate.getTime();
            } else {
                timeLong = endDate.getTime() - beginDate.getTime();
            }

            days = (int) timeLong / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 得到将来和现在的时间差
     *
     * @return 将来和现在的时间差
     */
    public static String getDeltaT(Date future) {
        Date now = new Date();

        long l = future.getTime() - now.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个日期型的时间相差多少时间
     */
    public static String compareTime(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong > 0 && timeLong < 60 * 1000) return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    /**
     * 是否是今天
     *
     * @param c
     * @return
     */
    public static boolean isToady(Calendar c) {
        return isTheSameDay(c, Calendar.getInstance(Locale.CHINA));
    }

    /**
     * 是否是同一天
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean isTheSameDay(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR) || c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR))
            return false;

        return true;
    }

}
