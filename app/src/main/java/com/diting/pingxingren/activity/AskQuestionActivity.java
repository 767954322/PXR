package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.AskQuestionAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.AskListModel;
import com.diting.pingxingren.model.AskModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.AskListPageObserver;
import com.diting.pingxingren.util.MySharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 会问界面
 */
public class AskQuestionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TitleBarView titleBarView;
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AskQuestionAdapter mAdapter;
    private List<AskListModel> askListModelList=new ArrayList<>();
    private int moreCount = 1;
    private AskListModel changeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        EventBus.getDefault().register(this);
        initViews();
        initDate();
        initTitleView();
        initEvents();

    }

    @Override
    protected void initViews() {
        titleBarView = findViewById(R.id.title_bar);
        mSwipeRefreshWidget=findViewById(R.id.swipeLayout);
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new AskQuestionAdapter(R.layout.item_ask_question,askListModelList);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        mAdapter.setOnAskItemClickListener(new AskQuestionAdapter.onAskItemClickListener() {
            @Override
            public void onaskInfo(AskListModel askListModel) {
                changeModel=askListModel;
                startActivity(AddKnowledgeActivity.callingIntent(AskQuestionActivity.this, askListModel));
            }
        });
        mSwipeRefreshWidget.setOnRefreshListener(this);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAdapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
    }

    private void initTitleView() {
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("常见问题");
        titleBarView.setBtnRightText("");

    }

    private void initDate() {
        onRefresh();

    }
    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getAskList(moreCount, MySharedPreferences.getUniqueId(),new AskListPageObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            dismissLoadingDialog();
            if (result instanceof AskModel) {
//                emptyView.setVisibility(View.VISIBLE);
                AskModel askModel = (AskModel) result;

                if (moreCount == 1) {
                    mAdapter.setNewData(askModel.getItems());
                } else {
                    mAdapter.addData(askModel.getItems());
                    mAdapter.loadMoreComplete();
                }

                if (moreCount * 15 >= askModel.getTotal()) {
                    mAdapter.loadMoreEnd(true);
                }

                moreCount++;
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            mSwipeRefreshWidget.setRefreshing(false);
            if (o instanceof String)
                showShortToast((String) o);
        }
    };
    @Override
    public void onRefresh() {
        showLoadingDialog("请求中");
        mSwipeRefreshWidget.setRefreshing(false);
        moreCount = 1;
//        ApiManager.getAskList(new AskListObserver(mResultCallBack));
        ApiManager.getAskList(moreCount, MySharedPreferences.getUniqueId(),new AskListPageObserver(mResultCallBack));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSuccess(AskListModel askListModel  ) {
//        mAdapter.getData().remove(changeModel);
//        mAdapter.getData().add(askListModel);
        int postion=mAdapter.getData().indexOf(changeModel);
        mAdapter.remove(postion);
        mAdapter.getData().add(postion,askListModel);
        mAdapter.notifyDataSetChanged();
    }

}
