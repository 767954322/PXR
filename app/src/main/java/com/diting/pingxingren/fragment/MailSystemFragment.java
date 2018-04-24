package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.MailDetailActivity;
import com.diting.pingxingren.adapter.SystemMailAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.model.SystemMessageItemModel;
import com.diting.pingxingren.model.SystemMessageModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.SystemMessageObserver;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/1/6.
 */

public class MailSystemFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, SystemMailAdapter.MailDetailListener {

    private SystemMailAdapter adapter;
    private List<SystemMessageItemModel> mailList = new ArrayList<>();
    private LinearLayout ll_no_mail;
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private View emptyView;
    private TextView tv_prompt;

    private int count = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mail, null);
        initViews(view);
        initEvents();
        initDate();
        return view;
    }

    private void initDate() {
        onRefresh();
    }

    private void initViews(View view) {


        mSwipeRefreshWidget = view.findViewById(R.id.swipe_refresh_widget_sysmail);
        mRecyclerView = view.findViewById(R.id.recycle_view_sysmail);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SystemMailAdapter(R.layout.mail_item, mailList);
        mRecyclerView.setAdapter(adapter);
        emptyView = getLayoutInflater().inflate(R.layout.empty, (ViewGroup) mRecyclerView.getParent(), false);
        tv_prompt = emptyView.findViewById(R.id.tv_tip);
        tv_prompt.setText(R.string.no_system_date);
        adapter.setEmptyView(emptyView);


    }

    private void initEvents() {
        adapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        adapter.setMailDetailListener(this);
    }


    private void changeMailState(final SystemMessageItemModel systemMessageItemModel) {
        showLoadingDialog("加载中");

        ApiManager.systemIsRead(systemMessageItemModel.getId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result.toString());
//                    dismissLoadingDialog();
//                    if (jsonObject.getString("message").equals("保存成功！")) {
                dismissLoadingDialog();
                EventBus.getDefault().post("readed");
                MyApplication.unreadLetterNum--;
                if (!Utils.hasUnreadMail()) {
                    EventBus.getDefault().post("hideRedPoint");
                }


                gotoMailDetail(systemMessageItemModel);
                systemMessageItemModel.setIdRead(true);
                adapter.setShowPoint(true);
                adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {
                if (o instanceof String)
                    showShortToast((String) o);
            }
        });

    }

    private void gotoMailDetail(final SystemMessageItemModel systemMessageItemModel) {
        Intent intent = new Intent(getActivity(), MailDetailActivity.class);
        intent.putExtra("from", "MailSystemFragment");
        intent.putExtra("System", systemMessageItemModel);
        startActivity(intent);
    }

    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getSystemMessageList(count, new SystemMessageObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            dismissLoadingDialog();
            if (result instanceof SystemMessageModel) {
                SystemMessageModel systemMessageModel = (SystemMessageModel) result;
                if (count == 1) {
                    adapter.setNewData(systemMessageModel.getItems());
                } else {
                    adapter.addData(systemMessageModel.getItems());
                    adapter.loadMoreComplete();
                }

                if (count * 15 >= systemMessageModel.getTotal()) {
                    adapter.loadMoreEnd(true);
                }

                count++;
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
//            ToastUtils.showShortToastSafe("请求失败!");
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
        count = 1;
        ApiManager.getSystemMessageList(count, new SystemMessageObserver(mResultCallBack));
    }


    @Override
    public void gotoSysMailDetail(SystemMessageItemModel systemMessageItemModel) {
        changeMailState(systemMessageItemModel);
    }
}
