package com.diting.pingxingren.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.Utils;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class MyDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private EditText contentEdit;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private String contentText;  //外界设置的编辑框文本
    private int contentLength; //设置文本编辑框的最大输入字符数
    private int inputType=InputType.TYPE_CLASS_TEXT;// 设置输入框的输入的类型

    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private boolean isContent = false;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        contentEdit.setRawInputType(inputType);
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
        if (contentLength != 0) {
            contentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(contentLength)});

        }

        if (isContent) {
            messageTv.setVisibility(View.GONE);
            contentEdit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(contentText)) {
                contentEdit.setText(contentText);
                contentEdit.setSelection(contentText.length());
            }
        } else {
            messageTv.setVisibility(View.VISIBLE);
            contentEdit.setVisibility(View.GONE);
        }

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes =  findViewById(R.id.yes);
        no = findViewById(R.id.no);
        titleTv =  findViewById(R.id.title);
        messageTv =  findViewById(R.id.message);
        contentEdit =  findViewById(R.id.et_content);

        if (inputType == 0) {
            contentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            contentEdit.setInputType(inputType);
        }
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public void setIsContent(boolean content) {
        isContent = content;
    }

    public String getContentText() {
        return contentEdit.getText().toString();
    }

    public void setContentText(String contentText) {
        this.contentText = !Utils.isEmpty(contentText) && contentText.equals("0") ? "" : contentText;
    }

    public void setContentLength(int length) {
        contentLength = length;
    }


    public void setContentEditInputType(int editinputType) {
inputType=editinputType;
    }


}
