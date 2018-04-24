package com.diting.pingxingren.smarteditor.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.dialog.DialogUtil;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.ScreenUtil;

/**
 * 确定取消提示会话框
 * Created by 2018 on 2018/1/15.
 */

public class UsualAlertDialog extends DefaultDialog implements View.OnClickListener {

    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ClickListener mClickListener;

    public UsualAlertDialog(Activity activity) {
        super(activity);
        mDialog = DialogUtil.createDialog(activity, R.layout.dialog_usual_alert, R.style.CustomDialog);
        mDialog.setCancelable(false);

        Button ok = mDialog.findViewById(R.id.bt_enter);
        Button cancel = mDialog.findViewById(R.id.bt_cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mTitleTextView = mDialog.findViewById(R.id.dialog_title);
        mContentTextView = mDialog.findViewById(R.id.tv_content);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(activity) * 0.8);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(layoutParams);
        }
    }

    public void setCancelable(boolean flag) {
        if (mDialog != null) mDialog.setCancelable(flag);
    }

    public void setTitle(String title) {
        if (StringUtil.isNotEmpty(title)) {
            mTitleTextView.setText(title);
        }
    }

    public void setContent(String content) {
        if (StringUtil.isNotEmpty(content)) {
            mContentTextView.setText(content);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_enter:
                if (mClickListener != null)
                    mClickListener.onClick(v, "OK");
                break;
            case R.id.bt_cancel:
                dismissDialog();
                break;
        }
    }
}
