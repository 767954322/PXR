package com.diting.pingxingren.adapter;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.diting.pingxingren.R;
import com.diting.pingxingren.model.KnowledgeItemModel;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class KnowledgeAdapter extends BaseQuickAdapter<KnowledgeItemModel, BaseViewHolder> {

    private ItemClickListener mItemClickListener;

    private boolean    highFrequencyQuestion  = true;

    public void setHighFrequencyQuestion(boolean highFrequencyQuestion) {
        this.highFrequencyQuestion = highFrequencyQuestion;
    }

    public KnowledgeAdapter(@LayoutRes int layoutResId, @Nullable List<KnowledgeItemModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final KnowledgeItemModel item) {
        final String question = item.getQuestion();
        String answer = item.getAnswer();
        String scene = item.getScene();
        String file = item.getFile_url();
        String video = item.getVideo_url();
        String audio = item.getAudio_url();
        String photo = item.getImg_url();
        String hyperLink = item.getHyperlink_url();
          highFrequencyQuestion = item.isHighFrequencyQuestion();
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
                .setText(R.id.tv_answer_content, answer)
                .setText(R.id.tvScenes, Utils.isEmpty(scene) ? "请设置场景" : scene)
                .setText(R.id.tvAnnex, annex);

//        if (Utils.isInTheLanguages(question, item.getId())) {
//            holder.getView(R.id.tvAdd).setVisibility(View.GONE);
//            holder.getView(R.id.ivLanguagePrompt).setVisibility(View.GONE);
//        } else {
//            holder.getView(R.id.tvAdd).setVisibility(View.VISIBLE);
//            holder.getView(R.id.ivLanguagePrompt).setVisibility(View.VISIBLE);
//        }

        if (!item.isAddCommon()) {
            if (highFrequencyQuestion) {
                holder.getView(R.id.tvAdd).setVisibility(View.GONE);
                holder.getView(R.id.ivLanguagePrompt).setVisibility(View.GONE);
            } else {
                holder.getView(R.id.tvAdd).setVisibility(View.VISIBLE);
                holder.getView(R.id.ivLanguagePrompt).setVisibility(View.VISIBLE);
            }
        } else {
            holder.getView(R.id.tvAdd).setVisibility(View.GONE);
            holder.getView(R.id.ivLanguagePrompt).setVisibility(View.GONE);
        }

        holder.getView(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.deleteKnowledge(item);
            }
        });
        holder.getView(R.id.tvEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.editKnowledge(item);
            }
        });
        holder.getView(R.id.tvAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.addCommonLanguage(item);
            }
        });
        holder.getView(R.id.ivLanguagePrompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.showLanguagePrompt();
            }
        });
    }

    public void setListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void deleteKnowledge(KnowledgeItemModel knowledgeItem);

        void editKnowledge(KnowledgeItemModel knowledgeItem);

        void addCommonLanguage(KnowledgeItemModel knowledgeItemModel);

        void showLanguagePrompt();
    }
}
