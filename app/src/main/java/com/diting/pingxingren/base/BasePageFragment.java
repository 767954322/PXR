package com.diting.pingxingren.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diting.pingxingren.db.DTingDBUtil;
import com.diting.pingxingren.util.InstallationUtils;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 11, 16:11.
 * Description: .
 */

public abstract class BasePageFragment extends Fragment {

    protected Activity mActivity;
    protected boolean isInit = false;
    protected boolean isVisible = false;
    protected ViewDataBinding mViewBinding;
    protected String mUuid;
    protected DTingDBUtil mDTingDBUtil;
    protected Bundle mBundle;

    /**
     * 可见
     */
    protected void onVisible() {
        if (isInit) {
            init();
            isInit = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mUuid = InstallationUtils.getUuid(mActivity);
        mDTingDBUtil = DTingDBUtil.getInstance(mActivity);
        mBundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInit = true;
        if (needLazyLoad()) {
            if (isVisible)
                onVisible();
        } else {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible)
            onVisible();
    }

    public abstract int getLayoutID();

    protected boolean needLazyLoad() {
        return true;
    }

    protected void init() {
        initView();
        initData();
        initListener();
    }

    public void initView() {
    }

    public void initData() {
    }

    public void initListener() {
    }

    protected void startToActivity(Class<?> cls) {
        mActivity.startActivity(new Intent(mActivity, cls));
    }

    protected void startToActivity(Intent intent) {
        mActivity.startActivity(intent);
    }
}
