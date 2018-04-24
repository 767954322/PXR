package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.MyLuckyModel;
import com.diting.pingxingren.util.LogUtils;

import java.util.List;

/**
 * 我的活动
 * Created by Administrator on 2018/1/8.
 */

public class MyLuckyAdapter extends BaseQuickAdapter<MyLuckyModel, BaseViewHolder> {
    public LuckyImageListener onLuckyImageListener;
    public MyLuckyAdapter(int layoutResId, @Nullable List<MyLuckyModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final MyLuckyModel item) {
        ImageView luckuImage=holder.getView(R.id.iv_lucky);

        Glide.with(mContext).load(item.getActivityPicture()).into(luckuImage);
        luckuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLuckyImageListener.toLuckyDetail(item);
            }
        });
    }

    public void setOnLuckyImageListener(LuckyImageListener onLuckyImageListener) {
        this.onLuckyImageListener = onLuckyImageListener;
    }

    public interface  LuckyImageListener{
        void toLuckyDetail(MyLuckyModel myLuckyModel);
    }
}
