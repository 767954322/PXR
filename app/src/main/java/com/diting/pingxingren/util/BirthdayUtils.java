package com.diting.pingxingren.util;

import android.content.Context;

import com.diting.pingxingren.app.MyApplication;

/**
 * Created by Administrator on 2017/11/16.
 */

public class BirthdayUtils  {

    // ====== 传入 月日的offset 传回干支, 0=甲子
    public static String cyclicalm(int num) {
        final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚",
                "辛", "壬", "癸"};
        final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午",
                "未", "申", "酉", "戌", "亥"};
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    //农历的月份
    public static String monthOfAlmanac[] = {"正月", "二月", "三月", "四月", "五月", "六月",
            "七月", "八月", "九月", "十月", "冬月", "腊月"};
    //农历的日
    public static String daysOfAlmanac[] = {"初一", "初二", "初三", "初四", "初五", "初六",
            "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六",
            "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六",
            "廿七", "廿八", "廿九", "三十"}; // 农历的天数

    //阳历的月份数 12个月
    public static String monthsofGregorian[] = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

    //得到阳历的年份
    public static String[] getYeraGregorian() {
        String str[] = new String[107];

        int index1 = 1942;
        // int num = year - 1900 + 36;
        for (int i = 0; i < str.length; i++) {
            str[i] = index1 + "年";
            index1++;
        }
        return str;
    }

    //得到阳历的天数
    public static String[] getDaysofGregorians(int daysNum) {
        String days[] = new String[daysNum];
        int index1 = 1;
        for (int i = 0; i < daysNum; i++) {
            days[i] = index1 + "日";
            index1++;
        }

        return days;
    }
    /***
     * 得到天干地支
     *
     * @return
     */
    public static String[] getYera() {
        String str[] = new String[107];

        int index1 = 1942;
        // int num = year - 1900 + 36;
        for (int i = 0; i < str.length; i++) {
            str[i] = cyclicalm(index1 - 1900 + 36) + "年(" + index1 + ")";
            index1++;
        }
        return str;
    }

}
