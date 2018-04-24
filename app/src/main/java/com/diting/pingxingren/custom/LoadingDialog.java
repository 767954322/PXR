package com.diting.pingxingren.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.diting.pingxingren.R;

/**
 * @author JunJie
 */
public class LoadingDialog extends Dialog {
    private TextView loading_text;

    public LoadingDialog(Context context) {
        super(context);
        setLoadingDialog();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setLoadingDialog();
    }

    protected LoadingDialog(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        // 加载自定义dialog的布局文件
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null);
        // 无标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局
        super.setContentView(dialog_view);

        loading_text = (TextView) dialog_view.findViewById(R.id.loading_text);
        this.setCancelable(false);
        // 点击dialog外侧不会消失
        this.setCanceledOnTouchOutside(false);
    }

    public void setLoadingText(String text) {
        loading_text.setText(text);
    }
}


