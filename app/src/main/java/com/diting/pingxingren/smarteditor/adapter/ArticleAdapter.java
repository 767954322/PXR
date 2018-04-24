package com.diting.pingxingren.smarteditor.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 15:21.
 * Description: .
 */

public class ArticleAdapter extends BaseQuickAdapter<ArticleModel.ItemsBean, BaseViewHolder> {

    private ClickListener mClickListener;

    public ArticleAdapter(@Nullable List<ArticleModel.ItemsBean> data, ClickListener clickListener) {
        super(R.layout.item_article, data);
        mClickListener = clickListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ArticleModel.ItemsBean item) {
        holder.setText(R.id.tvArticleTitle, item.getTitle().trim())
                .setText(R.id.tvArticleTime, item.getUpdatedtime().split(" ")[1])
                .setText(R.id.tvArticleType, item.getEditortype().getClassification());
        String star = item.getStar();
        if (Utils.isNotEmpty(star) && star.equals("1")) {
            holder.setBackgroundRes(R.id.ivArticleStar, R.drawable.ic_star);
        } else {
            holder.setBackgroundRes(R.id.ivArticleStar, R.drawable.ic_star_n);
        }

        holder.getView(R.id.ivArticleStar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onClick(view, item, holder.getLayoutPosition());
            }
        });

        holder.getView(R.id.articleView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onClick(view, item);
            }
        });

        holder.getView(R.id.articleView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mClickListener != null) mClickListener.onClick(view, item, holder.getLayoutPosition());
                return false;
            }
        });
    }
}
