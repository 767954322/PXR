package com.diting.pingxingren.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;

/**
 * 此对话框用于了机器人设置的问号点击
 * Created by Administrator on 2017/11/18.
 */

public class MyCustomDialog extends Dialog {
    private TextView tv_title;//设置对话框的title
    private TextView tv_content;// 设置对话框的内容
    private TextView tv_content_tip_first;//设置对话框tip的第一个字
    private LinearLayout ll_ok;
    private TextView tv_ok;
    private TextView tv_content_tip_Second;//设置设置对话框的提示内容


    private String titleStr;
    private String concentStr;
    private String content_tipFirstStr;
    private String content_tipSecondStr;
    private String yesStr;


    private int tipFirstisShow;//设置提tip的第一个字是否显示


    public onOkOnclickListener okOnclickListener;//确定按钮被点击了的监听器

    public MyCustomDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_my_coustom);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
        //初始化界面控件
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content_tip_first = (TextView) findViewById(R.id.tv_content_tip_first);
        tv_content_tip_Second = (TextView) findViewById(R.id.tv_content_tip_Second);
        ll_ok = (LinearLayout) findViewById(R.id.ll_ok);
        tv_ok = (TextView) findViewById(R.id.tv_ok);

        tv_content_tip_Second.setVisibility(tipFirstisShow);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

        //如果用户自定了title和message
        if (titleStr != null) {
            tv_title.setText(titleStr);
        }
        if (concentStr != null) {
            tv_content.setText(concentStr);
        }
        //如果设置按钮的文字
        if (content_tipFirstStr != null) {
            tv_content_tip_first.setText(content_tipFirstStr);
        }

        if (content_tipSecondStr != null) {
            tv_content_tip_Second.setText(content_tipSecondStr);
        }


        //如果设置按钮的文字
        if (yesStr != null) {
            tv_ok.setText(yesStr);
        }


    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okOnclickListener != null) {
                    okOnclickListener.onOkClick();
                }
            }
        });

    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onOkOnclickListener {
        public void onOkClick();
    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setOkOnclickListener(String str, onOkOnclickListener onOkOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.okOnclickListener = onOkOnclickListener;
    }


    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setConcentStr(String concentStr) {
        this.concentStr = concentStr;
    }

    public void setContent_tipFirstStr(String content_tipFirstStr) {
        this.content_tipFirstStr = content_tipFirstStr;
    }

    public void setContent_tipSecondStr(String content_tipSecondStr) {
        this.content_tipSecondStr = content_tipSecondStr;
    }

    public void setTipFirstisShow(int tipFirstisShow) {
        this.tipFirstisShow = tipFirstisShow;
    }

}
