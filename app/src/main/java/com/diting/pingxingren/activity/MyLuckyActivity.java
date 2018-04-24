package com.diting.pingxingren.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MyLuckyAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.MyLuckyModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.MyLuckyObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的活动   抽奖   列表
 */
public class MyLuckyActivity extends BaseActivity {
    private List<MyLuckyModel> myLuckyModels = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TitleBarView titleBarView;
    private MyLuckyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lucky);
        initViews();
        initEvents();
        initDate();
    }

    @Override
    protected void initViews() {
        mAdapter = new MyLuckyAdapter(R.layout.item_my_lucky, myLuckyModels);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("我的活动");
        titleBarView.setBtnRightText("");

    }

    @Override
    protected void initEvents() {
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setOnLuckyImageListener(new MyLuckyAdapter.LuckyImageListener() {
            @Override
            public void toLuckyDetail(MyLuckyModel myLuckyModel) {
                Intent intent = new Intent(MyLuckyActivity.this, ShowLuckyWebActivity.class);
                intent.putExtra("luckyModel",myLuckyModel);
                startActivity(intent);

            }
        });

    }

    private void initDate() {
        ApiManager.getLuckyList(new MyLuckyObserver(new ResultCallBack() {
            @Override
            public void onResult(Object result) {

            }

            @Override
            public void onResult(List<?> resultList) {


                myLuckyModels = (List<MyLuckyModel>) resultList;
                mAdapter.setNewData(myLuckyModels);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object o) {
                dismissLoadingDialog();
                if (o instanceof String)
                    showShortToast((String) o);
//                ToastUtils.showShortToastSafe("请求失败!");
            }
        }));
    }


}
