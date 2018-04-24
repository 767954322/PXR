package com.diting.pingxingren.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/10, 15:50.
 * 登录界面.
 */

public class RobotDetailActivity extends BaseActivity implements ClickListener, View.OnClickListener {

    private static final String EXTRA = "robot";
    private TextView tvfollow;
    private ImageView ivAttention;
    private LinearLayout layAttention;
    private TextView tvAttentionState;
    //
    private TitleBarView titleBarView;
    private CommunicateModel model;
    private CircleImageView ivHeader;
    private TextView tv_robotName;
    private TextView tv_fansNum;
    private TextView tv_robotOwner;
    private TextView tv_remark;
    private TextView tv_phoneNum;
    private TextView tv_robotProfile;
    private ImageView iv_profile;
    private Button toChat;
    private LinearLayout lay_remark;
    private View line;
    private int fansNum;
    private static final String TYPE_TEXT = "TYPE_TEXT";
    private static final String TYPE_AUDIO = "TYPE_AUDIO";
    private static final String TYPE_FILE = "TYPE_FILE";
    private static final String TYPE_PICTURE = "TYPE_PICTURE";
    private static final String SPACE = ";";
    private String type;
    private String profileUrl;

    public static Intent getCallingIntent(Context context, CommunicateModel communicateModel) {
        Intent intent = new Intent();
        intent.setClass(context, RobotDetailActivity.class);
        intent.putExtra(EXTRA, communicateModel);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_robot_detail);
        initViews();
        inittitleViews();
        initEvents();
        initData();
    }

    @Override
    protected void initViews() {

        ivHeader = findViewById(R.id.iv_photo);
        tv_robotName = findViewById(R.id.tv_robot_name);
        tv_fansNum = findViewById(R.id.tv_fans_count);
        tv_robotOwner = findViewById(R.id.tv_robor_owner);
        tv_remark = findViewById(R.id.tv_remark);
        tv_phoneNum = findViewById(R.id.tv_username);
        tv_robotProfile = findViewById(R.id.tv_profile);
        toChat = findViewById(R.id.btn_chat);
        lay_remark = findViewById(R.id.lay_remark);
        line = findViewById(R.id.line);

        iv_profile = findViewById(R.id.iv_profile);
        tvfollow = findViewById(R.id.tv_cancel);
        ivAttention = findViewById(R.id.ivAttention);
        layAttention = findViewById(R.id.layAttention);
        tvAttentionState = findViewById(R.id.tvAttentionState);
    }


    private void inittitleViews() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText(R.string.robot_detail_title);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void initEvents() {
        toChat.setOnClickListener(this);

        layAttention.setOnClickListener(this);
        tv_remark.setOnClickListener(this);
        tv_phoneNum.setOnClickListener(this);
//        layAttention.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!model.getIndustryids()) {
//                    addConcern();
//                } else {
//                    showDialog();
//                }
//            }
//        });
    }

    private void initData() {
//        ifAddFollow = getIntent().getAction();

        model = getIntent().getParcelableExtra(EXTRA);
        if (!Utils.isEmpty(model.getRobotHeadImg())) {
            Glide.with(this).load(model.getRobotHeadImg()).into(ivHeader);
        }
        tv_robotName.setText(!Utils.isEmpty(model.getName()) && !model.getName().equals("null")
                ? model.getName() : "未设置机器人名");
        tv_fansNum.setText(model.getFansNum());
        if (!Utils.isEmpty(model.getFansNum())) {
            fansNum = Integer.parseInt(model.getFansNum());
        }
        tv_robotOwner.setText(model.getCompanyName());
        tv_phoneNum.setText(model.getNamePinyin());
        tv_remark.setText(Utils.isEmpty(model.getShortDomainName()) ? "添加备注" : model.getShortDomainName());
        String profile = model.getProfile();
//        tv_robotProfile.setText(Utils.isEmpty(model.getProfile()) || "null".equals(model.getProfile()) ? "这个人很懒, 什么都没有设置" : model.getProfile());
        if (!Utils.isEmpty(profile)) {
            String[] strings = profile.split(SPACE);
            if (strings.length != 2) {//当老用户没有该标记时
                tv_robotProfile.setText(Utils.isEmpty(profile) || "null".equals(profile) ? "这个人很懒, 什么都没有设置" : profile);

            } else {
                type=strings[0];
                profileUrl=strings[1];
                initWelcomeContent( strings[1]);
            }
        }
        if (!MySharedPreferences.getLogin()) {
            layAttention.setVisibility(View.INVISIBLE);
            toChat.setVisibility(View.INVISIBLE);
            tv_remark.setEnabled(false);
            lay_remark.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if (model.getUniqueId().equals(MySharedPreferences.getUniqueId())) {
            layAttention.setVisibility(View.INVISIBLE);
            tv_remark.setEnabled(false);
            lay_remark.setVisibility(View.GONE);
        }
        ApiManager.getIsAddFollow(model.getUniqueId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    int flag = jsonObject.getInt("flg");
                    switch (flag) {
                        case 1:
//                            model.setIndustryIds(0);
                            tvAttentionState.setText("加关注");
                            tvAttentionState.setTextColor(Color.parseColor("#00c4c2"));
                            ivAttention.setImageResource(R.mipmap.icon_add_concern);
                            lay_remark.setVisibility(View.GONE);
                            line.setVisibility(View.GONE);

                            break;
                        case 0:
//                            model.setIndustryIds(1);
                            tvAttentionState.setText("已关注");
                            tvAttentionState.setTextColor(Color.parseColor("#969696"));
                            ivAttention.setImageResource(R.mipmap.icon_concerned);
                            lay_remark.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);

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

            }
        });


    }

    private void initWelcomeContent(  String content) {
        switch (type) {
            case TYPE_TEXT:
                tv_robotProfile.setVisibility(View.VISIBLE);
                iv_profile.setVisibility(View.GONE);
                iv_profile.setVisibility(View.GONE);
                tv_robotProfile.setText(Utils.isEmpty(content) || "null".equals(content) ? "这个人很懒, 什么都没有设置" : content);

                break;
            case TYPE_PICTURE:
                tv_robotProfile.setVisibility(View.GONE);
                iv_profile.setVisibility(View.VISIBLE);
                Glide.with(this).load(content).into(iv_profile);
//                ic_file
//                        icon_play
                break;
            case TYPE_AUDIO:
                tv_robotProfile.setVisibility(View.GONE);
                iv_profile.setVisibility(View.VISIBLE);
                iv_profile.setBackgroundColor(getResources().getColor(R.color.call_voice_bg_color));
                iv_profile.setImageDrawable(getResources().getDrawable(R.mipmap.icon_play));
                break;
            case TYPE_FILE:
                tv_robotProfile.setVisibility(View.GONE);
                iv_profile.setVisibility(View.VISIBLE);
                iv_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_file));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent detailIntent = new Intent();
        switch (view.getId()) {
            case R.id.layAttention:
                if (!model.getIndustryIds()) {
                    addConcern();
                } else {
                    showDialog();
                }
                break;
            case R.id.tv_remark:
                editRemark();
                break;
            case R.id.btn_chat:
                startActivity(NewChatActivity.callingIntent(RobotDetailActivity.this, model));
                finish();
                break;
            case R.id.tv_username:
                call(tv_phoneNum.getText().toString());
                break;
            case R.id.iv_profile:
                switch (type) {

                    case TYPE_AUDIO:
                        if (!Utils.isEmpty(profileUrl)) {
                            detailIntent.setAction("audio");
                            detailIntent.putExtra("media_url",profileUrl);
                            detailIntent.setClass(this, EnclosureShowActivity.class);
                        } else {
                            showShortToast("无效地址");
                        }
                        break;
                    case TYPE_FILE:
                        if (profileUrl.contains("http")) {
                            detailIntent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(profileUrl);
                            detailIntent.setData(content_url);
                            // detailIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                        }
                        break;
                }


                break;
            default:
                break;
        }
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void editRemark() {
        String remark = model.getShortDomainName();
        final MyDialog myDialog = new MyDialog(this);
        myDialog.setIsContent(true);
        myDialog.setTitle("添加备注");
        myDialog.setContentLength(5);
        if (!Utils.isEmpty(remark)) {
            myDialog.setContentText(remark);
        } else {
            myDialog.setContentText(null);
        }

        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (TextUtils.isEmpty(myDialog.getContentText())) {
                    myDialog.dismiss();
                    return;
                }
                showLoadingDialog("请求中");
                ApiManager.updateRemarkFormFollow(model.getUniqueId(), myDialog.getContentText(), new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            dismissLoadingDialog();
                            if (jsonObject.getString("message").equals("修改成功！")) {
                                myDialog.dismiss();
                                tv_remark.setText(myDialog.getContentText());
                                model.setShortDomainName(myDialog.getContentText());
                                EventBus.getDefault().post("update");
                            } else {
                                myDialog.dismiss();
                                showShortToast("请求失败");
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
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void showDialog() {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要取消对" + model.getName() + "的关注吗");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");
                ApiManager.cancelFollow(model.getUniqueId(), new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            int flag = jsonObject.getInt("flg");
                            switch (flag) {
                                case 1:
                                    dismissLoadingDialog();
                                    myDialog.dismiss();
                                    model.setIndustryIds(1);
                                    EventBus.getDefault().post("update");
                                    showShortToast(getString(R.string.cancle_follow_success));
                                    lay_remark.setVisibility(View.GONE);
                                    line.setVisibility(View.GONE);
                                    tvAttentionState.setTextColor(Color.parseColor("#00c4c2"));
                                    tv_remark.setText("添加备注");
                                    model.setShortDomainName("");
                                    if (!Utils.isEmpty(tv_fansNum.getText().toString())) {
                                        fansNum = Integer.parseInt(tv_fansNum.getText().toString());
                                        fansNum = fansNum - 1;
                                        tv_fansNum.setText("" + fansNum);

                                    }
                                    ivAttention.setImageResource(R.mipmap.icon_add_concern);
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
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }


    public void addConcern() {
        ApiManager.addFollow(model.getUniqueId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    int flag = jsonObject.getInt("flg");
                    switch (flag) {
                        case 0:
                            model.setIndustryIds(0);

                            EventBus.getDefault().post("update");
                            showShortToast(getString(R.string.add_follow_success));
                            lay_remark.setVisibility(View.VISIBLE);
                            line.setVisibility(View.VISIBLE);
                            tvAttentionState.setTextColor(Color.parseColor("#969696"));
                            ivAttention.setImageResource(R.mipmap.icon_concerned);
                            fansNum = fansNum + 1;
                            tv_fansNum.setText("" + fansNum);
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


//        showLoadingDialog("请求中");
//        Diting.addConcern(robotConcern.getPhone(), new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                dismissLoadingDialog();
//                try {
//                    int flag = response.getInt("flg");
//                    switch (flag) {
//                        case 0:
//                            showShortToast("关注成功");
//                            robotConcern.setConcerned(true);
//                            tvAttentionState.setText("已关注");
//                            EventBus.getDefault().post("update");
//                            tvAttentionState.setTextColor(Color.parseColor("#969696"));
//                            ivAttention.setImageResource(R.mipmap.icon_concerned);
//                            break;
//                        case 1:
//                            showShortToast("只能关注20个机器人");
//                            break;
//                        case 2:
//                            showShortToast("已关注");
//                            break;
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                dismissLoadingDialog();
//                showShortToast("关注失败");
//
//            }
//        });
    }


    @Override
    public void onClick(Object o) {

    }
}
