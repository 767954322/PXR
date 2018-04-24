package com.diting.pingxingren.news.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.NewsListModel;
import com.diting.pingxingren.util.StringUtil;

import java.util.List;

/**
 * 新闻列表布局
 * Created by 2018 on 2018/1/9.
 */

public class NewsListView extends RelativeLayout implements View.OnClickListener {

    private ImageView icon;//上方图片
    private TextView oneTitle;
    private TextView oneSource;
    private TextView twoTitle;
    private TextView twoSource;
    private TextView threeTitle;
    private TextView threeSource;
    private TextView fourTitle;
    private TextView fourSource;
    private TextView fiveTitle;
    private TextView fiveSource;
    private TextView sixTitle;
    private TextView sixSource;
    private Context t;
    private ClickListener mClickListener;
    private List<NewsListModel> mList;

    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.t = context;
        LayoutInflater.from(context).inflate(R.layout.news_list_view, this, true);
        icon = findViewById(R.id.iv_icon);
        oneTitle = findViewById(R.id.tv_top_title);
        oneSource = findViewById(R.id.tv_top_source);
        twoTitle = findViewById(R.id.tv_two_title);
        twoSource = findViewById(R.id.tv_two_source);
        threeTitle = findViewById(R.id.tv_three_title);
        threeSource = findViewById(R.id.tv_three_source);
        fourTitle = findViewById(R.id.tv_four_title);
        fourSource = findViewById(R.id.tv_four_source);
        fiveTitle = findViewById(R.id.tv_five_title);
        fiveSource = findViewById(R.id.tv_five_source);
        sixTitle = findViewById(R.id.tv_six_title);
        sixSource = findViewById(R.id.tv_six_source);
        RelativeLayout oneClick = findViewById(R.id.rl_top_click);
        RelativeLayout twoClick = findViewById(R.id.rl_two_click);
        RelativeLayout threeClick = findViewById(R.id.rl_three_click);
        RelativeLayout fourClick = findViewById(R.id.rl_four_click);
        RelativeLayout fiveClick = findViewById(R.id.rl_five_click);
        RelativeLayout sixClick = findViewById(R.id.rl_six_click);
        oneClick.setOnClickListener(this);
        twoClick.setOnClickListener(this);
        threeClick.setOnClickListener(this);
        fourClick.setOnClickListener(this);
        fiveClick.setOnClickListener(this);
        sixClick.setOnClickListener(this);
    }

    public void setmClickListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top_click:
                setData(0);
                break;
            case R.id.rl_two_click:
                setData(1);
                break;
            case R.id.rl_three_click:
                setData(2);
                break;
            case R.id.rl_four_click:
                setData(3);
                break;
            case R.id.rl_five_click:
                setData(4);
                break;
            case R.id.rl_six_click:
                setData(5);
                break;
        }
    }

    private void setData(int index) {
        if (mClickListener != null) {
            mClickListener.onClick(mList.get(index));
        }
    }

    public void upDateDatas(List<NewsListModel> list) {
        if (list != null) {
            mList = list;
            for (int i = 0; i < list.size(); i++) {
                NewsListModel model = list.get(i);
                switch (i) {
                    case 0:
                        Glide.with(t).load(model.getImageUrl()).into(icon);
                        oneTitle.setText(model.getTitle());
                        oneSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                    case 1:
                        twoTitle.setText(model.getTitle());
                        twoSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                    case 2:
                        threeTitle.setText(model.getTitle());
                        threeSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                    case 3:
                        fourTitle.setText(model.getTitle());
                        fourSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                    case 4:
                        fiveTitle.setText(model.getTitle());
                        fiveSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                    case 5:
                        sixTitle.setText(model.getTitle());
                        sixSource.setText(pressAuthor(model.getAuthorName()));
                        break;
                }
            }
        }
    }

    private String pressAuthor(String str) {
        if (!StringUtil.isNotEmpty(str)) {
            return "";
        }
        return StringUtil.getString(R.string.from) + str;
    }

}
