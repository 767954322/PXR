package com.diting.pingxingren.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class RobotRankAdapter extends ArrayAdapter<RobotConcern> {

    private int resourceId;
    private Context mContext;
    private boolean isByValue;
    private IconcernListener listener;
    public static final int SORT_BY_QUESTION = 0;
    public static final int SORT_BY_VALUE = 1;
    public static final int SORT_BY_FANS = 2;
    private int sortType;
    private boolean isLogin = true;

    public RobotRankAdapter(Context context, int resource, List<RobotConcern> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
    }

    public void setByValue(boolean isByValue) {
        this.isByValue = isByValue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RobotConcern robotConcern = getItem(position);
        View view = null;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            viewHolder.tv_robot_name = (TextView) view.findViewById(R.id.tv_robot_name);
            viewHolder.tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);
            viewHolder.tv_profile = (TextView) view.findViewById(R.id.tv_profile);
            viewHolder.btn_state = (TextView) view.findViewById(R.id.btn_state);
            viewHolder.ll_concern = (LinearLayout) view.findViewById(R.id.ll_concern);
            viewHolder.iv_concern = (ImageView) view.findViewById(R.id.iv_concern);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_robot_name.setText(robotConcern.getRobotName());
        switch (sortType) {
            case SORT_BY_QUESTION:
                viewHolder.tv_fans_count.setText("问答量: " + robotConcern.getFansCount());
                break;
            case SORT_BY_VALUE:
                viewHolder.tv_fans_count.setText("价值: " + robotConcern.getRobotValue() + "元");
                break;
            case SORT_BY_FANS:
                viewHolder.tv_fans_count.setText("粉丝: " + robotConcern.getFansCount());
                break;
            default:
                break;
        }
//        if(isByValue) {
//            viewHolder.tv_fans_count.setText("价值: " + robotConcern.getRobotValue() + "元");
//        }else {
//            viewHolder.tv_fans_count.setText("粉丝: " + robotConcern.getFansCount());
//        }

        viewHolder.tv_profile.setText(robotConcern.getCompanyName());
        if (!isLogin) {
            viewHolder.ll_concern.setVisibility(View.GONE);

        }
        if (!robotConcern.isConcerned()) {
            viewHolder.btn_state.setText("加关注");
            viewHolder.btn_state.setTextColor(Color.parseColor("#00c4c2"));
            viewHolder.iv_concern.setImageResource(R.mipmap.icon_add_concern);
            viewHolder.ll_concern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.addConcern(robotConcern);
                }
            });
        } else {
            viewHolder.btn_state.setText("已关注");
            viewHolder.btn_state.setTextColor(Color.parseColor("#969696"));
            viewHolder.iv_concern.setImageResource(R.mipmap.icon_concerned);
            viewHolder.ll_concern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.canConcern(robotConcern);
                }
            });
        }
        viewHolder.btn_state.setClickable(true);

        final String headImgUrl = robotConcern.getHeadPortrait();
        if (!Utils.isEmpty(headImgUrl)) {
            Glide.with(mContext).load(headImgUrl).into(viewHolder.iv_photo);
        } else {
            viewHolder.iv_photo.setImageResource(R.mipmap.icon_left);
        }
        return view;
    }

    static class ViewHolder {
        ImageView iv_photo;
        TextView tv_robot_name;
        TextView tv_fans_count;
        TextView tv_profile;
        TextView btn_state;
        LinearLayout ll_concern;
        ImageView iv_concern;
    }

    public void setListener(IconcernListener listener) {
        this.listener = listener;
    }

    public interface IconcernListener {
        void addConcern(RobotConcern robotConcern);

        void canConcern(RobotConcern robotConcern);
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
