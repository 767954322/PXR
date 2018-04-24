package com.diting.pingxingren.news.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.news.listener.OnTabCheckedListener;
import com.diting.pingxingren.util.ScreenUtil;

/**
 * 四个底部按钮,暂不支持按钮数量变化
 * 只需要动态创建radiobutton即可.
 * Created by 2018 on 2018/1/6.
 */

public class NewsMainBottomView extends RelativeLayout implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private RadioGroup mBottomButton;//底部按钮父类
    private View line;//线
    private int lineMode = MODE_TEXT_WIDTH;//线的模式
    private int textWidth;//文字宽度
    private OnTabCheckedListener mListener;
    private RadioButton bt1;
    private RadioButton bt2;
    private RadioButton bt3;
    private RadioButton bt4;

    private String[] mTextList;//放置文本

    public static final int MODE_AVERAGE_PARENT = 1;//平均分控件宽度
    public static final int MODE_TEXT_WIDTH = 2;//文字的宽度

    private ViewPager pager;

    private int viewWidth;//控件宽度

    private int currentPosition = -1;//当前显示的页面下标

    private int animDuration = 200;

    public NewsMainBottomView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.news_main_bottom_bt, this);
        mBottomButton = findViewById(R.id.radio_group);
        line = findViewById(R.id.line_view);
        bt1 = findViewById(R.id.rb_one);
        bt2 = findViewById(R.id.rb_two);
        bt3 = findViewById(R.id.rb_three);
        bt4 = findViewById(R.id.rb_four);
        init();
    }

    public void setOnTabClickListener(OnTabCheckedListener listener){
        this.mListener = listener;
    }

    public NewsMainBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.news_main_bottom_bt, this);
        mBottomButton = findViewById(R.id.radio_group);
        line = findViewById(R.id.line_view);
        bt1 = findViewById(R.id.rb_one);
        bt2 = findViewById(R.id.rb_two);
        bt3 = findViewById(R.id.rb_three);
        bt4 = findViewById(R.id.rb_four);
        init();
    }

    //设置按钮文字(四个)
    public void setTextList(String[] str){
        mTextList = str;
        bt1.setText(mTextList[0]);
        bt2.setText(mTextList[1]);
        bt3.setText(mTextList[2]);
        bt4.setText(mTextList[3]);
        initWidth();
    }

    private void init(){
        mBottomButton.setOnCheckedChangeListener(this);
    }

    //初始化宽度
    private void initWidth(){
        textWidth = (int) bt1.getPaint().measureText(mTextList[0]);
        final LayoutParams params = new LayoutParams(textWidth, ScreenUtil.dip2px(getContext(), 2));
        //获取单个按钮的宽度
        mBottomButton.post(new Runnable(){
            public void run(){
                viewWidth = mBottomButton.getWidth();
                params.leftMargin = (viewWidth / 4 - textWidth) / 2;
                line.setLayoutParams(params);
            }
        });
    }

    public void setViewPager(ViewPager viewPage){
        this.pager = viewPage;
        pager.addOnPageChangeListener(this);
    }

    int lastCheckedId = R.id.rb_one;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(lastCheckedId != checkedId && mListener != null){
            mListener.onTabChecked(checkedId);
            lastCheckedId = checkedId;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    @Override
    public void onPageSelected(int position) {
        this.currentPosition = position;
        if(position < 6){
            bt1.setChecked(true);
            upDateAnimation(0);
        } else if(position >= 6 && position < 12){
            bt2.setChecked(true);
            upDateAnimation(1);
        } else if(position >= 12 && position < 18){
            bt3.setChecked(true);
            upDateAnimation(2);
        } else{
            bt4.setChecked(true);
            upDateAnimation(3);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int nowTranslatePosition;//当前横线左侧所在x坐标
    /**
     * 更新横线的动画
     * @param index 当前选中按钮的下标
     */
    public void upDateAnimation(int index){
        //下次移动的x坐标(相对) = radiogroup / 控件的数量 * 选中控件位置的下标 + 一个item中文字到该item左侧的距离(此处没有加此项,因为上面已经为line设置了此项的marginleft)
        int nextTranslatePosition = viewWidth / mTextList.length * index;//点击或滑动后横线需要移动到的x坐标
        if(nowTranslatePosition != nextTranslatePosition){
            TranslateAnimation animation = new TranslateAnimation(nowTranslatePosition, nextTranslatePosition,0,0);
            animation.setFillAfter(true);
            animation.setDuration(animDuration);
            line.startAnimation(animation);
            nowTranslatePosition = nextTranslatePosition;
        }
    }
}
