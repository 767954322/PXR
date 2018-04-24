package com.diting.pingxingren.smarteditor.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.smarteditor.listener.ClickListener;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 15:21.
 * Description: .
 */

public class ArticleMonthAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private ClickListener mClickListener;

    public ArticleMonthAdapter(@Nullable List<String> data, ClickListener clickListener) {
        super(R.layout.item_article_month, data);
        mClickListener = clickListener;
    }

    @Override
    protected void convert(BaseViewHolder holder, final String item) {
        final String[] s = item.split(",");
        holder.setText(R.id.tvMonthText, s[0]);
        holder.setBackgroundRes(R.id.viewMonth, getResId(holder.getLayoutPosition() % 12));
        holder.getView(R.id.viewMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null)
                    mClickListener.onClick(view, s[1]);
            }
        });
    }

    private int getResId(int index) {
        switch (index) {
            case 0:
                return R.drawable.ic_article_month_1;
            case 1:
                return R.drawable.ic_article_month_2;
            case 2:
                return R.drawable.ic_article_month_3;
            case 3:
                return R.drawable.ic_article_month_4;
            case 4:
                return R.drawable.ic_article_month_5;
            case 5:
                return R.drawable.ic_article_month_6;
            case 6:
                return R.drawable.ic_article_month_7;
            case 7:
                return R.drawable.ic_article_month_8;
            case 8:
                return R.drawable.ic_article_month_9;
            case 9:
                return R.drawable.ic_article_month_10;
            case 10:
                return R.drawable.ic_article_month_11;
            case 11:
                return R.drawable.ic_article_month_12;
            default:
                return R.drawable.ic_article_month_1;
        }
    }
}
