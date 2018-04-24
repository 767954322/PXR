package com.diting.pingxingren.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 10, 11:20.
 * Description: .
 */

@SuppressLint("Registered")
public class BaseChangeFragments extends BaseFragmentActivity {

    public static final String FRAGMENT_TAG_CHATTING = "chatting";
    protected static final String FRAGMENT_TAG_COMMUNICATION = "communication";
    protected static final String FRAGMENT_TAG_KNOWLEDGE = "knowledge";
    protected static final String FRAGMENT_TAG_MINE = "mine";
    protected static final String FRAGMENT_TAG_HOME = "home";

    protected FragmentManager mFragmentManager;
    protected FragmentTransaction mFragmentTransaction;

    protected String mCurrentFragmentTag;
    protected Map<String, BaseFragment> mapFragments = new HashMap<String, BaseFragment>();

    protected FragmentController fragmentController;

    @Override
    protected void initView() {
        super.initView();
        fragmentController = new FragmentController(this);
        mFragmentManager = getSupportFragmentManager();
    }

    protected void add(boolean isShow, String tag, int resourceId, Fragment fragment) {
        mCurrentFragmentTag = tag;
        fragmentController.add(isShow, tag, resourceId, fragment);
    }

    protected FragmentController getFragmentController() {
        return fragmentController;
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment) {
        mCurrentFragmentTag = tag;
        fragmentController.changeFragment(tag, resourceId, targetFragment);
    }

    public void changeFragment(String tag) {
        fragmentController.changeFragment(tag);
    }

    public void changeFragment(String tag, int resourceId, Fragment targetFragment, int HOMEPUSH) {
        mCurrentFragmentTag = tag;
        fragmentController.changeFragment(tag, resourceId, targetFragment, HOMEPUSH);
    }

    public void changeFragment(String tag, int HOMEPUSH) {
        // mCurrentFragmentTag = tag;
        fragmentController.changeFragment(tag, HOMEPUSH);
    }

    public void hideFragment(String tag) {
        fragmentController.hideFragment(tag);

    }

    public void hideFragment(Fragment fragment) {
        fragmentController.hideFragment(fragment);
    }

    public void showFragment(Fragment fragment) {
        fragmentController.showFragment(fragment);
    }

    public void showFragment(String tag) {
        fragmentController.showFragment(tag);
    }

    public void removeFragment(String tag) {
        fragmentController.removeFragment(tag);
    }

    protected boolean isFragmentExist(String tag) {
        return fragmentController.isFragmentExist(tag);
    }

    public Fragment getFragment(String tag) {
        return fragmentController.getFragment(tag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
        Fragment f = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
    }
}
