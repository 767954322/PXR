package com.diting.pingxingren.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 17, 14:50.
 * Description: .
 */

public class ChildRobotAdapter extends BaseQuickAdapter<ChildRobotModel, BaseViewHolder> {

    private ClickListener mClickListener;

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ChildRobotAdapter(int layoutResId, @Nullable List<ChildRobotModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChildRobotModel item) {
        TextView nameTextView = holder.getView(R.id.text);
        CircleImageView circleImageView = holder.getView(R.id.icon);

        if (item != null) {
            nameTextView.setText(item.getName());
            nameTextView.setTextColor(Color.parseColor("#48495F"));
            if (Utils.hasJellyBean())
                nameTextView.setBackground(null);
            String robotHeadImg = item.getRobotHeadImg();
            if (Utils.isEmpty(robotHeadImg)) {
                circleImageView.setImageResource(R.mipmap.icon_left);
            } else {
                Glide.with(mContext).load(robotHeadImg).into(circleImageView);
            }
        } else {
            nameTextView.setText("创建");
            nameTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            if (Utils.hasJellyBean())
                nameTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.login_button_selector));
            circleImageView.setImageResource(R.mipmap.default_head);
        }

        holder.getView(R.id.robotLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    mClickListener.onClick(item != null ? item : "创建");
            }
        });
    }
}
