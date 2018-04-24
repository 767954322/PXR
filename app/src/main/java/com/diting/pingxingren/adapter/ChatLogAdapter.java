package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.ChatLogItemModel;
import com.diting.pingxingren.util.MySharedPreferences;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 访客管理  查看消息
 * Created by Administrator on 2017/12/25.
 */

public class ChatLogAdapter extends BaseQuickAdapter<ChatLogItemModel, BaseViewHolder> {

    Context mContext;
    private String headerImage;

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public ChatLogAdapter(Context mContext, int layoutResId, @Nullable List<ChatLogItemModel> data) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, ChatLogItemModel item) {

        CircleImageView header_left = holder.getView(R.id.icon_left);
        TextView left_msg = holder.getView(R.id.left_msg);
        CircleImageView icon_right = holder.getView(R.id.icon_right);
        TextView right_msg = holder.getView(R.id.right_msg);


//        header_left.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_left));
//        Glide.with(mContext).load(headerImage).into(header_left);
        Glide.with(mContext).load(headerImage)
                .placeholder(R.mipmap.icon_right)
                .error(R.mipmap.icon_right)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(header_left);
        left_msg.setText(item.getQuestion());
//        if (Utils.isNotEmpty(MySharedPreferences.getHeadPortrait())) {
//            Glide.with(mContext).load(MySharedPreferences.getHeadPortrait()).into(icon_right);
//        }  else {
//            icon_right.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_left));
//        }

        Glide.with(mContext).load(MySharedPreferences.getHeadPortrait())
                .placeholder(R.mipmap.icon_right)
                .error(R.mipmap.icon_right)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(icon_right);



        right_msg.setText(item.getAnswer());
    }
}
