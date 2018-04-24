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
import com.diting.pingxingren.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MyRankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private ArrayList<RobotConcern> robotRankList;
    private boolean mIsmAttention = false;
    private  AttentionListener mListener;
    private Context mContext;
    public static final int SORT_BY_QUESTION = 0;
    public static final int SORT_BY_VALUE = 1;
    public static final int SORT_BY_FANS = 2;
    private boolean mIsShow;
    private boolean mIsRemark = false;
    private boolean mItemClick = false;
    private boolean mIsLogin = true;
    private int mArrowPosition;
    public MyRankListAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<RobotConcern> data) {
        if (robotRankList == null) {
            robotRankList = new ArrayList<>();
        }
        robotRankList.clear();

        if (data != null) {

            robotRankList.addAll(data);

        }
        notifyDataSetChanged();
    }

    public void addData(List<RobotConcern> data) {
        if (robotRankList == null) {
            robotRankList = new ArrayList<>();
        }
        if (data != null) {
            robotRankList.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (robotRankList == null) {
            robotRankList = new ArrayList<>();
        }
        robotRankList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<RobotConcern> getRobotConcernList() {
        return robotRankList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_value_rank, parent, false);
            FSItemViewHolder vh = new FSItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView more_data_msg = (TextView) view.findViewById(R.id.footer_tv);

            return new FooterViewHolder(view);
        }
    }

    public void setListener(AttentionListener listener) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FSItemViewHolder) {
            final FSItemViewHolder viewHolder = (FSItemViewHolder) holder;
            final RobotConcern robotConcern = robotRankList.get(position);
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
            if (position == 0) {
                Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_one));
                viewHolder.mNumberButton.setText("");
            } else if (position == 1) {
                Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_two));
                viewHolder.mNumberButton.setText("");
            } else if (position == 2) {
                Utils.setBackGround(viewHolder.mNumberButton, ContextCompat.getDrawable(mContext, R.drawable.ic_number_three));
                viewHolder.mNumberButton.setText("");
            } else {
                Utils.setBackGround(viewHolder.mNumberButton, null);
                viewHolder.mNumberButton.setText(position + 1 + "");
            }
            viewHolder.mAttention.setVisibility(mIsLogin ? View.VISIBLE : View.GONE);

            viewHolder.mArrowDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mArrowPosition = position;
                    Utils.setBackGround(viewHolder.mIvArrowDown, viewHolder.mRobotContent.getVisibility()
                            == View.VISIBLE ? ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_down)
                            : ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_up));
                    viewHolder.mRobotContent.setVisibility(viewHolder.mRobotContent.getVisibility()
                            == View.VISIBLE ? View.GONE : View.VISIBLE);
                    mIsShow = viewHolder.mRobotContent.getVisibility() == View.VISIBLE;
                }
            });
            if (mArrowPosition == position) {
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



//                viewHolder.mRobotName.setText(robotConcern.getRobotName());
//                SpannableStringBuilder companyNameBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_host, robotConcern.getCompanyName()));
//                companyNameBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "主人:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.mCompanyName.setText(companyNameBuilder);
//
//
//                String value = String.valueOf(robotConcern.getRobotValue());
//                if (value.contains("."))
//                    value = value.substring(0, value.indexOf("."));
//                SpannableStringBuilder valueBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_value, value));
//                valueBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "价值:  ¥".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.mValue.setText(valueBuilder);
//                SpannableStringBuilder fansBuilder = new SpannableStringBuilder(mContext.getString(R.string.rank_item_fans, robotConcern.getFansCount()));
//                fansBuilder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.call_voice_bg_color)), 0, "粉丝数:  ".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.mFans.setText(fansBuilder);
//                String profile = robotConcern.getProfile();
//                viewHolder.mStyleContent.setText(Utils.isEmpty(profile) ? "这个人很懒, 什么都没有设置" : profile);
//                String remark = robotConcern.getRemark();
//                if (!Utils.isEmpty(remark)) {
//                    String s = "< " + remark + " >";
//                    viewHolder.mRemark.setText(s);
//                    Utils.setBackGround(viewHolder.mRemark, null);
//                } else {
//                    viewHolder.mRemark.setText(null);
//                    Utils.setBackGround(viewHolder.mRemark, ContextCompat.getDrawable(mContext, R.drawable.ic_remarks));
//                }
                final String headImgUrl = robotConcern.getHeadPortrait();
                if (!Utils.isEmpty(headImgUrl)) {
                    Glide.with(mContext).load(headImgUrl).into(viewHolder.mRobotImage);
                } else {
                    viewHolder.mRobotImage.setImageResource(R.mipmap.icon_left);
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
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return robotRankList == null ? 0 : robotRankList.size();
    }


    public class FSItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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

        public FSItemViewHolder(View view) {
            super(view);
            mNumberButton = (Button) view.findViewById(R.id.btNumber);
            mRobotImage = (CircleImageView) view.findViewById(R.id.ivRobotImage);
            mRobotName = (TextView) view.findViewById(R.id.tvRobotName);
            mCompanyName = (TextView) view.findViewById(R.id.tvCompanyName);
            mAttention = (LinearLayout) view.findViewById(R.id.llAttention);
            mView = (LinearLayout) view.findViewById(R.id.llItem);
            mIvAttention = (ImageView) view.findViewById(R.id.ivAttention);
            mAttentionState = (TextView) view.findViewById(R.id.tvAttentionState);
            mArrowDown = (RelativeLayout) view.findViewById(R.id.rlArrowDown);
            mIvArrowDown = (ImageView) view.findViewById(R.id.ivArrowDown);
            mRobotContent = (RelativeLayout) view.findViewById(R.id.rlRobotContent);
            mValue = (TextView) view.findViewById(R.id.tvValue);
            mFans = (TextView) view.findViewById(R.id.tvFans);
            mStyleContent = (TextView) view.findViewById(R.id.tvStyleContent);
            mRemark = (TextView) view.findViewById(R.id.tvRemark);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, this.getPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public interface AttentionListener {
        void addConcern(RobotConcern robotConcern);

        void canConcern(RobotConcern robotConcern);

        void updateRemark(RobotConcern robotConcern, View view);

        void getDetail(RobotConcern robotConcern);

        void onItemClick(RobotConcern robotConcern);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
