package com.diting.pingxingren.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Utils;

import java.util.List;


public class RobotAdapter extends BaseQuickAdapter<RobotConcern, BaseViewHolder> {



    public RobotAdapter(int resId, List<RobotConcern> list) {
        super(resId,list);
    }


    @Override
    protected void convert(final BaseViewHolder viewHolder, final RobotConcern item) {
        String welcome = item.getWelcome();
        viewHolder.setText(R.id.tv_robot_name,item.getRobotName())
                .setText(R.id.tv_fans_count, "粉丝数: " + item.getFansCount())
                .setText(R.id.tv_profile,Utils.isEmpty(welcome) ? Const.commonWelcome : welcome);
        if(!Utils.isEmpty(item.getHeadPortrait())) {
            Glide.with(mContext).load(item.getHeadPortrait()).into((ImageView) viewHolder.getView(R.id.iv_photo));
        }else {
            ((ImageView) viewHolder.getView(R.id.iv_photo)).setImageResource(R.mipmap.icon_left);
        }
        viewHolder.setVisible(R.id.ll_concern,false);
    }
}