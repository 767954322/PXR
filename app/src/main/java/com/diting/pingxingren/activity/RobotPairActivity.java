package com.diting.pingxingren.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.NewCommunicateAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.FollowListObserver;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RobotPairActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NewCommunicateAdapter.AttentionListener {

    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewCommunicateAdapter mAdapter;
    private TitleBarView titleBarView;
    private List<CommunicateModel> communicateBeanList = new ArrayList<CommunicateModel>();
    private Button btn_concern;
    private View emptyView;
    private TextView tv_prompt;
    private String mIndustry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_robot_pair);
        EventBus.getDefault().register(this);

        initViews();
        initEvents();
        initDatas();
    }


    @Override
    protected void initViews() {

        mSwipeRefreshWidget = findViewById(R.id.swipe_refresh_widget_speed);
        mRecyclerView = findViewById(R.id.recycle_view_speed);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new NewCommunicateAdapter(R.layout.item_communicate, communicateBeanList);


        emptyView = getLayoutInflater().inflate(R.layout.empty_view_btn, (ViewGroup) mRecyclerView.getParent(), false);
        tv_prompt = emptyView.findViewById(R.id.tv_prompt);

        btn_concern = emptyView.findViewById(R.id.btn_concern);

        mAdapter.setEmptyView(emptyView);
        emptyView.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
        inittitleViews();
    }

    private void inittitleViews() {
        titleBarView =   findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setTitleText(R.string.robot_pair);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public boolean isIndustry;

    private void initDatas() {
        mIndustry = MySharedPreferences.getIndustry();
        if (!Utils.isEmpty(mIndustry)) {

            titleBarView.setBtnRightText(mIndustry);
            tv_prompt.setText("当前行业没有机器人，请选择行业");
            btn_concern.setText("选择行业");
            titleBarView.getBtnRight().setVisibility(View.VISIBLE);
            isIndustry = true;
            if (mIndustry.length() > 4) {

//                industry = MySharedPreferences.getIndustry().substring(0, 4) + "..";
                titleBarView.setBtnRightText(MySharedPreferences.getIndustry().substring(0, 4) + "..");
            } else {
                titleBarView.setBtnRightText(mIndustry);
            }
            onRefresh();
        } else {
            isIndustry = false;
            titleBarView.getBtnRight().setVisibility(View.GONE);
            titleBarView.setBtnRightText("选择行业");
            btn_concern.setText("机器人设置");
            emptyView.setVisibility(View.VISIBLE);
            tv_prompt.setText("您还没有设置行业，请到机器人设置设置行业");
        }
//        onRefresh();

    }

    @Override
    protected void initEvents() {
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mAdapter.setmListener(this);
        btn_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isIndustry) {
                    startActivityForResult(SelectIndustryActivity.callingIntent(RobotPairActivity.this, titleBarView.getBtnRight().getText().toString()), 4);

                } else {
                    startActivity(RobotManagerActivity.callingIntent(RobotPairActivity.this));
                }


            }
        });
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(SelectIndustryActivity.callingIntent(RobotPairActivity.this, titleBarView.getBtnRight().getText().toString()), 4);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {

                case 4:
                    if (data != null) {
                        String industry = data.getStringExtra("industry");
                        mIndustry = industry;
                        onRefresh();
                        if (!Utils.isEmpty(industry))
                            if (industry.length() > 4) {

                                industry = industry.substring(0, 4) + "..";
                                titleBarView.setBtnRightText(industry);
                            } else {
                                titleBarView.setBtnRightText(industry);
                            }

                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void getRobotSpeedList() {
        showLoadingDialog("请求中");
        ApiManager.getRobotSpeedList(mIndustry, new FollowListObserver(new ResultCallBack() {
            @Override
            public void onResult(Object result) {

            }

            @Override
            public void onResult(List<?> resultList) {
                emptyView.setVisibility(View.VISIBLE);
                communicateBeanList = (List<CommunicateModel>) resultList;
                dismissLoadingDialog();
                mAdapter.setNewData(communicateBeanList);
                mSwipeRefreshWidget.setRefreshing(false);
            }

            @Override
            public void onError(Object o) { dismissLoadingDialog();
                if (o instanceof String)
                    showShortToast((String) o);
            }
        }));
    }


    @Override
    public void onRefresh() {
        getRobotSpeedList();
    }

    //添加关注
    @Override
    public void addConcern(final CommunicateModel communicateBean) {
        ApiManager.addFollow(communicateBean.getUniqueId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {


                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    int flag = jsonObject.getInt("flg");
                    switch (flag) {
                        case 0:
                            EventBus.getDefault().post("update");
                            communicateBean.setIndustryIds(0);
                            showShortToast(getString(R.string.add_follow_success));
                            mAdapter.notifyDataSetChanged();

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
                if (o instanceof String)
                    showShortToast((String) o);
            }
        });
    }

    //取消关注
    @Override
    public void canConcern(final CommunicateModel communicateBean) {
        showDialog(communicateBean);
    }

    //取消关注的弹出框
    private void showDialog(final CommunicateModel communicateBean) {
        final MyDialog myDialog = new MyDialog(this);
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
                                    EventBus.getDefault().post("update");
                                    showShortToast(getString(R.string.cancle_follow_success));
                                    mAdapter.notifyDataSetChanged();
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


    //获取详情
    @Override
    public void getDetail(CommunicateModel communicateBean) {
//        if (communicateBean.getIndustryIds()) {
        startActivity(RobotDetailActivity.getCallingIntent(this, communicateBean));
//        } else {
//            startActivity(RobotDetailActivity.getCallingIntent(this.getContext(), communicateBean ));
//        }
    }

    @Override
    public void onItemClick(CommunicateModel communicateBean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String message) {
        if (message.equals("update")) {
            onRefresh();
        } else if (message.equals("updateHeadImage")) {

            titleBarView.getBtnRight().setVisibility(View.VISIBLE);
            tv_prompt.setText("当前行业没有机器人，请选择行业");
            btn_concern.setText("选择行业");
            mIndustry = MySharedPreferences.getIndustry();
            isIndustry = true;
            if (MySharedPreferences.getIndustry().length() > 4) {

//                industry = MySharedPreferences.getIndustry().substring(0, 4) + "..";
                titleBarView.setBtnRightText(MySharedPreferences.getIndustry().substring(0, 4) + "..");
            } else {
                titleBarView.setBtnRightText(mIndustry);
            }
            onRefresh();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
