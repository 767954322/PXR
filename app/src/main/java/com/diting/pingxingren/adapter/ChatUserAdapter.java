package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.ChatUserManageItemModel;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 2016/4/11.
 */
public class ChatUserAdapter extends BaseQuickAdapter<ChatUserManageItemModel, BaseViewHolder> {
    private Context mContext;
    private int resourceId;
    private OnRobotClickListener onRobotClickListener;

    public ChatUserAdapter(int layoutResId, @Nullable List<ChatUserManageItemModel> data) {
        super(layoutResId, data);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChatUserManageItemModel chatUser) {
        LinearLayout ll_concern=holder.getView(R.id.ll_concern);
        CircleImageView iv_photo = holder.getView(R.id.iv_photo);
        TextView tv_chat_log = holder.getView(R.id.tv_chat_log);
        TextView tv_robot = holder.getView(R.id.tv_robot);
        String robotName = Utils.isEmpty(chatUser.getRobotName()) ? "游客" : chatUser.getRobotName();
        holder.setText(R.id.tv_user_name, "用户姓名: " + robotName);
        String type = null;
        switch (chatUser.getExtra4()) {
            case 1:
                type = "web机器人";
                break;
            case 2:
                type = "微信";
                break;
            case 3:
                type = "app";
                break;
            default:
                type = "web机器人";
                break;
        }
        holder.setText(R.id.tv_user_type, type + "");
        holder.setText(R.id.tv_count, "对话总次数: " + chatUser.getCount());
        holder.setText(R.id.tv_time, "时间: " + TimeUtil.millis2String(chatUser.getCreatedTime(), TimeUtil.PATTERN_YMD_HM1));

        if(MySharedPreferences.getUsername().equals(chatUser.getLoginUsername())){
            ll_concern.setVisibility(View.GONE);
        }else {
            ll_concern.setVisibility(View.VISIBLE);
        }


        if (!Utils.isEmpty(chatUser.getLoginUsername())) {
            tv_robot.setEnabled(true);
        } else {
            tv_robot.setEnabled(false);
        }
//        if (!Utils.isEmpty(chatUser.getHeadImgUrl())) {
//            Glide.with(mContext).load(chatUser.getHeadImgUrl()).into(iv_photo);
//
//        } else {
//            iv_photo.setImageResource(R.mipmap.icon_left);
//        }
        Glide.with(mContext).load(chatUser.getHeadImgUrl())
                .placeholder(R.mipmap.icon_right)
                .error(R.mipmap.icon_right)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_photo);
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRobotClickListener != null) {
                    onRobotClickListener.gotoRobotDetail(chatUser);
                }
            }
        });
        tv_chat_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRobotClickListener != null) {
                    onRobotClickListener.gotoChatLog(chatUser);
                }

            }
        });
        tv_robot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRobotClickListener != null) {
                    onRobotClickListener.gotoChat(chatUser);
                }
            }
        });
    }

    public interface OnRobotClickListener {
        void gotoChat(ChatUserManageItemModel chatUserManageItemModel);

        void gotoChatLog(ChatUserManageItemModel chatUserManageItemModel);


        void gotoRobotDetail(ChatUserManageItemModel chatUserManageItemModel);

    }
    public void setOnRobotClickListener(OnRobotClickListener onRobotClickListener) {
        this.onRobotClickListener = onRobotClickListener;
    }
}
