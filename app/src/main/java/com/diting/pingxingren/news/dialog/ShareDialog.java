package com.diting.pingxingren.news.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.ToastUtils;

/**
 * Created by 2018 on 2018/1/5.
 * 分享对话框
 */

public class ShareDialog extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_share_dialog);
        initWindow();
        initViews();
    }

    @SuppressWarnings("deprecation")
    private void initWindow() {
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        android.view.WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = display.getWidth() * 1;
        lp.rotationAnimation = R.anim.umeng_socialize_shareboard_animation_out;
        getWindow().setAttributes(lp);
    }

    private void initViews() {
        RelativeLayout layout = findViewById(R.id.rl_layout);
        Button mFriendCircle = findViewById(R.id.frined_circle);
        Button mWeChatFriend = findViewById(R.id.wechat_friend);
        Button mQZone = findViewById(R.id.qq_zone);
        Button mQQFriend = findViewById(R.id.qq_friend);
        Button mCopy = findViewById(R.id.copy_net);
        Button mCancel = findViewById(R.id.bt_cancel);
        layout.setOnClickListener(this);
        mFriendCircle.setOnClickListener(this);
        mWeChatFriend.setOnClickListener(this);
        mQZone.setOnClickListener(this);
        mQQFriend.setOnClickListener(this);
        mCopy.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frined_circle:
                break;
            case R.id.wechat_friend:
                break;
            case R.id.qq_zone:
                break;
            case R.id.qq_friend:
                break;
            case R.id.copy_net:
                ToastUtils.showShortToastSafe(R.string.link_really_copied);
                finish();
                break;
            case R.id.bt_cancel:
            case R.id.rl_layout:
                finish();
//                overridePendingTransition(0, R.anim.umeng_socialize_slide_out_from_bottom);
                break;
        }
    }
}
