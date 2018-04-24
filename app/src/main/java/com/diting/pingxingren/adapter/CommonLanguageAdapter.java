package com.diting.pingxingren.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.CommonLanguageModel;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 05, 15:29.
 * Description: .
 */

public class CommonLanguageAdapter extends BaseQuickAdapter<CommonLanguageListModel, BaseViewHolder> {

    private ItemClickListener mListener;
    private boolean mCanDelete = true;

    public void setListener(ItemClickListener listener) {
        mListener = listener;
    }

    public void setCanDelete(boolean canDelete) {
        this.mCanDelete = canDelete;
    }

    public CommonLanguageAdapter(int layoutResId) {
        super(layoutResId, MyApplication.sCommonLanguages);
    }

    public CommonLanguageAdapter(int layoutResId, List<CommonLanguageListModel> languageModels) {
        super(layoutResId, languageModels);
    }

    @Override
    protected void convert(BaseViewHolder holder, final CommonLanguageListModel item) {
        holder.setText(R.id.tv_industry, item.getQuestion())
                .setImageResource(R.id.ivArrowDown, R.drawable.delete_selector);

        holder.setVisible(R.id.ivArrowDown, mCanDelete);

        holder.getView(R.id.ivArrowDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.delete(item);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.itemClick(item);
            }
        });
    }

    public interface ItemClickListener {
        void delete(CommonLanguageListModel commonLanguage);

        void itemClick(CommonLanguageListModel commonLanguage);

        void add();
    }
}
