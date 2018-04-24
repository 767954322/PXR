package com.diting.pingxingren.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.ChatLogAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.ChatLogItemModel;
import com.diting.pingxingren.model.ChatLogModel;
import com.diting.pingxingren.model.ChatUserManageItemModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChatLogObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息记录
 * Created by asus on 2017/3/13.
 */

public class ChatLogActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TitleBarView titleBarView;
//    private LoadListView lv_chat_log;
//    private MessageAdapter adapter;
//    private List<Msg> msgList = new ArrayList<Msg>();

    private int moreCount = 1;
    private String uuid;

    private ChatUserManageItemModel mChatUserManageItemModel;

    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ChatLogAdapter mAdapter;
    List<ChatLogItemModel> msg = new ArrayList<ChatLogItemModel>();
    private View emptyView;
    private TextView tv_prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_log);
        initViews();
        initEvents();
        initDate();
    }

    private void initDate() {

        uuid = getIntent().getStringExtra("uuid");
        mChatUserManageItemModel = getIntent().getParcelableExtra("chatUserManageItemModel");
        mAdapter.setHeaderImage(mChatUserManageItemModel.getHeadImgUrl());

        onRefresh();
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("消息");
    }

    private void initTitleBarEvents() {
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
        mSwipeRefreshWidget = findViewById(R.id.swipeLayout);
        mRecyclerView = findViewById(R.id.chatRecycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatLogAdapter(this, R.layout.item_chat_log, msg);
        emptyView = getLayoutInflater().inflate(R.layout.empty, (ViewGroup) mRecyclerView.getParent(), false);
        tv_prompt = emptyView.findViewById(R.id.tv_tip);
        tv_prompt.setText(R.string.no_usermessage_date);
        mAdapter.setEmptyView(emptyView);
        emptyView.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mAdapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);


//        getChatLog(1,false);
    }

    public void getChatLog(final int pageNo, boolean isRefresh) {
//        if(pageNo==1&&!isRefresh){
//            showLoadingDialog("加载中");
//        }


        ApiManager.getChatLogList(moreCount, uuid, new ChatLogObserver(mResultCallBack));
//        Diting.getChatLog(pageNo,uuid, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                dismissLoadingDialog();
//                List<Msg> list = Utils.parseChatLog(response);
//                if(list== null){
//                    if(pageNo == 1){
//                        ll_no_knowledge.setVisibility(View.VISIBLE);
//                    }else {
//                        showShortToast("无更多数据");
//                        lv_chat_log.loadCompleted();
//                    }
//                }else {
//                    ll_no_knowledge.setVisibility(View.GONE);
//                    moreCount++;
//                    msgList.addAll(list);
//                    adapter.notifyDataSetChanged();
//                    lv_chat_log.reflashComplete();
//                    lv_chat_log.loadCompleted();
//                    if(pageNo == 1){
//                        lv_chat_log.setSelection(0);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                dismissLoadingDialog();
//                if (error.networkResponse == null) {
//                    showShortToast("请求超时！");
//                }
//            }
//        });
    }

    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getChatLogList(moreCount, uuid, new ChatLogObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            dismissLoadingDialog();
            if (result instanceof ChatLogModel) {
                emptyView.setVisibility(View.VISIBLE);
                ChatLogModel chatLogModel = (ChatLogModel) result;

                if (moreCount == 1) {
                    mAdapter.setNewData(chatLogModel.getItems());
                } else {
                    mAdapter.addData(chatLogModel.getItems());
                    mAdapter.loadMoreComplete();
                }

                if (moreCount * 15 >= chatLogModel.getTotal()) {
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
            if (o instanceof String)
                showShortToast((String) o);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        showLoadingDialog("请求中");
        mSwipeRefreshWidget.setRefreshing(false);
        moreCount = 1;
        ApiManager.getChatLogList(moreCount, uuid, new ChatLogObserver(mResultCallBack));

    }
}
