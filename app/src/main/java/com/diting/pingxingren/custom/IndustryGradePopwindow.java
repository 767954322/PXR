package com.diting.pingxingren.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.wheelview.WheelView;
import com.diting.pingxingren.custom.wheelview.adapters.ArrayWheelAdapter;
import com.diting.pingxingren.util.BirthdayUtils;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import java.util.Calendar;

import static com.diting.pingxingren.util.ScreenUtil.getScreenHeight;

/**
 * 机器人设置界面的行业等级选择
 * Created by Administrator on 2017/11/17.
 */

public class IndustryGradePopwindow implements View.OnClickListener {
    private Context mCtx;
    //弹出框
    private PopupWindow mPopupWindow;
    //弹出view
    private View mPopupWindowView;
    //依赖view，在他下面
    private View mRootView;
    //取消
    private TextView tv_cancle;
    //确定
    private TextView tv_resure;
    //
    private TextView tv_select_title;

    //年月日的列表
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    private int[] mLocation = new int[2];
    private int mStartY;
    private int mScreenHeight;
    private String[] industryGrade = new String[]{"普通", "精英", "专家", "教授"};

    public IndustryGradePopwindow(Context context, View mRootView) {
        mCtx = context;
        this.mRootView = mRootView;
        initPopWindow();

    }

    private void initPopWindow() {

        initView();
        //获取按键的位置
        mRootView.getLocationOnScreen(mLocation);
        mStartY = mLocation[1] + mRootView.getHeight();
        mScreenHeight = getScreenHeight((Activity) mCtx);

    }

    private void initView() {
        mPopupWindowView = View.inflate(mCtx, R.layout.popwindow_select_birthday, null);
        tv_cancle = (TextView) mPopupWindowView.findViewById(R.id.tv_cancle);
        tv_resure = (TextView) mPopupWindowView.findViewById(R.id.tv_resure);
        tv_select_title = (TextView) mPopupWindowView.findViewById(R.id.tv_select_title);
        wv_year = (WheelView) mPopupWindowView.findViewById(R.id.wv_year);
        wv_month = (WheelView) mPopupWindowView.findViewById(R.id.wv_month);
        wv_day = (WheelView) mPopupWindowView.findViewById(R.id.wv_day);
        wv_month.setVisibility(View.GONE);
        wv_day.setVisibility(View.GONE);
        wv_year.setVisibleItems(5);


        wv_year.setCurrentItem(0);


        tv_select_title.setText("行业等级");
        addListener();

    }

    private void addListener() {
        tv_cancle.setOnClickListener(this);
        tv_resure.setOnClickListener(this);


    }

    public void show(String gradeLevel) {
        if (mPopupWindow == null) {
            initPopWindow();

            //设置弹框的大小  弹框位置在按钮以下，占据所有屏幕
            mPopupWindow = new PopupWindow(mPopupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x22000000));
            //mPopupWindow.setAnimationStyle(R.style.AnimationFade);
//            mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            wv_year.setViewAdapter(new ArrayWheelAdapter<String>(mCtx, industryGrade));// 设置"年"的显示数据
            wv_year.setCyclic(false);// 可循环滚动
            if (Utils.isEmpty(gradeLevel)) {
                wv_year.setCurrentItem(0);// 初始化时显示的数据
            } else {
                for (int i = 0; i < industryGrade.length; i++) {//普通for
                    if (gradeLevel.equals(industryGrade[i])) {
                        wv_year.setCurrentItem(i);
                        break;
                    }
                }
            }
        }

        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            //设置弹框的位置
            mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
        }
    }

    public int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle://取消
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_resure://确定
                onInGradeResultListener.onIngradeResult(industryGrade[wv_year.getCurrentItem()]);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                break;


        }
    }

    public interface OnInGradeResultListener {
        void onIngradeResult(String inGradeStr);
    }

    private OnInGradeResultListener onInGradeResultListener;

    public void setOnTimeResultListener(OnInGradeResultListener onInGradeResultListener) {
        this.onInGradeResultListener = onInGradeResultListener;
    }
}
