package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.NotScrollGridView;
import com.diting.pingxingren.entity.VoicePeopleModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/27, 14:51.
 */

public class VoicePeopleAdapter extends RecyclerView.Adapter<VoicePeopleAdapter.ViewHolder> {

    private Context mContext;
    private LinkedHashMap<String, ArrayList<VoicePeopleModel>> mVoiceHashMap;
    private LayoutInflater mInflater;
    private String[] mTitleArray;
    private ItemClickListener mListener;

    public VoicePeopleAdapter(Context context,
                              LinkedHashMap<String, ArrayList<VoicePeopleModel>> voiceHashMap) {
        mContext = context;
        mVoiceHashMap = voiceHashMap;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListener(ItemClickListener listener) {
        mListener = listener;
    }

    public void setVoiceHashMap(LinkedHashMap<String, ArrayList<VoicePeopleModel>> voiceHashMap) {
        if (mVoiceHashMap == null)
            mVoiceHashMap = new LinkedHashMap<>();

        mVoiceHashMap = voiceHashMap;
        mTitleArray = mVoiceHashMap.keySet().toArray(new String[mVoiceHashMap.size()]);
        notifyItemRangeChanged(0, mVoiceHashMap.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.voice_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mTitleArray[position];
        holder.mNameView.setText(title);
        PeopleAdapter peopleAdapter = new PeopleAdapter(mContext);
        peopleAdapter.setVoiceHashMap(mVoiceHashMap.get(title));
        holder.mNotScrollGridView.setAdapter(peopleAdapter);
        holder.mNotScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoicePeopleModel peopleModel = (VoicePeopleModel) parent.getItemAtPosition(position);
                if (mListener != null && peopleModel != null)
                    mListener.onClick(peopleModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVoiceHashMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;
        public final NotScrollGridView mNotScrollGridView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameView =   itemView.findViewById(R.id.title);
            mNotScrollGridView =   itemView.findViewById(R.id.voicePeopleGrid);
        }
    }

    public interface ItemClickListener {
        void onClick(VoicePeopleModel peopleModel);
    }
}
