package com.diting.pingxingren.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 16, 14:58.
 * Description: .
 */

public class Industry implements MultiItemEntity {

    public String industryItem;

    public Industry(String industryItem) {
        this.industryItem = industryItem;

    }

    @Override
    public int getItemType() {
        return 1;
    }
}
