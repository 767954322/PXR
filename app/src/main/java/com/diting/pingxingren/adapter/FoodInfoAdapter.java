package com.diting.pingxingren.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.FoodInfo;
import com.diting.pingxingren.util.Utils;

import java.util.List;


public class FoodInfoAdapter extends BaseQuickAdapter<FoodInfo, BaseViewHolder> {

    public FoodInfoAdapter(int resId, List<FoodInfo> list) {
        super(resId,list);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final FoodInfo item) {
        viewHolder.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_distance, "距您" + (float)Math.round(item.getDistance()/100)/10+"km");
        if(!Utils.isEmpty(item.getPhotos())) {
            Glide.with(mContext).load(item.getPhotos()).into((ImageView) viewHolder.getView(R.id.iv_photo));
        }
    }
}