package com.diting.pingxingren.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.ExternalOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class IndustryAdapter extends BaseQuickAdapter<ExternalOptions,BaseViewHolder> {


    public IndustryAdapter(@LayoutRes int layoutResId, @Nullable List<ExternalOptions> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ExternalOptions item) {
        holder.setText(R.id.tv_industry,item.getAppName());
        if(!item.isChoose()){
            holder.setBackgroundRes(R.id.tv_industry,R.drawable.application_btn_disable);
            //holder.setTextColor(R.id.tv_industry,R.color.color_text_title);
            holder.setTextColor(R.id.tv_industry,mContext.getResources().getColor(R.color.color_text_title));
        }else {
            holder.setBackgroundRes(R.id.tv_industry,R.drawable.application_btn_enable);
            holder.setTextColor(R.id.tv_industry,mContext.getResources().getColor(R.color.colorWhite));
        }
    }
}
