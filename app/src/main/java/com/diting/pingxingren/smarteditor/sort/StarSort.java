package com.diting.pingxingren.smarteditor.sort;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.util.StringUtil;

import java.util.Comparator;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 30, 02:21.
 * Description: .
 */

public class StarSort implements Comparator<ArticleModel.ItemsBean> {

    @Override
    public int compare(ArticleModel.ItemsBean o1, ArticleModel.ItemsBean o2) {
        String star1 = o1.getStar();
        String star2 = o2.getStar();

        if (StringUtil.isNotEmpty(star1) && StringUtil.isNotEmpty(star2)) {
            if (Integer.valueOf(star1) > Integer.valueOf(star2)) {
                return -1;
            } else {
                return 1;
            }
        } else if (StringUtil.isNotEmpty(star1) || StringUtil.isNotEmpty(star2)) {
            if (StringUtil.isNotEmpty(star1) && !StringUtil.isNotEmpty(star2)) {
                return -1;
            } else if (!StringUtil.isNotEmpty(star1) && StringUtil.isNotEmpty(star2)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
