package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.HomeAppItem;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 17, 14:50.
 * Description: .
 */

public class HomeAppAdapter extends BaseQuickAdapter<HomeAppItem, BaseViewHolder> {

    public HomeAppAdapter(int layoutResId, @Nullable List<HomeAppItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeAppItem item) {
        helper.setText(R.id.text, item.getTitle());
        helper.setImageResource(R.id.icon, item.getImageRes());
        helper.setVisible(R.id.coding, item.isCoding());
    }
}
