package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.AskListModel;

import java.util.List;

/**
 * 会问的adapter
 * Created by Administrator on 2018/1/10.
 */

public class AskQuestionAdapter extends BaseQuickAdapter<AskListModel, BaseViewHolder> {

    private onAskItemClickListener onAskItemClickListener;

    public AskQuestionAdapter(int layoutResId, @Nullable List<AskListModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final AskListModel item) {
        RelativeLayout maskRel = holder.getView(R.id.rel_ask_question);
        ImageView imageView = holder.getView(R.id.iv_ask_question);
        holder.setText(R.id.tv_ask_question, item.getQuestion());


        if (item.isAskChange()) {
            imageView.setImageResource(R.mipmap.icon_ask_qustion_finish);
        } else {
//            imageView.setImageResource(R.mipmap.icon_ask_qustion_unask);
            if (item.getAskrobot() != null) {
                imageView.setImageResource(R.mipmap.icon_ask_qustion_finish);
            } else {
                imageView.setImageResource(R.mipmap.icon_ask_qustion_unask);

            }
        }
//        if(!item.isAskChange()){
//        if (item.getAskrobot() != null) {
//            imageView.setImageResource(R.mipmap.icon_ask_qustion_finish);
//        } else {
//            imageView.setImageResource(R.mipmap.icon_ask_qustion_unask);
//
//        }
        maskRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getAskrobot() != null || item.isAskChange()) {

                } else {
                    onAskItemClickListener.onaskInfo(item);

                }

            }
        });
//        }else{
//            imageView.setImageResource(R.mipmap.icon_ask_qustion_finish);
//        }


    }


    public void setOnAskItemClickListener(AskQuestionAdapter.onAskItemClickListener onAskItemClickListener) {
        this.onAskItemClickListener = onAskItemClickListener;
    }

    public interface onAskItemClickListener {
        void onaskInfo(AskListModel askListModel);

    }
}
