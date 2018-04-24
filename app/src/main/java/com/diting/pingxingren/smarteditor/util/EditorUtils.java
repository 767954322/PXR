package com.diting.pingxingren.smarteditor.util;

import com.diting.pingxingren.util.StringUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 06, 13:07.
 * Description: .
 */

public class EditorUtils {

    public static String getArticleCategory(String categoryText) {
        if (!StringUtil.isNotEmpty(categoryText)) {
            return "1";
        }

        switch (categoryText) {
            case "我的工作":
                return "1";
            case "我的生活":
                return "2";
            case "我的商务":
                return "3";
            case "我的日记":
                return "4";
            default:
                return "1";
        }
    }
}
