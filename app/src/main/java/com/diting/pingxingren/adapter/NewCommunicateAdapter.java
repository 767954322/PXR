package com.diting.pingxingren.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 沟通的adapter
 * Created by Administrator on 2017/12/11.
 */

public class NewCommunicateAdapter extends BaseQuickAdapter<CommunicateModel, BaseViewHolder> {
    public AttentionListener mListener;
    public boolean mIsmAttention;
    private int mArrowPosition;
    private boolean mIsShow;

    public NewCommunicateAdapter(int layoutResId, @Nullable List<CommunicateModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final CommunicateModel item) {
        final int position = holder.getLayoutPosition();
        LinearLayout mllItem = holder.getView(R.id.llItem);
        CircleImageView mivRobotImage = holder.getView(R.id.ivRobotImage);//头像
        LinearLayout mAttention = holder.getView(R.id.llAttention);//加关注布局
        RelativeLayout mrlArrowDown = holder.getView(R.id.rlArrowDown);//展开按钮布局
        final RelativeLayout mrlRobotContent = holder.getView(R.id.rlRobotContent);//展开后下面的布局
        Button mbtNumber = holder.getView(R.id.btNumber);//前面你的数字排序
        final ImageView mIvArrowDown = holder.getView(R.id.ivArrowDown);//想下展开的图片
        TextView  mStyleShow=holder.getView(R.id.tvStyleContent);

        String name = item.getName();//      机器人名称
        String robotHeadImg = item.getRobotHeadImg();//机器人头像
        String profile = item.getProfile();//机器人个性
        String companyName = item.getCompanyName();//主人名称
        String fansNum = item.getFansNum();//粉丝数
        String robotValue = item.getRobotVal();

        if(!MySharedPreferences.getLogin()){
            mAttention.setVisibility(View.INVISIBLE);
        }
        if(item.getUniqueId().equals(MySharedPreferences.getUniqueId())){
            mAttention.setVisibility(View.INVISIBLE);
        }
        if (!Utils.isEmpty(robotHeadImg)) {
            Glide.with(mContext).load(robotHeadImg).into(mivRobotImage);
        } else {
            mivRobotImage.setImageResource(R.mipmap.icon_left);
        }
        //机器人姓名
        holder.setText(R.id.tvRobotName, !Utils.isEmpty(name) && !name.equals("null")
                ? name : "未设置机器人名");
        String value = String.valueOf(robotValue);
        if (value.contains("."))
            value = value.substring(0, value.indexOf("."));
        holder.setText(R.id.tvValue, value);

        holder.setText(R.id.tvCompanyName, companyName);
        holder.setText(R.id.tvFans, fansNum);
        if (!Utils.isEmpty(profile)) {
            String[] strings = profile.split(SPACE);
            if (strings.length != 2) {//当老用户没有该标记时
                mStyleShow.setText(Utils.isEmpty(profile) ? "这个人很懒, 什么都没有设置" : profile);
            } else {
                initWelcomeContent(strings[0], strings[1],  mStyleShow);
            }
        }else{
            mStyleShow.setText(Utils.isEmpty(profile) ? "这个人很懒, 什么都没有设置" : profile);
        }
//        holder.setText(R.id.tvStyleContent, Utils.isEmpty(profile) ? "这个人很懒, 什么都没有设置" : profile);
        mllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(item);
            }
        });
        mivRobotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.getDetail(item);
            }
        });
        if (position == 0) {
            Utils.setBackGround(mbtNumber, ContextCompat.getDrawable(mContext, R.drawable.ic_number_one));
            mbtNumber.setText("");
        } else if (position == 1) {
            Utils.setBackGround(mbtNumber, ContextCompat.getDrawable(mContext, R.drawable.ic_number_two));
            mbtNumber.setText("");
        } else if (position == 2) {
            Utils.setBackGround(mbtNumber, ContextCompat.getDrawable(mContext, R.drawable.ic_number_three));
            mbtNumber.setText("");
        } else {
            Utils.setBackGround(mbtNumber, null);
            mbtNumber.setText(position + 1 + "");
        }
        mrlArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrowPosition = position;
                Utils.setImageView(mIvArrowDown, mrlRobotContent.getVisibility()
                        == View.VISIBLE ? ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down)
                        : ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up));
                mrlRobotContent.setVisibility(mrlRobotContent.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
                mIsShow = mrlRobotContent.getVisibility() == View.VISIBLE;
            }
        });
        if (mArrowPosition == position) {
            mrlRobotContent.setVisibility(mIsShow ? View.VISIBLE : View.GONE);
        } else {
            mrlRobotContent.setVisibility(View.GONE);
            Utils.setImageView(mIvArrowDown,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down));
        }
        mAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item != null) {
                    mIsmAttention = item.getIndustryIds();
                    if (mIsmAttention)
                        mListener.canConcern(item);
                    else
                        mListener.addConcern(item);
                }
            }
        });
        if (!item.getIndustryIds()) {
            holder.setText(R.id.tvAttentionState, "加关注");
            holder.setTextColor(R.id.tvAttentionState, Color.parseColor("#00c4c2"));
            holder.setImageResource(R.id.ivAttention, R.mipmap.icon_add_concern);
            mIsmAttention = true;
        } else {

            holder.setText(R.id.tvAttentionState, "已关注");
            holder.setTextColor(R.id.tvAttentionState, Color.parseColor("#969696"));
            holder.setImageResource(R.id.ivAttention, R.mipmap.icon_concerned);
            mIsmAttention = false;
        }


    }
    private static final String TYPE_TEXT = "TYPE_TEXT";
    private static final String TYPE_AUDIO = "TYPE_AUDIO";
    private static final String TYPE_FILE = "TYPE_FILE";
    private static final String TYPE_PICTURE = "TYPE_PICTURE";
    private static final String SPACE = ";";

    private void initWelcomeContent(String type, String content, TextView mStyleShow) {
        switch (type) {
            case TYPE_TEXT:
                mStyleShow.setText(Utils.isEmpty(content) ? "我很懒! 没有写动态啦!" : content);
                break;
            case TYPE_PICTURE:
                mStyleShow.setText("图片");

                break;
            case TYPE_AUDIO:
                mStyleShow.setText("音频");

                break;
            case TYPE_FILE:
                mStyleShow.setText("文件");
                break;
        }
    }

    public void setmListener(AttentionListener mListener) {
        this.mListener = mListener;
    }

    public interface AttentionListener {
        void addConcern(CommunicateModel communicateBean);

        void canConcern(CommunicateModel communicateBean);


        void getDetail(CommunicateModel communicateBean);

        void onItemClick(CommunicateModel communicateBean);
    }

}
