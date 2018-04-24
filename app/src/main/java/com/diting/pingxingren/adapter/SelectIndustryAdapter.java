package com.diting.pingxingren.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.IndustryEntity;
import com.diting.pingxingren.entity.IndustryItemEntity;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 16, 13:24.
 * Description: .
 */

public class SelectIndustryAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private IndustryItemClick mItemClick;

    public void setItemClick(IndustryItemClick itemClick) {
        mItemClick = itemClick;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SelectIndustryAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(0, R.layout.item_industrys);
        addItemType(1, R.layout.item_industry);
    }

    @Override
    protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case 0:
                final IndustryEntity industryEntity = (IndustryEntity) item;
                holder.setText(R.id.tv_industry, industryEntity.industry)
                        .setImageResource(R.id.ivArrowDown, !industryEntity.isExpanded() ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (industryEntity.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case 1:
                final IndustryItemEntity itemEntity = (IndustryItemEntity) item;
                holder.setText(R.id.tv_industry, itemEntity.industryItem);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClick.onItemClick(itemEntity.industryItem);
                    }
                });
                break;
        }
    }

    public interface IndustryItemClick {
        void onItemClick(String industry);
    }
}
