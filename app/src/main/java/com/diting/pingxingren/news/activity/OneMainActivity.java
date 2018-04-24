package com.diting.pingxingren.news.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseFragmentActivity;
import com.diting.pingxingren.model.NewsListModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.NewsListObserver;
import com.diting.pingxingren.news.adapter.ViewPageAdapter;
import com.diting.pingxingren.news.custom.NewsMainBottomView;
import com.diting.pingxingren.news.custom.NewsTitleBar;
import com.diting.pingxingren.news.fragment.DetailsFragment;
import com.diting.pingxingren.news.listener.OnTabCheckedListener;
import com.diting.pingxingren.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2018 on 2018/1/4.
 * 每日新闻主页(最多24页，可从第一页滑动至最后一页
 */

public class OneMainActivity extends BaseFragmentActivity implements  NewsTitleBar.onTitleClickListener, OnTabCheckedListener, View.OnClickListener {

    private NewsMainBottomView bottomView;
    private int maxPage = 24;//最多可華東的頁數
    private ViewPager mViewPager;
    private ViewPageAdapter adapter;
    private ArrayList<Fragment> list;
    private List<NewsListModel> mList;
    private Bundle bundle;
    private RelativeLayout reload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main_one);
        initViews();
        initDatas();
        initListeners();
    }

    protected void initViews() {
//        mBottomButton = findViewById(R.id.radio_group);
        bottomView = findViewById(R.id.bottom_bt);
        mViewPager = findViewById(R.id.vp);
        reload = findViewById(R.id.rl_reload);
        reload.setOnClickListener(this);
        NewsTitleBar bar = findViewById(R.id.title_bar);
        bar.setTitle("头条新闻",R.drawable.news_title_icon,this);
        bar.showLine();
        bottomView.setTextList(StringUtil.getStringArray(R.array.news_main_tab_text));
        bottomView.setOnTabClickListener(this);
        bottomView.setViewPager(mViewPager);
    }

    protected void initDatas() {
        list = new ArrayList<>();
        request();
    }

    private void request(){
        showLoadingDialog(StringUtil.getString(R.string.loading));
        ApiManager.getNewsList(new NewsListObserver(new ResultCallBack() {
            @Override
            public void onResult(Object result) {
            }

            @Override
            public void onResult(List<?> resultList) {
                mList = (List<NewsListModel>) resultList;
                ArrayList<NewsListModel> itemModel = new ArrayList<>();
                for (int i = 0; i < maxPage; i++) {
                    if(bundle ==null){
                        bundle = new Bundle();
                        itemModel.addAll(mList);
                    }
                    bundle.putParcelableArrayList("list", itemModel);
                    list.add(DetailsFragment.getInstance(i, bundle));
                }
                setAdapter();
                dismissLoadingDialog();
            }

            @Override
            public void onError(Object o) {
                reload.setVerticalGravity(View.VISIBLE);
            }
        }));
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new ViewPageAdapter(getSupportFragmentManager(), list);
            mViewPager.setAdapter(adapter);
        }else{
            notifyAll();
        }
    }

    protected void initListeners() {
//        mBottomButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onBack(boolean isFinish) {
        finish();
    }

    @Override
    public void onSetting() {

    }

    @Override
    public void onShare() {

    }

    @Override
    public void onTabChecked(int id) {
        switch (id){
            case R.id.rb_one://综合
                if(bottomView.getCurrentPosition() > 5)
                    mViewPager.setCurrentItem(0,false);
                break;
            case R.id.rb_two://国内
                if(bottomView.getCurrentPosition() < 6 || bottomView.getCurrentPosition() > 11)
                    mViewPager.setCurrentItem(6,false);
                break;
            case R.id.rb_three://国际
                if(bottomView.getCurrentPosition() < 12 || bottomView.getCurrentPosition() > 17) {
                    mViewPager.setCurrentItem(12,false);
                }
                break;
            case R.id.rb_four://社会
                if(bottomView.getCurrentPosition() < 18)
                    mViewPager.setCurrentItem(18,false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_reload:
                reload.setVerticalGravity(View.GONE);
                request();
                break;
        }
    }
}
