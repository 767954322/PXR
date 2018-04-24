package com.diting.pingxingren.smarteditor.dialog;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.StringAdapter;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.dialog.DialogUtil;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 15:51.
 * Description: .
 */

public class MobileCategoryDialog extends DefaultDialog {

    private String[] mCategoryStrings;
    private List<String> mCategoryList;
    private StringAdapter mAdapter;

    public MobileCategoryDialog(Activity activity) {
        super(activity);
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_common_recycler, R.style.CustomDialog);
        mDialog.setCancelable(true);
        View titleView = mDialog.findViewById(R.id.titleLayout);
        titleView.setVisibility(View.GONE);
        RecyclerView recyclerView = mDialog.findViewById(R.id.commonRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecyclerViewDivider viewDivider = new RecyclerViewDivider(activity, RecyclerViewDivider.HORIZONTAL);
        viewDivider.setDividerColor(R.color.transparent);
        viewDivider.setDividerHeight(8);
        recyclerView.addItemDecoration(viewDivider);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.windowAnimations = R.style.Dialog_Animation;
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(activity) * 0.8);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(layoutParams);
        }

        mCategoryStrings = activity.getResources().getStringArray(R.array.article_classification);
        mCategoryList = new ArrayList<>(Arrays.asList(mCategoryStrings));
        mAdapter = new StringAdapter(mCategoryList, 22);
        recyclerView.setAdapter(mAdapter);
    }

    public void updateItems(String removeItem) {
        mCategoryList = new ArrayList<>(Arrays.asList(mCategoryStrings));
        if (mCategoryList.remove(removeItem)) {
            mAdapter.setNewData(mCategoryList);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        mAdapter.setClickListener(clickListener);
    }
}
