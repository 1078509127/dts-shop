package com.qiguliuxing.dts.wx.util;

import java.util.Calendar;
import java.util.Date;

public class DateCalculator {
    /**
     * 获取date的周一
     * */
    public static Date getMondayOfWeek(Date  date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToMonday = (dayOfWeek + 5) % 7;
        calendar.add(Calendar.DAY_OF_MONTH, -daysToMonday);
        return calendar.getTime();
    }
    /**
     * 获取date的周日
     * */
    public static Date getSundayOfWeek(Date  date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToSunday = (7 - dayOfWeek + 1) % 7;
        calendar.add(Calendar.DAY_OF_MONTH, daysToSunday);
        return calendar.getTime();
    }
}
