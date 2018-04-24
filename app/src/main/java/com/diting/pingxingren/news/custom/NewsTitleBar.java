package com.diting.pingxingren.news.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.StringUtil;

/**
 * Created by 2018 on 2018/1/5.
 * 新闻页面标题
 */

public class NewsTitleBar extends RelativeLayout implements View.OnClickListener {

    private Context t;
    private TextView left;//左侧按钮
    private TextView title;//标题文字
    private ImageView icon;//标题文字左侧图标
    private View line;//线
    private ImageView rightFirst;//右侧第一个按钮
    private ImageView rightSecond;//右侧第二个按钮
    private RelativeLayout layout;

    private onTitleClickListener mListener;

    public NewsTitleBar(Context context) {
        super(context);
        this.t = context;
        init();
    }

    public NewsTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.t = context;
        init();
    }

    //主要用于夜间日间模式的切换
    public void updateIcon() {
        rightFirst.setImageResource(R.drawable.news_more);
        rightSecond.setImageResource(R.drawable.news_night_share);
    }

    private void init() {
        LayoutInflater.from(t).inflate(R.layout.news_title_bar, this);
        left = findViewById(R.id.title_btn_left);
        title = findViewById(R.id.title_title);
        icon = findViewById(R.id.iv_icon);
        line = findViewById(R.id.line);
        rightFirst = findViewById(R.id.title_iv_right);
        rightSecond = findViewById(R.id.title_iv_right_second);
        layout = findViewById(R.id.title_bar);
        initListener();
    }

    private void initListener() {
        left.setOnClickListener(this);
//        close.setOnClickListener(this);
        rightFirst.setOnClickListener(this);
        rightSecond.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    //添加标题与标题左侧图标
    public void setTitle(String text, int imgRes, onTitleClickListener listener){
        this.mListener = listener;
        if(StringUtil.isNotEmpty(text)){
            title.setText(text);
        }
        if(imgRes != 0){
            icon.setVisibility(VISIBLE);
            icon.setImageResource(imgRes);
        }
    }
    //显示线
    public void showLine(){
        line.setVisibility(VISIBLE);
    }

    public void setOtherTitle(String text, boolean isSetting, boolean isShare, onTitleClickListener listener) {
        if (StringUtil.isNotEmpty(text)) {
            left.setText(text);
        }
        if (isSetting) {
            rightFirst.setVisibility(VISIBLE);
        }
        if (isShare) {
            rightSecond.setVisibility(VISIBLE);
        }
        updateIcon();
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:
                if (mListener != null) {
                    mListener.onBack(false);
                }
                break;
            case R.id.title_iv_right:
                if (mListener != null) {
                    mListener.onSetting();
                }
                break;
            case R.id.title_iv_right_second:
                if (mListener != null) {
                    mListener.onShare();
                }
                break;
        }
    }

    public interface onTitleClickListener {
        void onBack(boolean isFinish);//是否点击了关闭页面
        void onSetting();
        void onShare();
    }
}
