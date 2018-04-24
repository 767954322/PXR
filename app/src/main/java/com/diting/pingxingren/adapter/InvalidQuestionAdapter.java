package com.diting.pingxingren.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.InvalidQuestionItemModel;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2016/4/11.
 * 未知问题页面
 */
public class InvalidQuestionAdapter extends BaseQuickAdapter<InvalidQuestionItemModel, BaseViewHolder> {
    private UnknowledgeListener listener;
    public InvalidQuestionAdapter(int layoutResId, @Nullable List<InvalidQuestionItemModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final InvalidQuestionItemModel item) {
        final String question = item.getQuestion();
        int frequency = item.getNum();
        String file = item.getFile_url();
        String video = item.getVideo_url();
        String audio = item.getAudio_url();
        String photo = item.getImg_url();
        String hyperLink = item.getHyperlink_url();
        String annex;
        if (!Utils.isEmpty(file)) {
            annex = "文件";
        } else if (!Utils.isEmpty(video)) {
            annex = "视频";
        } else if (!Utils.isEmpty(audio)) {
            annex = "音频";
        } else if (!Utils.isEmpty(photo)) {
            annex = "照片";
        } else if (!Utils.isEmpty(hyperLink)) {
            annex = "超链接";
        } else {
            annex = "暂无附件, 请添加";
        }
        holder.setText(R.id.tv_question_content, question)
                .setText(R.id.tv_answer, "频率：")
                .setText(R.id.tv_answer_content, frequency+"")
                .setText(R.id.tvScenes,item.getScene())
                .setText(R.id.tvAnnex, annex);




        holder.getView(R.id.tvEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editKnowledge(item);
            }
        });
        holder.getView(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteKnowledge(item);
            }
        });
    }
    public void setListener(UnknowledgeListener listener) {
        this.listener = listener;
    }
    public interface UnknowledgeListener {
        void deleteKnowledge(InvalidQuestionItemModel invalidQuestion);

        void editKnowledge(InvalidQuestionItemModel invalidQuestion);

    }
}
