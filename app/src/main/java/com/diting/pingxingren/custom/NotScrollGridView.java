package com.diting.pingxingren.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/27, 11:29.
 */
public class NotScrollGridView extends GridView {
    public NotScrollGridView(Context context) {
        super(context);
    }

    public NotScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
