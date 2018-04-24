package com.diting.pingxingren.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.VoicePeopleModel;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import java.util.ArrayList;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/27, 14:51.
 */

public class PeopleAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<VoicePeopleModel> mPeopleModels;
    private LayoutInflater mInflater;

    public PeopleAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setVoiceHashMap(ArrayList<VoicePeopleModel> peopleModels) {
        if (mPeopleModels == null)
            mPeopleModels = new ArrayList<>();

        mPeopleModels = peopleModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPeopleModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeopleModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.people_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VoicePeopleModel peopleModel = mPeopleModels.get(position);
        String name = peopleModel.getPeopleName();
        String code = peopleModel.getPeopleCode();
        Drawable drawable = ContextCompat.getDrawable(mContext, peopleModel.getPeopleHead());
//        LogUtils.e("name-----------code---" +code);
//        LogUtils.e("name-----------" + MySharedPreferences.getVoicePeople());
        if (!Utils.isEmpty(MySharedPreferences.getVoicePeople())) {
            if (code.equals(MySharedPreferences.getVoicePeople())) {
                LogUtils.e("name---" + name);

            }
        } else {
            LogUtils.e("name---" + name);
        }
        if (name.equals("原声")) {
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.mImageViewDialect.setVisibility(View.GONE);
            setBackGround(holder.mImageView, drawable);
            if (Utils.isEmpty(MySharedPreferences.getVoicePeople())) {
                holder.mImageView_type.setVisibility(View.VISIBLE);
                holder.peopleHeadDialect_type.setVisibility(View.GONE);
            }
        } else if (name.equals("粤语") || name.equals("东北话") || name.equals("四川话")
                || name.equals("河南话") || name.equals("湖南话") || name.equals("陕西话")) {
            if (!Utils.isEmpty(MySharedPreferences.getVoicePeople())) {
                if (code.equals(MySharedPreferences.getVoicePeople())) {
                    holder.mImageView_type.setVisibility(View.GONE);
                    holder.peopleHeadDialect_type.setVisibility(View.VISIBLE);

                }

            }
            holder.mImageView.setVisibility(View.GONE);
            holder.mImageViewDialect.setVisibility(View.VISIBLE);
            setBackGround(holder.mImageViewDialect, drawable);
        } else {
            if (!Utils.isEmpty(MySharedPreferences.getVoicePeople())) {
                if (code.equals(MySharedPreferences.getVoicePeople())) {
                    holder.mImageView_type.setVisibility(View.VISIBLE);
                    holder.peopleHeadDialect_type.setVisibility(View.GONE);

                }

            }
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.mImageViewDialect.setVisibility(View.GONE);
            setBackGround(holder.mImageView, drawable);
        }
        holder.mNameView.setText(name);
        return convertView;
    }

    class ViewHolder {

        public final TextView mNameView;
        public final ImageView mImageView;
        public final ImageView mImageViewDialect;
        public final ImageView peopleHeadDialect_type;
        public final ImageView mImageView_type;


        public ViewHolder(View itemView) {
            mNameView = itemView.findViewById(R.id.peopleName);
            mImageView = itemView.findViewById(R.id.peopleHead);
            mImageViewDialect = itemView.findViewById(R.id.peopleHeadDialect);
            peopleHeadDialect_type = itemView.findViewById(R.id.peopleHeadDialect_type);
            mImageView_type = itemView.findViewById(R.id.peopleHead_type);
        }
    }

    private void setBackGround(ImageView imageView, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(drawable);
        } else {
            imageView.setBackgroundDrawable(drawable);
        }
    }
}
