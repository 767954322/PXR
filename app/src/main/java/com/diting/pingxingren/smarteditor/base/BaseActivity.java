package com.diting.pingxingren.smarteditor.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.custom.LoadingDialog;
import com.diting.pingxingren.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 08, 15:53.
 * Description: .
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;
    protected Context mContext;
    protected Activity mActivity;
    protected Resources mResources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        MyApplication.getInstance().addActivity(this);

        mContext = this;
        mActivity = this;
        mResources = getResources();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClassName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClassName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected String getClassName() {
        return this.getClass().getName();
    }

    protected void showShortToast(String text) {
        ToastUtils.showShortToastSafe(text);
    }

    protected void showShortToast(int resId) {
        ToastUtils.showShortToastSafe(resId);
    }

    protected void showLongToast(String text) {
        ToastUtils.showLongToastSafe(text);
    }

    protected void showLongToast(int resId) {
        ToastUtils.showLongToastSafe(resId);
    }

    protected void startActivity(Class<?> aClass) {
        startActivity(aClass, null);
    }

    protected void startActivity(Class<?> aClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, aClass);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> aClass, int requestCode) {
        startActivityForResult(aClass, null, requestCode);
    }

    protected void startActivityForResult(Class<?> aClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, aClass);
        if (bundle != null) intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    protected void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(this, R.style.mdialog);
        mLoadingDialog.show();
    }

    protected void showLoading(String loadText) {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(this, R.style.mdialog);
        mLoadingDialog.setLoadingText(loadText);
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }
}
