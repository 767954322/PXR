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
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class RobotNearbyAdapter extends ArrayAdapter<RobotConcern> {
    private int resourceId;
    private Context mContext;

    public RobotNearbyAdapter(Context context, int resource, List<RobotConcern> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RobotConcern robotConcern = getItem(position);
        View view = null;
        final ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            viewHolder.tv_robot_name = (TextView)view.findViewById(R.id.tv_robot_name);
            viewHolder.tv_fans_count = (TextView)view.findViewById(R.id.tv_fans_count);
            viewHolder.tv_welcome = (TextView)view.findViewById(R.id.tv_welcome);
            viewHolder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_robot_name.setText(robotConcern.getRobotName());
        viewHolder.tv_fans_count.setText("粉丝数: " + robotConcern.getFansCount());
        viewHolder.tv_welcome.setText(Utils.isEmpty(robotConcern.getWelcome()) ? Const.commonWelcome : robotConcern.getWelcome());
        viewHolder.tv_distance.setText(robotConcern.getDistance() + "");
        final String headImgUrl = robotConcern.getHeadPortrait();
        if(!Utils.isEmpty(headImgUrl)){
            Glide.with(mContext).load(headImgUrl).into(viewHolder.iv_photo);
        }else {
            viewHolder.iv_photo.setImageResource(R.mipmap.icon_left);
        }
        return view;
    }
    static class ViewHolder{
        ImageView iv_photo;
        TextView tv_robot_name;
        TextView tv_fans_count;
        TextView tv_welcome;
        TextView tv_distance;
    }
}
