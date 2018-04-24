package com.diting.pingxingren.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.custom.LoadingDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by asus on 2017/9/22.
 * <p>
 * Update by 2017-11-10 11:10.
 * Update by user Gu Fan Fan.
 */

public class BaseFragmentActivity extends FragmentActivity {

    private MyApplication mMyApplication;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mMyApplication = MyApplication.getInstance();
        mMyApplication.addActivity(this);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyApplication.removeActivity(this);
    }

    protected void initView() {
        initTitleView();
    }

    protected void initListener() {
    }

    protected void initData() {

    }

    public void showLoading() {
    }

    public void dismissLoading() {
    }

    private void initTitleView() {

    }

    /**
     * 默认退出
     **/
    protected void defaultFinish() {
        super.finish();
    }

    protected void showLoadingDialog(String text) {
        dialog = new LoadingDialog(this, R.style.mdialog);
        dialog.setLoadingText(text);
        dialog.show();
    }

    protected void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
