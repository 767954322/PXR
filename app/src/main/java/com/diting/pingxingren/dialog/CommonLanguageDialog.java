package com.diting.pingxingren.dialog;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.CommonLanguageAdapter;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.util.ScreenUtil;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 05, 15:11.
 * Description: .
 */

public class CommonLanguageDialog extends DefaultDialog {

    private CommonLanguageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private View mAddLanguageView;
    private CommonLanguageAdapter.ItemClickListener mItemClickListener;

    public CommonLanguageDialog(Activity activity) {
        super(activity);
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_common_language_dialog, R.style.CustomDialog);
        mDialog.setCancelable(true);
        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            layoutParams.width = ScreenUtil.getScreenWidth(activity);
            layoutParams.height = ScreenUtil.dip2px(activity, 300);
            dialogWindow.setAttributes(layoutParams);
        }
        mRecyclerView = mDialog.findViewById(R.id.commonRecycler);
        mAddLanguageView = mDialog.findViewById(R.id.tv_add_common_language);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        RecyclerViewDivider viewDivider = new RecyclerViewDivider(mActivity, RecyclerViewDivider.HORIZONTAL);
        viewDivider.setDividerHeight(2);
        viewDivider.setDividerColor(R.color.transparent);
        mRecyclerView.addItemDecoration(viewDivider);
        mAddLanguageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.add();
            }
        });
    }
    public void setAddViewisShow(){
        mAddLanguageView.setVisibility(View.GONE);
        mAdapter.setCanDelete(false);
    }

    public void setAdapterData(List<CommonLanguageListModel> languageModels) {
        if (languageModels != null) {
            mAdapter = new CommonLanguageAdapter(R.layout.item_industrys, languageModels);
            mAdapter.setCanDelete(false);
        } else {
            mAdapter = new CommonLanguageAdapter(R.layout.item_industrys);
            mAdapter.setCanDelete(true);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setListener(CommonLanguageAdapter.ItemClickListener listener) {
        mItemClickListener = listener;
        mAdapter.setListener(mItemClickListener);
    }

    public void updateData() {
        mAdapter.setNewData(MyApplication.sCommonLanguages);
        mAdapter.setCanDelete(true);
        showDialog();
    }

    public void updateData(List<CommonLanguageListModel> languageModels) {
        mAdapter.setNewData(languageModels);
        showDialog();
    }
}
