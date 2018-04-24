package com.diting.pingxingren.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 14, 18:55.
 * Description: .
 */

public class SearchRobotAdapter extends RecyclerView.Adapter<SearchRobotAdapter.ViewHolder> {

    private Context mContext;
    private boolean mIsmAttention = false;
    private ValueRankAdapter.AttentionListener mListener;
    private boolean mIsLogin = true;
    private int mArrowPosition;
    private boolean mIsShow;
    private boolean mIsRemark = false;
    private boolean mItemClick = false;
    private List<RobotConcern> mRobotConcerns;

    public void setRobotConcerns(List<RobotConcern> robotConcerns) {
        mRobotConcerns = robotConcerns;
        notifyDataSetChanged();
    }

    public void setListener(ValueRankAdapter.AttentionListener listener) {
        mListener = listener;
    }

    public void setLogin(boolean login) {
        mIsLogin = login;
    }

    public void setRemark(boolean remark) {
        mIsRemark = remark;
    }

    public void setItemClick(boolean itemClick) {
        mItemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_value_rank, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ViewHolder viewHolder = holder;
        final int index = position;
        final RobotConcern robotConcern = mRobotConcerns.get(position);

        if (mIsRemark) {
            viewHolder.mRemark.setVisibility(View.VISIBLE);
            viewHolder.mRemark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.updateRemark(robotConcern, v);
                }
            });
        } else {
            viewHolder.mRemark.setVisibility(View.GONE);
        }

        viewHolder.mRobotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.getDetail(robotConcern);
            }
        });

        if (mItemClick) {
            viewHolder.mView.setClickable(true);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(robotConcern);
                }
            });
        } else {
            viewHolder.mView.setClickable(false);
        }

        if (index == 0) {
            Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_one));
            viewHolder.mNumberButton.setText("");
        } else if (index == 1) {
            Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_two));
            viewHolder.mNumberButton.setText("");
        } else if (index == 2) {
            Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_three));
            viewHolder.mNumberButton.setText("");
        } else {
            Utils.setBackGround(viewHolder.mNumberButton, null);
            viewHolder.mNumberButton.setText(index + 1 + "");
        }

        viewHolder.mAttention.setVisibility(mIsLogin ? View.VISIBLE : View.GONE);

        viewHolder.mArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrowPosition = index;
                Utils.setBackGround(viewHolder.mIvArrowDown, viewHolder.mRobotContent.getVisibility()
                        == View.VISIBLE ? ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down)
                        : ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up));
                viewHolder.mRobotContent.setVisibility(viewHolder.mRobotContent.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
                mIsShow = viewHolder.mRobotContent.getVisibility() == View.VISIBLE;
            }
        });

        if (mArrowPosition == index) {
            viewHolder.mRobotContent.setVisibility(mIsShow ? View.VISIBLE : View.GONE);
        } else {
            viewHolder.mRobotContent.setVisibility(View.GONE);
            Utils.setBackGround(viewHolder.mIvArrowDown,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down));
        }

        viewHolder.mAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (robotConcern != null) {
                    mIsmAttention = robotConcern.isConcerned();
                    if (mIsmAttention)
                        mListener.canConcern(robotConcern);
                    else
                        mListener.addConcern(robotConcern);
                }
            }
        });

        if (robotConcern != null) {
            String robotName = robotConcern.getRobotName();
            viewHolder.mRobotName.setText(!Utils.isEmpty(robotName) && !robotName.equals("null")
                    ? robotName : "未设置机器人名");
            String companyName = robotConcern.getCompanyName();
            companyName = !Utils.isEmpty(companyName) && !companyName.equals("null")
                    ? companyName : "未设置主人名";
            SpannableStringBuilder companyNameBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_host, companyName));
            companyNameBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "主人:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mCompanyName.setText(companyNameBuilder);
            String value = String.valueOf(robotConcern.getRobotValue());
            if (value.contains("."))
                value = value.substring(0, value.indexOf("."));
            SpannableStringBuilder valueBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_value, value));
            valueBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "价值:  ¥".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mValue.setText(valueBuilder);
            SpannableStringBuilder fansBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_fans, robotConcern.getFansCount()));
            fansBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "粉丝数:  ".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mFans.setText(fansBuilder);
            String profile = robotConcern.getProfile();
            viewHolder.mStyleContent.setText(Utils.isEmpty(profile) ? "这个人很懒, 什么都没有设置" : profile);
            String remark = robotConcern.getRemark();
            if (!Utils.isEmpty(remark)) {
                String s = "< " + remark + " >";
                viewHolder.mRemark.setText(s);
                Utils.setBackGround(viewHolder.mRemark, null);
            } else {
                viewHolder.mRemark.setText(null);
                Utils.setBackGround(viewHolder.mRemark, ContextCompat.getDrawable(mContext, R.drawable.ic_remarks));
            }
            final String headImgUrl = robotConcern.getHeadPortrait();
            if (!Utils.isEmpty(headImgUrl)) {
                Glide.with(mContext).load(headImgUrl).into(viewHolder.mRobotImage);
            } else {
                viewHolder.mRobotImage.setImageResource(R.mipmap.icon_left);
            }

            if(Utils.isEmpty(robotConcern.getPhone())){
                viewHolder.mAttentionState.setVisibility(View.GONE);
                viewHolder.mIvAttention.setVisibility(View.GONE);
            }else{
                viewHolder.mAttentionState.setVisibility(View.VISIBLE);
                viewHolder.mIvAttention.setVisibility(View.VISIBLE);
            }

            if (!robotConcern.isConcerned()) {
                viewHolder.mAttentionState.setText("加关注");
                viewHolder.mAttentionState.setTextColor(Color.parseColor("#00c4c2"));
                viewHolder.mIvAttention.setImageResource(R.mipmap.icon_add_concern);
                mIsmAttention = false;
            } else {
                viewHolder.mAttentionState.setText("已关注");
                viewHolder.mAttentionState.setTextColor(Color.parseColor("#969696"));
                viewHolder.mIvAttention.setImageResource(R.mipmap.icon_concerned);
                mIsmAttention = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRobotConcerns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final Button mNumberButton;
        final CircleImageView mRobotImage;
        final TextView mRobotName;
        final TextView mCompanyName;
        final LinearLayout mAttention;
        final LinearLayout mView;
        final ImageView mIvAttention;
        final TextView mAttentionState;
        final RelativeLayout mArrowDown;
        final ImageView mIvArrowDown;
        final RelativeLayout mRobotContent;
        final TextView mValue;
        final TextView mFans;
        final TextView mStyleContent;
        final TextView mRemark;

        public ViewHolder(View itemView) {
            super(itemView);
            mNumberButton = (Button) itemView.findViewById(R.id.btNumber);
            mRobotImage = (CircleImageView) itemView.findViewById(R.id.ivRobotImage);
            mRobotName = (TextView) itemView.findViewById(R.id.tvRobotName);
            mCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
            mAttention = (LinearLayout) itemView.findViewById(R.id.llAttention);
            mView = (LinearLayout) itemView.findViewById(R.id.llItem);
            mIvAttention = (ImageView) itemView.findViewById(R.id.ivAttention);
            mAttentionState = (TextView) itemView.findViewById(R.id.tvAttentionState);
            mArrowDown = (RelativeLayout) itemView.findViewById(R.id.rlArrowDown);
            mIvArrowDown = (ImageView) itemView.findViewById(R.id.ivArrowDown);
            mRobotContent = (RelativeLayout) itemView.findViewById(R.id.rlRobotContent);
            mValue = (TextView) itemView.findViewById(R.id.tvValue);
            mFans = (TextView) itemView.findViewById(R.id.tvFans);
            mStyleContent = (TextView) itemView.findViewById(R.id.tvStyleContent);
            mRemark = (TextView) itemView.findViewById(R.id.tvRemark);
        }
    }
}
