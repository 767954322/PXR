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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.ChatUserAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.ChatUserManageItemModel;
import com.diting.pingxingren.model.ChatUserManageModel;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChatUserManageObserver;
import com.diting.pingxingren.net.observers.RobotInfoByPhoneObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/3/13.
 */

public class UserManageActivity extends BaseActivity implements View.OnClickListener, ChatUserAdapter.OnRobotClickListener, SwipeRefreshLayout.OnRefreshListener {
    private TitleBarView titleBarView;
    private ChatUserAdapter adapter;
    private List<ChatUserManageItemModel> chatUserList = new ArrayList<ChatUserManageItemModel>();
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private View emptyView;
    private TextView tv_prompt;
    private Button btn_concern;

    private int count = 1;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_manage);
        initViews();
        initDate();
        initEvents();

    }

    private void initDate() {
        onRefresh();
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("访客管理");
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
        mSwipeRefreshWidget = findViewById(R.id.swipe_refresh_widget_usermanage);
        mRecyclerView = findViewById(R.id.recycle_view_usermanage);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new ChatUserAdapter(R.layout.item_chat_user, chatUserList);
        adapter.setmContext(this);
        mRecyclerView.setAdapter(adapter);

        emptyView = getLayoutInflater().inflate(R.layout.empty_view_btn, (ViewGroup) mRecyclerView.getParent(), false);
        tv_prompt = emptyView.findViewById(R.id.tv_prompt);
        tv_prompt.setText(R.string.no_usermessage_date);
        btn_concern = emptyView.findViewById(R.id.btn_concern);
        btn_concern.setText("推广我的机器人");
        emptyView.setVisibility(View.GONE);
        adapter.setEmptyView(emptyView);


    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        adapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        adapter.setOnRobotClickListener(this);
        btn_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(QRCodeActivity.class);
            }
        });
    }


    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getChatUserManageList(count, new ChatUserManageObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            if (result instanceof ChatUserManageModel) {

                emptyView.setVisibility(View.VISIBLE);
                ChatUserManageModel chatUserManageModel = (ChatUserManageModel) result;
                dismissLoadingDialog();
                if (count == 1) {
                    adapter.setNewData(chatUserManageModel.getItems());
                } else {
                    adapter.addData(chatUserManageModel.getItems());
                    adapter.loadMoreComplete();
                }

                if (count * 15 >= chatUserManageModel.getTotal()) {
                    adapter.loadMoreEnd(true);
                }

                count++;
            } else if (result instanceof RobotInfoModel) {
                final RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                ApiManager.getRobotInfoByUniqueId(robotInfoModel.getUniqueId(), new RobotInfoObserver(new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        if (result instanceof RobotInfoModel) {
                            RobotInfoModel mRobotInfoModel = (RobotInfoModel) result;
                            if (type == 1) {
                                CommunicateModel communicateModel = new CommunicateModel();
                                communicateModel.setName(mRobotInfoModel.getName());
                                communicateModel.setUniqueId(mRobotInfoModel.getUniqueId());
                                communicateModel.setRobotHeadImg(mRobotInfoModel.getHeadImg());
                                communicateModel.setProfile(mRobotInfoModel.getProfile());
                                communicateModel.setCompanyName(mRobotInfoModel.getCompanyName());
                                communicateModel.setFansNum(mRobotInfoModel.getFansNum());
                                communicateModel.setShortDomainName(mRobotInfoModel.getShortDomainName());
                                communicateModel.setRobotVal(mRobotInfoModel.getRobotVal());
                                communicateModel.setNamePinyin(mRobotInfoModel.getUsername());
                                communicateModel.setInvalidAnswer1(mRobotInfoModel.getInvalidAnswer1());
                                communicateModel.setInvalidAnswer2(mRobotInfoModel.getInvalidAnswer2());
                                communicateModel.setInvalidAnswer3(mRobotInfoModel.getInvalidAnswer3());
                                startActivity(RobotDetailActivity.getCallingIntent(UserManageActivity.this, communicateModel) );
                            } else if (type == 2) {
                                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                                robotInfoModel.setmFrom("isOtherChat");
                                startActivity(NewChatActivity.callingIntent(
                                        UserManageActivity.this, robotInfoModel));
                            }


                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {

                    }
                }));
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
    public void onRefresh() {
        mSwipeRefreshWidget.setRefreshing(false);
        count = 1;
        showLoadingDialog("请求中");
        ApiManager.getChatUserManageList(count, new ChatUserManageObserver(mResultCallBack));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_question:
                startActivity(AddKnowledgeActivity.class);
                break;
            default:
                break;
        }
    }

    public void getRobotInfoByPhone(final String phoneNum) {
        ApiManager.getRobotInfoByPhone(phoneNum, new RobotInfoByPhoneObserver(mResultCallBack));
//        ApiManager.getRobotInfoByPhone(phoneNum, new RobotInfoByPhoneObserver(new ResultCallBack() {
//            @Override
//            public void onResult(Object result) {
//
//                if (result instanceof RobotInfoModel) {
//
//                    RobotInfoModel robotInfoModel = (RobotInfoModel) result;
//
//
//                    ApiManager.getRobotInfoByUniqueId(robotInfoModel.getUniqueId(), new RobotInfoObserver(new ResultCallBack() {
//                        @Override
//                        public void onResult(Object result) {
//                            if (result instanceof RobotInfoModel) {
//                                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
//                                robotInfoModel.setmFrom("isOtherChat");
//                                startActivity(NewChatActivity.callingIntent(
//                                        UserManageActivity.this, robotInfoModel));
//                            }
//                        }
//
//                        @Override
//                        public void onResult(List<?> resultList) {
//
//                        }
//
//                        @Override
//                        public void onError(Object o) {
//
//                        }
//                    }));
////                        ChatBundle chatBundle = new ChatBundle();
////                        chatBundle.setFrom("chatting");
////                        chatBundle.setUniqueId(robotInfoModel.getUniqueId());
////                        chatBundle.setUserName(robotInfoModel.getUsername());
////                        chatBundle.setHeadPortrait(robotInfoModel.getHeadImg());
////                        chatBundle.setRobotName(robotInfoModel.getName());
////                        chatBundle.setWelcome(robotInfoModel.getWelcomes());
////                        chatBundle.setAnswer1(robotInfoModel.getInvalidAnswer1());
////                        chatBundle.setAnswer2(robotInfoModel.getInvalidAnswer2());
////                        chatBundle.setAnswer3(robotInfoModel.getInvalidAnswer3());
//
////                        startActivity(NewChatActivity.callingIntent(
////                                UserManageActivity.this, robotInfoModel));
//                }
//
//            }
//
//            @Override
//            public void onResult(List<?> resultList) {
//
//            }
//
//            @Override
//            public void onError(Object o) {
//                if (o instanceof String)
//                    showShortToast((String) o);
//            }
//        }));
    }

    @Override
    public void gotoChat(ChatUserManageItemModel chatUserManageItemModel) {
        type = 2;
        getRobotInfoByPhone(chatUserManageItemModel.getLoginUsername());
    }

    @Override
    public void gotoChatLog(ChatUserManageItemModel chatUserManageItemModel) {
        Intent intent = new Intent(this, ChatLogActivity.class);
        intent.putExtra("uuid", chatUserManageItemModel.getUuid());
        intent.putExtra("chatUserManageItemModel", chatUserManageItemModel);
        startActivity(intent);
    }

    @Override
    public void gotoRobotDetail(ChatUserManageItemModel chatUserManageItemModel) {
        type = 1;
        getRobotInfoByPhone(chatUserManageItemModel.getLoginUsername());
    }


}
