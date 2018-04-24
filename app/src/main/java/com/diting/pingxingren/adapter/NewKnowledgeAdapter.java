package com.diting.pingxingren.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.entity.Knowledge;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2016/4/11.
 */
public class NewKnowledgeAdapter extends ArrayAdapter<Knowledge> {
    private int resourceId;
    private List<Knowledge> knowledgeList;
    private IknowledgeListener listener;

    public NewKnowledgeAdapter(Context context, int resource, List<Knowledge> knowledgeList) {
        super(context, resource, knowledgeList);
        resourceId = resource;
        this.knowledgeList = knowledgeList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Knowledge knowledge = getItem(position);
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_question_content = (TextView) view.findViewById(R.id.tv_question_content);
            viewHolder.tv_answer_content = (TextView) view.findViewById(R.id.tv_answer_content);
            viewHolder.tv_delete = (TextView) view.findViewById(R.id.tvDelete);
            viewHolder.tv_edit = (TextView) view.findViewById(R.id.tvEdit);
            viewHolder.tv_add = (TextView) view.findViewById(R.id.tvAdd);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        String question = knowledge.getQuestion();
        viewHolder.tv_question_content.setText(question);
        viewHolder.tv_answer_content.setText(knowledge.getAnswer());
        if (Utils.isInTheLanguages(question, knowledge.getKnowledgeId())) {
            viewHolder.tv_add.setVisibility(View.GONE);
        } else {
            viewHolder.tv_add.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteKnowledge(knowledge);
            }
        });
        viewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddKnowledgeActivity.class);
                intent.putExtra("knowledge", knowledge);
                getContext().startActivity(intent);
            }
        });
        viewHolder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addCommonLanguage(knowledge.getQuestion());
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView tv_question_content;
        TextView tv_answer_content;
        TextView tv_delete;
        TextView tv_edit;
        TextView tv_add;
    }

    public void setListener(IknowledgeListener listener) {
        this.listener = listener;
    }

    public interface IknowledgeListener {
        void deleteKnowledge(Knowledge knowledge);

        void addCommonLanguage(String knowledgeQuestion);
    }

}
