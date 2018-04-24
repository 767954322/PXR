package com.diting.pingxingren.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.diting.pingxingren.util.ScreenUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 01, 11:51.
 */

public class FitImageView extends AppCompatImageView {

    private int screenWidth;
    private int screenHeight;
    private int displayWidth;
    private int displayHeight;

    public FitImageView(Context context) {
        super(context);
    }

    public FitImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FitImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = ScreenUtil.getScreenWidth(getContext());
        screenHeight = ScreenUtil.getScreenHeight(getContext());
    }

    public void setSize(int bitmapWidth, int bitmapHeight) {
        displayWidth = screenWidth;
        displayHeight = screenHeight;

        //计算出按比例拉伸后的宽度和高度
        displayWidth = screenHeight * bitmapWidth / bitmapHeight;
        displayHeight = screenWidth * bitmapHeight / bitmapWidth;
        //判断如果以图片高度拉伸到屏幕的高度,按照相应的拉伸比是否能够拉伸超过屏幕宽度或者等于屏幕宽度,否则以另一边为基准

        if (displayWidth >= screenWidth) {
            displayHeight = screenHeight;
        } else {
            displayWidth = screenWidth;
        }

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        lp.leftMargin = (screenWidth - displayWidth) / 2;
        lp.topMargin = ((screenHeight - displayHeight) / 2);
        setLayoutParams(lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(displayWidth, displayHeight);
    }
}
