package com.diting.pingxingren.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.ExternalOptions;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<ExternalOptions> {
    private int resourceId;
    public GridViewAdapter(Context context,int resource, List<ExternalOptions> appList) {
        super(context,resource,appList);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExternalOptions externalOptions = getItem(position);
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.btn_appName = (TextView) view.findViewById(R.id.btn_app_name);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.btn_appName.setText(externalOptions.getAppName());
        if(!externalOptions.isChoose()){
            viewHolder.btn_appName.setBackgroundResource(R.drawable.application_btn_disable);
            viewHolder.btn_appName.setTextColor(Color.parseColor("#646464"));
        }else {
            viewHolder.btn_appName.setBackgroundResource(R.drawable.application_btn_enable);
            viewHolder.btn_appName.setTextColor(Color.parseColor("#ffffff"));
        }
//        view.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        return view;
    }
    static class ViewHolder{
        TextView btn_appName;
    }
}
