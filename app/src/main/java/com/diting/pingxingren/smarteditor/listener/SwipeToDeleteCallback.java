package com.diting.pingxingren.smarteditor.listener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.diting.pingxingren.R;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 31, 17:33.
 * Description: .
 */

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private ColorDrawable mColorDrawable;
    private Drawable mDeleteDrawable;

    public SwipeToDeleteCallback(Context context, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        mDeleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_white_delete);
        mColorDrawable = new ColorDrawable();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int itemViewHeight = itemView.getBottom() - itemView.getTop();

        mColorDrawable.setColor(Color.parseColor("#F44336"));
        mColorDrawable.setBounds((int) (itemView.getRight() + dX), itemView.getTop(),
                itemView.getRight(), itemView.getBottom());
        mColorDrawable.draw(c);

        int deleteIconTop = itemView.getTop() + (itemViewHeight
                - mDeleteDrawable.getIntrinsicHeight()) / 2;
        int deleteIconMargin = (itemViewHeight - mDeleteDrawable.getIntrinsicHeight()) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - mDeleteDrawable.getIntrinsicWidth();
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + mDeleteDrawable.getIntrinsicHeight();
        mDeleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        mDeleteDrawable.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
