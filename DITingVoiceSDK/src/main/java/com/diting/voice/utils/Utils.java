package com.diting.voice.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 11:20.
 * Description: 工具类.
 */

public class Utils {

    /**
     * 判断对象是否为空.
     *
     * @param o 需要判空的对象.
     * @return True: 非空. False: 空.
     */
    public static boolean isNotEmpty(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            return s.length() > 0;
        } else {
            return o != null;
        }
    }

    /**
     * 判断集合是否为空.
     *
     * @param objects 需要判空的集合.
     * @return True: 非空. False: 空.
     */
    public static boolean isNotEmpty(List<?> objects) {
        return objects != null && objects.size() > 0;
    }

    //   API Level        SDK Name
    //      15              4.0.3
    //      16              4.1
    //      17              4.2
    //      18              4.3
    //      19              4.4
    //      20              4.4W
    //      21              5.0
    //      22              5.1
    //      23              6.0
    //      24              7.0
    //      25              7.1.1
    //      26              8.0

    /**
     * 判断当前SDK版本是否 >= 16.
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 判断当前SDK版本是否 >= 19.
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断当前SDK版本是否 >= 21.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 判断当前SDK版本是否 >= 26.
     */
//    public static boolean hasO() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
//    }

    /**
     * 判断是否是静音.
     */
    public static boolean hasMute(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    /**
     * 设置view背景兼容低版本.
     */
    public static void setBackGround(View view, Drawable background) {
        if (hasJellyBean()) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * 获取随机字符串 a-z,A-Z,0-9
     * @param length 字符串长度
     */
    public static String getRandomString(int length) {
        char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z' };
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(chr[random.nextInt(62)]);
        }
        String base64String;
        try {
            base64String = Base64Util.encodeString(builder.toString());
        } catch (UnsupportedEncodingException e) {
            base64String = builder.toString();
            e.printStackTrace();
        }
        return base64String;
    }

    public static String second2time(int t){
        int h = t / 60 / 60;
        int m = t / 60 % 60;
        int s = t % 60 % 60;
        String time = "";
        if (h > 9) {
            time = "" + h;
        } else {
            time = "0" + h;
        }
        if (m > 9) {
            time += ":" + m;
        } else {
            time += ":0" + m;
        }
        if (s > 9) {
            time += ":" + s;
        } else {
            time += ":0" + s;
        }
        return time;
    }
}
