package com.diting.pingxingren.dialog;

import android.app.Activity;
import android.app.Dialog;

/**
 * Creator: Gu FanFan.
 * Date: 2017/11/1, 10:26.
 */

public class DefaultDialog {

    Dialog mDialog;
    Activity mActivity;

    DefaultDialog(Activity activity) {
        mActivity = activity;
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }
}
