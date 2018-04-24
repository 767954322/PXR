package com.diting.pingxingren.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 2016/4/11.
 */
public class RobotConcernAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private int resourceId;
    private IconcernListener listener;
    private List<RobotConcern> mOriginalValues;
    private List<RobotConcern> robotConcernList;
    private final Object mLock = new Object();

    public RobotConcernAdapter(Context context, int resource, List<RobotConcern> objects) {
        robotConcernList = objects;
        resourceId = resource;
        mContext = context;
    }

    @Override
    public int getCount() {
        return robotConcernList.size();
    }

    @Override
    public Object getItem(int position) {
        return robotConcernList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final RobotConcern robotConcern = (RobotConcern) getItem(position);
        View view = null;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_photo = (CircleImageView) view.findViewById(R.id.iv_photo);
            viewHolder.tv_robot_name = (TextView) view.findViewById(R.id.tv_robot_name);
            viewHolder.tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);
            viewHolder.tv_profile = (TextView) view.findViewById(R.id.tv_profile);
            viewHolder.btn_state = (TextView) view.findViewById(R.id.btn_state);
            viewHolder.ll_concern = (LinearLayout) view.findViewById(R.id.ll_concern);
            viewHolder.iv_concern = (ImageView) view.findViewById(R.id.iv_concern);
            viewHolder.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_robot_name.setText(robotConcern.getRobotName());
        viewHolder.tv_fans_count.setText("粉丝数: " + robotConcern.getFansCount());
        //viewHolder.tv_profile.setText(robotConcern.getCompanyName());
        String welcome = robotConcern.getWelcome();
        viewHolder.tv_profile.setText(Utils.isEmpty(welcome) ? Const.commonWelcome : welcome);
        String headImgUrl = robotConcern.getHeadPortrait();
        if (!Utils.isEmpty(headImgUrl)) {
            Glide.with(mContext).load(headImgUrl).into(viewHolder.iv_photo);
        } else {
            viewHolder.iv_photo.setImageResource(R.mipmap.icon_left);
        }
        viewHolder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoDetail(robotConcern);
            }
        });
        viewHolder.btn_state.setText("已关注");
        viewHolder.btn_state.setTextColor(Color.parseColor("#969696"));
        viewHolder.iv_concern.setImageResource(R.mipmap.icon_concerned);
        viewHolder.ll_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.canConcern(robotConcern);
            }
        });
        viewHolder.tv_remark.setVisibility(View.VISIBLE);
        final String remark = robotConcern.getRemark();
        if (!Utils.isEmpty(remark)) {
            viewHolder.tv_remark.setText(remark);
        } else {
            viewHolder.tv_remark.setText("备注");
        }
        //viewHolder.tv_remark.setVisibility(View.VISIBLE);
        viewHolder.tv_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updateRemark(robotConcern, v);
            }
        });
        return view;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            robotConcernList = (List<RobotConcern>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // TODO Auto-generated method stub
            FilterResults filterResults = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = robotConcernList;
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<RobotConcern> list;
                synchronized (mLock) {
                    list = new ArrayList<RobotConcern>(mOriginalValues);
                }
                filterResults.values = list;
                filterResults.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<RobotConcern> values;
                synchronized (mLock) {
                    values = new ArrayList<RobotConcern>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<RobotConcern> newValues = new ArrayList<RobotConcern>();

                for (int i = 0; i < count; i++) {
                    final RobotConcern value = values.get(i);
                    final String valueText = value.getRobotName();
                    // First match against the whole, non-splitted value
                    if (valueText.contains(prefixString)) {
                        newValues.add(value);
                    }
                }

                filterResults.values = newValues;
                filterResults.count = newValues.size();
            }

            return filterResults;
        }

    };

    public void setData(List<RobotConcern> robotConcernList) {
        this.robotConcernList = robotConcernList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        CircleImageView iv_photo;
        TextView tv_robot_name;
        TextView tv_fans_count;
        TextView tv_profile;
        TextView btn_state;
        LinearLayout ll_concern;
        ImageView iv_concern;
        TextView tv_remark;
    }

    public void setListener(IconcernListener listener) {
        this.listener = listener;
    }

    public interface IconcernListener {
        void canConcern(RobotConcern robotConcern);

        void updateRemark(RobotConcern robotConcern, View view);

        void gotoDetail(RobotConcern robotConcern);
    }
}
