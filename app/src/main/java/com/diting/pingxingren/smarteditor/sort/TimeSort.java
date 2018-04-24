package com.diting.pingxingren.smarteditor.sort;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.util.TimeUtil;

import java.util.Comparator;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 30, 02:21.
 * Description: .
 */

public class TimeSort implements Comparator<ArticleModel.ItemsBean> {

    @Override
    public int compare(ArticleModel.ItemsBean o1, ArticleModel.ItemsBean o2) {
        long time1 = TimeUtil.string2Millis(o1.getUpdatedtime(), TimeUtil.PATTERN_YMD_HM1);
        long time2 = TimeUtil.string2Millis(o2.getUpdatedtime(), TimeUtil.PATTERN_YMD_HM1);

        if (time1 > time2) {
            return -1;
        } else if (time1 < time2) {
            return 1;
        } else {
            return 0;
        }
    }
}
