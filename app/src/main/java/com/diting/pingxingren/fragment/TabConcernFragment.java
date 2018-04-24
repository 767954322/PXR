package com.diting.pingxingren.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MyPagerAdapter;
import com.diting.pingxingren.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/2/22.
 * 沟通页面
 */

public class TabConcernFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> mTitleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concern_tab, null);
        initViews(view);
        setTabLayout();
        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        mTitleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        fragmentList.add(new NewRankListFragment());
        fragmentList.add(new NewFollowListFragment());
        fragmentList.add(new NewFansFragment());
        //fragmentList.add(new NearByFragment());
        mTitleList.add("价值排行");
        mTitleList.add("关注");
        mTitleList.add("粉丝");
        //mTitleList.add("附近");
    }

    private void setTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//系统默认模式、
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager(), mTitleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);//tablayout和viewpager关联起来
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSuccess(String message) {
        if (message.equals("gotoRankList")) {
            viewPager.setCurrentItem(0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
