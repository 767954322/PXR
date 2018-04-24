package com.diting.pingxingren.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.RobotDetailActivity;
import com.diting.pingxingren.adapter.NewCommunicateAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.entity.CommonLanguage;
import com.diting.pingxingren.entity.CommunicateBean;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.FollowListObserver;
import com.diting.pingxingren.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class NewFollowListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewCommunicateAdapter.AttentionListener {

    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private NewCommunicateAdapter mAdapter;

    private List<CommunicateModel> communicateBeanList = new ArrayList<CommunicateModel>();

    private Button btn_concern;
    private View emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_concern, null);
        initView(view);
        initData();
        initEvents();
        return view;
    }


    private void initView(View view) {

        mSwipeRefreshWidget = view.findViewById(R.id.swipe_refresh_widget_myRank);
        mRecyclerView = view.findViewById(R.id.recycle_view_myRank);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewCommunicateAdapter(R.layout.item_communicate, communicateBeanList);

        emptyView = getLayoutInflater().inflate(R.layout.empty_view_btn, (ViewGroup) mRecyclerView.getParent(), false);
        btn_concern = emptyView.findViewById(R.id.btn_concern);

        mAdapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {

        onRefresh();
    }

    private void initEvents() {
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mAdapter.setmListener(this);
        btn_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("gotoRankList");
            }
        });

    }

    @Override
    public void onRefresh() {
        getFollowList();
    }

    public void getFollowList() {
        showLoadingDialog("请求中");
        ApiManager.getFollowList(new FollowListObserver(mResultCallBack));
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {

        }

        @Override
        public void onResult(List<?> resultList) {
            communicateBeanList = (List<CommunicateModel>) resultList;
            dismissLoadingDialog();
            mAdapter.setNewData(communicateBeanList);
//                    robotConcernList.addAll(list);
            mAdapter.notifyDataSetChanged();
//                    lv_concern.reflashComplete();
            mSwipeRefreshWidget.setRefreshing(false);
        }

        @Override
        public void onError(Object o) {
            if (o instanceof String)
                showShortToast((String) o);
        }
    };

    //添加关注
    @Override
    public void addConcern(final CommunicateModel communicateBean) {
    }

    //取消关注
    @Override
    public void canConcern(final CommunicateModel communicateBean) {
        showDialog(communicateBean);
    }

    //取消关注的弹出框
    private void showDialog(final CommunicateModel communicateBean) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要取消对" + communicateBean.getName() + "的关注吗");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");
                ApiManager.cancelFollow(communicateBean.getUniqueId(), new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            int flag = jsonObject.getInt("flg");
                            switch (flag) {
                                case 1:
                                    dismissLoadingDialog();
                                    communicateBean.setIndustryIds(1);
                                    showShortToast(getString(R.string.cancle_follow_success));
                                    EventBus.getDefault().post("update");
                                    mAdapter.notifyDataSetChanged();
                                    communicateBeanList.remove(communicateBean);


                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                });
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String message) {
        if (message.equals("update")) {
            onRefresh();
        }
    }

    //获取详情
    @Override
    public void getDetail(CommunicateModel communicateBean) {


        startActivity(RobotDetailActivity.getCallingIntent(this.getContext(), communicateBean) );
    }

    @Override
    public void onItemClick(CommunicateModel communicateBean) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
