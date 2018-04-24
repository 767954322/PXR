package com.diting.pingxingren.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 16, 14:58.
 * Description: .
 */

public class IndustryItemEntity extends AbstractExpandableItem<Industry> implements MultiItemEntity {

    public String industryItem;

    public IndustryItemEntity(String industryItem) {
        this.industryItem = industryItem;

    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
