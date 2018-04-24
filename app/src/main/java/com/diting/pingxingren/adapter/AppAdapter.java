package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.AppGuide;

import java.util.List;

/**
 * Created by frank on 17-1-14.
 */

public class AppAdapter extends RecyclerView.Adapter {

    public List<AppGuide> list;
    private Context mContext;

    public AppAdapter(List<AppGuide> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_app_list, parent, false);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AppGuide item = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.iv_photo.setImageResource(item.getResourceId());
        viewHolder.tv_app_name.setText(item.getAppName());
        viewHolder.tv_app_example.setText(item.getAppExample());
    }

    @Override
    public int getItemCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_photo;
        TextView tv_app_name;
        TextView tv_app_example;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            tv_app_example = (TextView) itemView.findViewById(R.id.tv_app_example);
        }
    }
}
