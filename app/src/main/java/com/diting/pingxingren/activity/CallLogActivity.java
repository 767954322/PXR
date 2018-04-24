package com.diting.pingxingren.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.CallLogAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.databinding.ActivityCallLogBinding;
import com.diting.pingxingren.entity.ChatBundle;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CallRecordObserver;
import com.diting.pingxingren.net.observers.RobotInfoByPhoneObserver;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.voice.data.body.VoiceCallInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/3/13.
 */

public class CallLogActivity extends BaseActivity implements CallLogAdapter.CallRecorditenListener {

    private ActivityCallLogBinding mBinding;
    private CallLogAdapter adapter;
    private List<VoiceCallInfo> callInfoList = new ArrayList<VoiceCallInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_call_log);
        initViews();
        initEvents();
        initData();
    }

    private void initTitleBarView() {
        mBinding.titleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mBinding.titleBar.setBtnLeft(R.mipmap.icon_back, null);
        mBinding.titleBar.setTitleText("通话记录");
        mBinding.titleBar.setBtnRightText("");
    }

    private void initTitleBarEvents() {
        mBinding.titleBar.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mBinding.titleBar.setBtnRightOnclickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                showDialog();
//            }
//        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        adapter = new CallLogAdapter(R.layout.item_call_log, callInfoList);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty, mBinding.recyclerView);
        adapter.setCallRecorditenListener(this);
        //        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                showLoadingDialog("请求中");
//                final String username = callInfoList.get(position).getFromUserName().equals(MySharedPreferences.getUsername()) ? callInfoList.get(position).getToUserName() : callInfoList.get(position).getFromUserName();
//                Diting.getRobotByUserName(username, new HttpCallBack() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        dismissLoadingDialog();
//
//                        try {
//                            ChatBundle chatBundle = new ChatBundle();
//                            chatBundle.setFrom("call");
//                            chatBundle.setUserName(username);
//                            chatBundle.setHeadPortrait(response.getString("headImg"));
//                            chatBundle.setRobotName(response.getString("name"));
//                            chatBundle.setWelcome(response.getString("welcomes"));
//                            chatBundle.setUniqueId(response.getString("uniqueId"));
//                            startActivity(NewChatActivity.callingIntent(
//                                    CallLogActivity.this, chatBundle));
//                            /*Intent intent = new Intent(CallLogActivity.this, ChatActivity.class);
//                            intent.putExtra("from", "ChatLog");
//                            intent.putExtra("username", username);
//                            intent.putExtra("headPortrait", response.getString("headImg"));
//                            intent.putExtra("welcome", response.getString("welcomes"));
//                            intent.putExtra("robotName", response.getString("name"));
//                            intent.putExtra("uniqueId", response.getString("uniqueId"));
//                            startActivity(intent);*/
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//                        dismissLoadingDialog();
//                        showShortToast("加载失败，请重试");
//                    }
//                });
//            }
//        });
    }

    private void initData() {
        showLoadingDialog("加载中");


        ApiManager.getCallRecordList(new CallRecordObserver(new ResultCallBack() {
            @Override
            public void onResult(Object result) {

            }

            @Override
            public void onResult(List<?> resultList) {

                dismissLoadingDialog();
                callInfoList = (List<VoiceCallInfo>) resultList;
                adapter.setNewData(callInfoList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object o) {
                dismissLoadingDialog();
                if (o instanceof String)
                    showShortToast((String) o);
//                ToastUtils.showShortToastSafe("请求失败!");
            }
        }));
//        Diting.getCallLog(new HttpJsonArrayCallBack() {
//            @Override
//            public void onSuccess(JSONArray response) {
//                dismissLoadingDialog();
//                Gson gson = new Gson();
//                callInfoList.addAll((Collection<? extends VoiceCallInfo>) gson.fromJson(response.toString(), new TypeToken<List<VoiceCallInfo>>() {
//                }.getType()));
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                dismissLoadingDialog();
//            }
//        });
    }

    private void showDialog() {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要清空通话记录吗?");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
                callInfoList.clear();
                adapter.notifyDataSetChanged();
                mBinding.titleBar.getBtnRight().setVisibility(View.GONE);
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();*/
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

    @Override
    public void onCallRecordItemClick(VoiceCallInfo voiceCallInfo) {
//        final String username = voiceCallInfo.getFromUserName().equals(MySharedPreferences.getUsername()) ? voiceCallInfo.getToUserName() : voiceCallInfo.getFromUserName();
//
//        ApiManager.getRobotInfoByPhone(username, new RobotInfoByPhoneObserver(new ResultCallBack() {
//            @Override
//            public void onResult(Object result) {
////                if (result instanceof ChatBundleModel) {
////                    ChatBundleModel chatBundleModel = (ChatBundleModel) result;
////                    chatBundleModel.setmFrom("call");
////                    chatBundleModel.setmUserName(username);
////                    startActivity(NewChatActivity.callingIntent(
////                            CallLogActivity.this, chatBundleModel));
////                }
//                if (result instanceof RobotInfoModel) {
//
//                    RobotInfoModel robotInfoModel = (RobotInfoModel) result;
//                    robotInfoModel.setmFrom("call");
////                    ChatBundle chatBundle = new ChatBundle();
////                    chatBundle.setFrom("call");
////                    chatBundle.setUserName(robotInfoModel.getUsername());
////                    chatBundle.setHeadPortrait(robotInfoModel.getHeadImg());
////                    chatBundle.setRobotName(robotInfoModel.getName());
////                    chatBundle.setWelcome(robotInfoModel.getWelcomes());
////                    chatBundle.setAnswer1(robotInfoModel.getInvalidAnswer1());
////                    chatBundle.setAnswer2(robotInfoModel.getInvalidAnswer2());
////                    chatBundle.setAnswer3(robotInfoModel.getInvalidAnswer3());
////                    LogUtils.e("robotInfoModel==========" + robotInfoModel.toString());
////                    startActivity(NewChatActivity.callingIntent(
////                            CallLogActivity.this, robotInfoModel));
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

//    private void initData(){
//        for(int i = 0;i < 20;i++){
//            VoiceCallInfo voiceCallInfo = new VoiceCallInfo((i%2==0)?"13161202845":"13161202888",(i%2==1)?"13161202845":"13161202888", VoiceCallInfo.CallType.VOICE,"逆流而上","逆流而上","张永升","永升科技","2017-08-09 11:11:11",i * 5555,getType(i) );
//            callInfoList.add(voiceCallInfo);
//        }
//        adapter.notifyDataSetChanged();
//    }
//    private VoiceCallInfo.EndType getType(int i){
//        switch (i % 3){
//            case 0:
//                return VoiceCallInfo.EndType.NORMAL;
//            case 1:
//                return VoiceCallInfo.EndType.NORESPONSE;
//            case 2:
//                return VoiceCallInfo.EndType.REJECT;
//        }
//        return VoiceCallInfo.EndType.NORMAL;
//    }
}
