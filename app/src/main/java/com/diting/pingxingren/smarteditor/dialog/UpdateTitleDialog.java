package com.diting.pingxingren.smarteditor.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.diting.pingxingren.R;
import com.diting.pingxingren.dialog.DialogUtil;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.util.ScreenUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 14:42.
 * Description: .
 */

public class UpdateTitleDialog extends DefaultDialog implements View.OnClickListener {

    private EditText mEditText;
    private ClickListener mClickListener;

    public UpdateTitleDialog(Activity activity, ClickListener clickListener) {
        super(activity);
        mClickListener = clickListener;
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_update_article_layout, R.style.CustomDialog);
        mDialog.setCancelable(false);
        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.windowAnimations = R.style.Dialog_Animation;
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(activity) * 0.8);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(layoutParams);
        }

        mEditText = mDialog.findViewById(R.id.etTitle);
        Button enterButton = mDialog.findViewById(R.id.bt_enter);
        enterButton.setOnClickListener(this);
        Button cancelButton = mDialog.findViewById(R.id.bt_cancel);
        cancelButton.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mEditText.setText(title);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.bt_enter:
                mClickListener.onClick(v, mEditText.getText().toString());
                break;
            case R.id.bt_cancel:
                dismissDialog();
                break;
        }
    }
}
