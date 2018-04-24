package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MyPagerAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.fragment.MailRobotFragment;
import com.diting.pingxingren.fragment.MailSystemFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/1/13.
 */

public class MailNewActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> mTitleList;
    private TitleBarView titleBarView;
    private View point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wechat_bind);
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
        setTabLayout();
        if(getIntent().getStringExtra("from") != null){
            viewPager.setCurrentItem(1);
        }
    }

    private void initTitleBarView(){
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setTitleText(R.string.mail);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        tabLayout =   findViewById(R.id.tab_layout);
        viewPager =   findViewById(R.id.view_pager);
        point = findViewById(R.id.red_point);
        if(MyApplication.unreadMessageNum > 0){
            point.setVisibility(View.VISIBLE);
        }
        mTitleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        mTitleList.add("系统消息");
        mTitleList.add("私信消息");
        fragmentList.add(new MailSystemFragment());
        fragmentList.add(new MailRobotFragment());
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mailReaded(String message) {
        if(message.equals("NoRobotMail")){
            point.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
