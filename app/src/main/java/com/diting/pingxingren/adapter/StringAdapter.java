package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.smarteditor.listener.ClickListener;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 18, 14:29.
 * Description: .
 */

public class StringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private float mTextSize = -1;
    private ClickListener mClickListener;
    private int mGravity = Gravity.START | Gravity.CENTER_VERTICAL;

    public StringAdapter(@Nullable List<String> data) {
        super(R.layout.item_string, data);
    }

    public StringAdapter(@Nullable List<String> data, float textSize) {
        super(R.layout.item_string, data);
        mTextSize = textSize;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final String item) {
        TextView textView = holder.getView(R.id.string);
        textView.setText(item);
        textView.setGravity(mGravity);
        if (mTextSize != -1)
            textView.setTextSize(mTextSize);
        if (mClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClick(view, item, holder.getLayoutPosition());
                }
            });
        }
    }
}
