package com.diting.pingxingren.custom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.QRCodeActivity;

public class PopWinSubmenu extends PopupWindow implements View.OnClickListener {
    private View mainView;
    private LinearLayout layout_robot;
    private LinearLayout layout_concern;
    private LinearLayout layout_knowledge;
    private LinearLayout layout_mime;
    private LinearLayout layout_mail;
    private Activity activity;

    public PopWinSubmenu(Activity paramActivity, int paramInt1, int paramInt2) {
        super(paramActivity);
        this.activity = paramActivity;
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_submenu, null);
        //平行人布局
        layout_robot = ((LinearLayout) mainView.findViewById(R.id.layout_robot));
        //关注布局
        layout_concern = ((LinearLayout) mainView.findViewById(R.id.layout_concern));
        //知识库布局
        layout_knowledge = (LinearLayout) mainView.findViewById(R.id.layout_knowledge);
        //个人中心布局
        layout_mime = (LinearLayout) mainView.findViewById(R.id.layout_mime);
        //站内消息布局
        layout_mail = (LinearLayout) mainView.findViewById(R.id.layout_mail);

        layout_robot.setOnClickListener(this);
        //设置每个子布局的事件监听器
        setContentView(mainView);
        //设置宽度  
        setWidth(paramInt1);
        //设置高度  
        setHeight(paramInt2);
        //设置显示隐藏动画  
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明  
        setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_robot:
                dismiss();
                activity.startActivity(new Intent(activity, QRCodeActivity.class));
                break;
            default:
                break;
        }
    }
}