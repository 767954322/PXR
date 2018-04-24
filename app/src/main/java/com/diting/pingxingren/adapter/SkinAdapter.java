package com.diting.pingxingren.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;

/**
 * Creator: Gu FanFan.
 * Date: 2017/10/26, 12:57.
 * Description: 换肤Adapter.
 */

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {

    private SaveSkin mSaveSkin;
    private int mSkinRes;

    private int[] icons = {R.drawable.icon_skin_default, R.drawable.icon_skin_one, R.drawable.icon_skin_two,
            R.drawable.icon_skin_three, R.drawable.icon_skin_four, R.drawable.icon_skin_five,
            R.drawable.icon_skin_six};

    private int[] backgrounds = {R.drawable.ic_skin_one, R.drawable.ic_skin_two,
            R.drawable.ic_skin_three, R.drawable.ic_skin_four, R.drawable.ic_skin_five,
            R.drawable.ic_skin_six};

    private int[] strings = {R.string.skin_default, R.string.skin_one, R.string.skin_two,
            R.string.skin_three, R.string.skin_four, R.string.skin_five, R.string.skin_six};

    public void setSaveSkin(SaveSkin saveSkin) {
        mSaveSkin = saveSkin;
    }

    public void setSkinRes(int skinRes) {
        mSkinRes = skinRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_skinning, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mSkinLayout.setBackgroundResource(icons[i]);
        viewHolder.mSkinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0)
                    mSaveSkin.onSave(-1);
                else
                    mSaveSkin.onSave(backgrounds[i - 1]);
            }
        });
        viewHolder.mSkinTextView.setText(strings[i]);

        if (mSkinRes == -1)
            viewHolder.mUsingTextView.setVisibility(View.VISIBLE);

        if (i > 0) {
            if (backgrounds[i - 1] == mSkinRes)
                viewHolder.mUsingTextView.setVisibility(View.VISIBLE);
            else
                viewHolder.mUsingTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout mSkinLayout;
        final TextView mUsingTextView;
        final TextView mSkinTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mSkinLayout = (RelativeLayout) itemView.findViewById(R.id.rlSkinning);
            mUsingTextView = (TextView) itemView.findViewById(R.id.tvUsing);
            mSkinTextView = (TextView) itemView.findViewById(R.id.tvSkinText);
        }
    }

    public interface SaveSkin {
        void onSave(int skinRes);
    }
}
