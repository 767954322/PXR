package com.diting.pingxingren.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.ScreenUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2017/11/1, 10:30.
 */

public class DialogUtil {

    public static Dialog createDialog(Context context,
                                      View view, int themeId, boolean customizeParams) {
        return initDialog(context, view, themeId, customizeParams);
    }

    public static Dialog createDialog(Context context,
                                      View view, int themeId,WindowManager.LayoutParams layoutParams) {
        return initDialog(context, view, themeId, layoutParams);
    }

    public static Dialog createDialog(Context context,
                                      int layoutId, int themeId) {
        View view = View.inflate(context, layoutId, null);
        return initDialog(context, view, themeId, false);
    }

    private static Dialog initDialog(Context context, View view,
                                     int themeId, boolean customizeParams) {
        Dialog dialog = new Dialog(context, themeId != -1 ? themeId : R.style.CustomDialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null && customizeParams) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(context) * 0.8);
            layoutParams.height = (int) (layoutParams.width * 2.8 / 4);
            dialogWindow.setAttributes(layoutParams);
        }
        return dialog;
    }

    private static Dialog initDialog(Context context, View view,
                                     int themeId, WindowManager.LayoutParams layoutParams) {
        Dialog dialog = new Dialog(context, themeId != -1 ? themeId : R.style.CustomDialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null && layoutParams != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(context));
            layoutParams.height = (int) (layoutParams.width * 2.8 / 4);
            dialogWindow.setAttributes(layoutParams);
        }
        return dialog;
    }

    public static Dialog showLanguagePrompt(Context context, final ResultCallBack resultCallBack) {
        final Dialog dialog = createDialog(context, R.layout.layout_language_prompt, R.style.CustomDialog);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = (int) (ScreenUtil.getScreenWidth(context) * 0.8);
            layoutParams.height = layoutParams.width / 4;
            dialogWindow.setAttributes(layoutParams);
        }

        RadioGroup radioGroup = dialog.findViewById(R.id.languageRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dialog.dismiss();
                if (checkedId == R.id.languageEveryone) {
                    resultCallBack.onResult("0");
                } else {
                    resultCallBack.onResult("1");
                }
            }
        });
        return dialog;
    }

    public interface ResultCallBack {
        void onResult(Object o);
    }
}
