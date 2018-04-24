package com.diting.pingxingren.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.Utils;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.List;

/**
 * Created by MQ on 2017/5/8.
 */

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<RobotConcern> mBeans;
    private static final int dividerHeight = 80;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String[] industries;

    public void setDatas(List<RobotConcern> mBeans) {
        this.mBeans = mBeans;
    }


    public CustomItemDecoration(Context mContext) {
        this.mContext = mContext;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        industries = mContext.getResources().getStringArray(R.array.industry_list);
    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        canvas.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
                continue;
            }
            if (position == 0) {
                //第一条数据有bar
                drawTitleBar(canvas, parent, child, mBeans.get(position), position);
            } else if (position > 0) {
                //与上一条数据中的tag不同时，该显示bar了
                if (!(mBeans.get(position).getIndustry() == mBeans.get(position - 1).getIndustry())) {
                    drawTitleBar(canvas, parent, child, mBeans.get(position), position);
                }
            }
        }
        canvas.restore();
    }

    /**
     * 绘制bar
     *
     * @param canvas Canvas
     * @param parent RecyclerView
     * @param child  ItemView
     */
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, RobotConcern bean, int position) {
        final int left = 0;
        final int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int top = mBounds.top;
        final int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + dividerHeight;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(left, top, right, bottom, mPaint);
        //根据位置不断变换Paint的颜色
        //ColorUtil.setPaintColor(mPaint, position);
        mPaint.setTextSize(40);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(industries[bean.getIndustry()], 20, bottom - dividerHeight / 3, mPaint);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        //用来绘制悬浮框
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
            return;
        }
        final int bottom = parent.getPaddingTop() + dividerHeight;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + dividerHeight, mPaint);
        //ColorUtil.setPaintColor(mPaint, tagsStr.indexOf(mBeans.get(position).getIndexTag()));
        mPaint.setTextSize(40);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(industries[mBeans.get(position).getIndustry()], 20, bottom - dividerHeight / 3, mPaint);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (mBeans == null || mBeans.size() == 0 || mBeans.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (position == 0) {
            //第一条数据有bar
            outRect.set(0, dividerHeight, 0, 0);
        } else if (position > 0) {
            //与上一条数据中的tag不同时，该显示bar了
            if (!(mBeans.get(position).getIndustry() == mBeans.get(position - 1).getIndustry())) {
                outRect.set(0, dividerHeight, 0, 0);
            }
        }
    }

}
