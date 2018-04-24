package com.diting.pingxingren.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> mTitleList;
    private Context context;

    public MyPagerAdapter(FragmentManager fm, List<String> mTitleList, List<Fragment> fragmentList) {
        super(fm);
        this.mTitleList = mTitleList;
        this.fragmentList = fragmentList;
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    /**
     *
     * Title
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}