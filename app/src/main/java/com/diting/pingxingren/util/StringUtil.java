package com.diting.pingxingren.util;

import android.support.v4.content.ContextCompat;

import com.diting.pingxingren.app.MyApplication;

/**
 * Created by 2018 on 2018/1/5.
 * String Util.
 */

public class StringUtil {

    public static boolean isNotEmpty(String str) {
        return Utils.isNotEmpty(str);
    }

    public static String getString(int res) {
        if (res != 0) {
            return MyApplication.getInstance().getBaseContext().getString(res);
        }
        return null;
    }

    public static String getString(int res, String text) {
        if (res != 0) {
            return MyApplication.getInstance().getBaseContext().getString(res, text);
        }
        return null;
    }

    public static String[] getStringArray(int res) {
        if (res != 0) {
            return MyApplication.getInstance().getBaseContext().getResources().getStringArray(res);
        }
        return new String[]{};
    }

    public static int getColor(int res) {
        if (res != 0) {
            return ContextCompat.getColor(MyApplication.getInstance().getBaseContext(), res);
        }
        return 0;
    }
}
