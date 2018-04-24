package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MyPagerAdapter;
import com.diting.pingxingren.base.BaseFragmentActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.fragment.WechatLoginFragment;
import com.diting.pingxingren.fragment.WechatRegisterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/1/13.
 */

public class WechatBindActivity extends BaseFragmentActivity {
    private TitleBarView titleBarView;
    //private FragmentTabHost mTabHost;
    private Bundle bundle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> mTitleList;
    private WechatLoginFragment loginFragment;
    private WechatRegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wechat_bind);
        bundle = getIntent().getBundleExtra("wechat");
        initViews();
        initEvents();
        setTabLayout();
    }

    private void initTitleBarView(){
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText("绑定手机号");
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initViews() {
        initTitleBarView();
//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//        mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator("已有账号"), WechatLoginFragment.class, bundle);
//        mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator("没有账号"), WechatRegisterFragment.class, bundle);
        tabLayout =   findViewById(R.id.tab_layout);
        viewPager =   findViewById(R.id.view_pager);
        mTitleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        loginFragment = new WechatLoginFragment();
        loginFragment.setArguments(bundle);
        registerFragment = new WechatRegisterFragment();
        registerFragment.setArguments(bundle);
        fragmentList.add(loginFragment);
        fragmentList.add(registerFragment);
        mTitleList.add("已有账号");
        mTitleList.add("没有账号");
    }

    protected void initEvents() {
        initTitleBarEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mTabHost = null;
    }

    private void setTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//系统默认模式、
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
//        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));


        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mTitleList,fragmentList );
        //viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);//tablayout和viewpager关联起来
        //tabLayout.setTabsFromPagerAdapter(mAdapter);//给TabLayout设置适配器

    }
}
