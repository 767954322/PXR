package com.diting.pingxingren.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.AppGuide;
import com.diting.pingxingren.entity.Knowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2016/4/11.
 */
public class AppGuideAdapter extends ArrayAdapter<AppGuide> {
    private int resourceId;
    public AppGuideAdapter(Context context, int resource, List<AppGuide> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppGuide appGuide = getItem(position);
        View view=null;
        ViewHolder viewHolder = null;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
            viewHolder.tv_app_example = (TextView) view.findViewById(R.id.tv_app_example);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_app_name.setText(appGuide.getAppName());
        viewHolder.tv_app_example.setText(appGuide.getAppExample());
        return view;
    }
    static class ViewHolder{
        TextView tv_app_name;
        TextView tv_app_example;
    }

}
