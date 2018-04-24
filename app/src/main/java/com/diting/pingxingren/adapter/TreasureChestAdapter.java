package com.diting.pingxingren.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;

/**
 * Creator: Gu FanFan.
 * Date: 2017/10/26, 12:57.
 * Description: 百宝箱Adapter.
 */

public class TreasureChestAdapter extends RecyclerView.Adapter<TreasureChestAdapter.ViewHolder> {

    private TreasureItemClick mTreasureItemClick;

    private int[] icons = {R.drawable.ic_treasure_chest_visitors, R.drawable.ic_treasure_chest_call, R.drawable.ic_treasure_chest_turned,
            R.drawable.ic_treasure_chest_mail, R.drawable.ic_treasure_chest_call, R.drawable.ic_treasure_chest_find};

    private int[] strings = {R.string.visitors, R.string.mail, R.string.turned,
            R.string.private_message, R.string.call, R.string.find};

    public void setTreasureItemClick(TreasureItemClick treasureItemClick) {
        mTreasureItemClick = treasureItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_treasure_chest, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mTreasureTextView.setText(strings[i]);
        viewHolder.mTreasureImageView.setBackgroundResource(icons[i]);
        viewHolder.mTreasureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTreasureItemClick != null)
                    mTreasureItemClick.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout mTreasureLayout;
        final TextView mTreasureTextView;
        final ImageView mTreasureImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTreasureLayout = (LinearLayout) itemView.findViewById(R.id.llTreasure);
            mTreasureTextView = (TextView) itemView.findViewById(R.id.tvTreasure);
            mTreasureImageView = (ImageView) itemView.findViewById(R.id.ivTreasure);
        }
    }

    public interface TreasureItemClick {

        void onClick(int position);
    }
}
