package com.diting.pingxingren.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 16, 13:26.
 * Description: .
 */

public class IndustryEntity extends AbstractExpandableItem<IndustryItemEntity> implements MultiItemEntity {

    public String industry;

    public IndustryEntity(String industry) {
        this.industry = industry;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
