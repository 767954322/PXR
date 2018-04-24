package com.diting.pingxingren.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.LoginResultModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.ThirdAccountModel;
import com.diting.pingxingren.model.ThirdBindModel;
import com.diting.pingxingren.model.ThirdLoginDateModel;
import com.diting.pingxingren.model.UserInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChildRobotsObserver;
import com.diting.pingxingren.net.observers.LoginObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.net.observers.ThirdIsBindObserver;
import com.diting.pingxingren.net.observers.UserInfoObserver;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.pingxingren.util.Utils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;


/**
 * Created by asus on 2017/1/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    //微信
    private IWXAPI api;

    private TitleBarView titleBarView;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button tv_register;
    private TextView tv_forget;
    private ImageView iv_wechat;
    private RelativeLayout rl_main;
    private Button btn_try;
    private ImageView iv_qq;
    private ImageView iv_weibo;

    private String mUserName;
    private String mPassword;
    private boolean isTry = false;
    //微信登录
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA platform;
    private String AccessToken;
    private String Uid;
    private Intent thirdLigonIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareAPI = UMShareAPI.get(this);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        mShareAPI.setShareConfig(config);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        thirdLigonIntent = new Intent();
//        regToWx();
        initViews();
        initEvents();
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setTitleText(R.string.login);
        titleBarView.setBtnRightText("先看看");
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTry();
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);
        tv_register = findViewById(R.id.register);
        tv_forget = findViewById(R.id.forget);
        iv_wechat = findViewById(R.id.iv_wechat);
        iv_qq = findViewById(R.id.iv_qq);
        rl_main = findViewById(R.id.rl_main);
        btn_try = findViewById(R.id.btn_try);
        iv_weibo = findViewById(R.id.iv_weibo);
        getUser();
        userCheck();
    }

    private void getUser() {
        String username = MySharedPreferences.getUsername();
        String password = MySharedPreferences.getPassword();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)&&!MySharedPreferences.getThirdState()) {
            et_username.setText(username);
            et_password.setText(password);
        }
    }

    @Override
    protected void initEvents() {
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_weibo.setOnClickListener(this);
        et_username.addTextChangedListener(mtextWatcher);
        et_password.addTextChangedListener(mtextWatcher);
        rl_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(LoginActivity.this, v);
                return false;
            }
        });
        btn_try.setOnClickListener(this);
//        UmengTool.getSignature(LoginActivity.this);
    }

    private void userCheck() {
        if (!TextUtils.isEmpty(et_username.getText().toString()) && !TextUtils.isEmpty(et_password.getText().toString())) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                MobclickAgent.onEvent(LoginActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_LOGIN);
                login();
                break;
            case R.id.register:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_REGISTER);
                startActivity(RegisterActivity.class);
                break;
            case R.id.forget:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_RESET_PASSWORD);
                startActivity(ForgetPasswordActivity.class);
                break;
            case R.id.iv_wechat:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_WECHAT);
                platform = SHARE_MEDIA.WEIXIN;
                thridLogin(platform);
                break;
            case R.id.btn_try:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_TRY);
                gotoTry();
                break;
            case R.id.iv_qq:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_QQ);
                platform = SHARE_MEDIA.QQ;
                thridLogin(platform);
                break;
            case R.id.iv_weibo:
                MobclickAgent.onEvent(LoginActivity.this,UmengUtil.EVENT_CLICK_NUM_OF_LOGIN_SINA_WEIBO);
                platform = SHARE_MEDIA.SINA;
                thridLogin(platform);
                break;
            default:
                break;
        }
    }

    private void gotoTry() {
        showLoadingDialog("请求中");
        isTry = true;
        ApiManager.getRobotInfoByUniqueId(Constants.XIAODI_UNIQUEID, new RobotInfoObserver(mResultCallBack));
    }

    private TextWatcher mtextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //内容改变前
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userCheck();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //结果显示在输入框中
        }
    };

    //登录
    private void login() {
        showLoadingDialog("登录中");
        mUserName = et_username.getText().toString();
        mPassword = et_password.getText().toString();
        btn_login.setEnabled(true);
        ApiManager.login(mUserName, mPassword, new LoginObserver(mResultCallBack));
    }

    private void thridLogin(SHARE_MEDIA platform) {
        mShareAPI.deleteOauth(LoginActivity.this, platform, null);

        mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthListener);
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof LoginResultModel) {
                if (platform == null) {
                    MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
                    MySharedPreferences.saveLogin(true);
                    MySharedPreferences.saveThirdState(false);
                    ApiManager.getUserInfo(new UserInfoObserver(mResultCallBack));
                } else {
                    ApiManager.getUserInfo(new UserInfoObserver(mResultCallBack));
                }
//                MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
//                MySharedPreferences.saveLogin(true);
//                ApiManager.getUserInfo(new UserInfoObserver(mResultCallBack));
            } else if (result instanceof UserInfoModel) {
                dismissLoadingDialog();
                btn_login.setEnabled(true);
                UserInfoModel userInfoModel = (UserInfoModel) result;
                String robotName = userInfoModel.getRobotName();
                String companyName = userInfoModel.getCompanyName();
                if (Utils.isNotEmpty(robotName) || Utils.isNotEmpty(companyName)) {
                    showShortToast("登录成功!");
                    // 保存用户信息
                    MySharedPreferences.saveUniqueId(userInfoModel.getUnique_id());
                    MySharedPreferences.savePhoneSwitch(userInfoModel.getTelephoneSwitch());
                    MySharedPreferences.saveHeadPortrait(userInfoModel.getHeadPortrait());
                    MySharedPreferences.saveProfile(userInfoModel.getProfile());
                    MySharedPreferences.saveRobotName(robotName);
                    MySharedPreferences.saveDefaultRobotName(robotName);
                    MySharedPreferences.saveDefaultRobot(robotName);
                    MySharedPreferences.saveCompanyName(companyName);
                    MySharedPreferences.saveFansCount(userInfoModel.getFansCount());

                    ApiManager.getChildRobots(new ChildRobotsObserver(this));
                } else {
                    startActivity(SettingActivity.class);
                    finish();
                }
            } else if (result instanceof RobotInfoModel) {
                dismissLoadingDialog();
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                if (isTry) {
                    robotInfoModel.setmFrom("try");

//                    ChatBundle chatBundle = new ChatBundle();
//                    chatBundle.setFrom("try");
//                    chatBundle.setUserName(robotInfoModel.getUsername());
//                    chatBundle.setHeadPortrait(robotInfoModel.getHeadImg());
//                    chatBundle.setRobotName(robotInfoModel.getName());
//                    chatBundle.setWelcome(robotInfoModel.getWelcomes());
                    startActivity(NewChatActivity.callingIntent(
                            LoginActivity.this, robotInfoModel));
                } else {
                    MySharedPreferences.saveUniqueId(robotInfoModel.getUniqueId());
                    MySharedPreferences.saveProfile(robotInfoModel.getProfile());
                    MySharedPreferences.saveRobotName(robotInfoModel.getName());
                    MySharedPreferences.saveDefaultRobot(robotInfoModel.getName());
                    MySharedPreferences.saveCompanyName(robotInfoModel.getCompanyName());
                    MySharedPreferences.saveRobotHeadPortrait(robotInfoModel.getRobotHeadImg());

                    MySharedPreferences.saveIndustry(robotInfoModel.getHangye());
                    MySharedPreferences.savePrice(robotInfoModel.getZidingyi());
                    MySharedPreferences.setShengri(robotInfoModel.getShengri());
                    MySharedPreferences.saveHangYeLevel(robotInfoModel.getHangyedengji());
                    MySharedPreferences.saveSex(robotInfoModel.getSex());
                    MySharedPreferences.saveEnable(robotInfoModel.isEnable());
                    MySharedPreferences.saveUniversalAnswer1(robotInfoModel.getInvalidAnswer1());
                    MySharedPreferences.saveUniversalAnswer2(robotInfoModel.getInvalidAnswer2());
                    MySharedPreferences.saveUniversalAnswer3(robotInfoModel.getInvalidAnswer3());
                    MySharedPreferences.saveRobotVal(robotInfoModel.getRobotVal());
                    startActivity(HomeActivity.class);
                    finish();
                }
            } else if (result instanceof Boolean) {
                isTry = false;
                ApiManager.getRobotInfo(new RobotInfoObserver(this));
            }
        }

        @Override
        public void onResult(List<?> resultList) {
            if (resultList.size() > 0) {
                Class<?> aClass = resultList.get(0).getClass();
                if (aClass.getName().equals(ChildRobotModel.class.getName())) {
                    Utils.sChildRobotModels = (List<ChildRobotModel>) resultList;
                    ApiManager.chooseChildRobot(
                            Utils.getRobotUniqueIdByChildRobotList(MySharedPreferences.getRobotName()), this);
                }
            } else {
                startActivity(SettingActivity.class);
                finish();
            }
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            btn_login.setEnabled(true);
            if (o instanceof String) {
                showShortToast((String) o);
            }
        }
    };


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            checkBingThird(data);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    private ThirdLoginDateModel mThirdLoginDateModel;

    private void checkBingThird(Map<String, String> data) {
        switch (platform) {
            case WEIXIN:
                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("openid");
                ApiManager.isBindWeiXin(AccessToken, Uid, new ThirdIsBindObserver(mThirdResultCallBack));
                break;
            case QQ:

                //{unionid=, is_yellow_vip=0, screen_name=北京谛听机器人,
                // msg=, vip=0, city=, accessToken=6A8911AF776571200F1C4627A79F647C,
                // gender=男, province=, is_yellow_year_vip=0,
                // openid=486DC101CBBA9BC734FCA48A77D2ECD8,
                // profile_image_url=http://q.qlogo.cn/qqapp/100424468/486DC101CBBA9BC734FCA48A77D2ECD8/100,
                // yellow_vip_level=0, access_token=6A8911AF776571200F1C4627A79F647C,
                // iconurl=http://q.qlogo.cn/qqapp/100424468/486DC101CBBA9BC734FCA48A77D2ECD8/100, name=北京谛听机器人,
                // uid=486DC101CBBA9BC734FCA48A77D2ECD8,
                // expiration=1524645259533, expires_in=1524645259533, level=0, ret=0}

                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("openid");
                ApiManager.isBindQQ(AccessToken, Uid,new ThirdIsBindObserver(mThirdResultCallBack));


                break;
            case SINA:

                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("uid");
                ApiManager.isBindWeiBo(AccessToken, Uid, new ThirdIsBindObserver(mThirdResultCallBack));
                break;
        }
    }

    private ResultCallBack mThirdResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof ThirdBindModel) {
                ThirdBindModel thirdBindModel = (ThirdBindModel) result;
                int flag = thirdBindModel.getFlag();
                switch (platform) {
                    case WEIXIN:

                        switch (flag) {
                            case 0:
                                mThirdLoginDateModel = new ThirdLoginDateModel();
                                mThirdLoginDateModel.setPlatform(1);
                                mThirdLoginDateModel.setAccessToken(AccessToken);
                                mThirdLoginDateModel.setUid(Uid);
                                startActivity(ThirdLoginActivity.getCallingIntent(LoginActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                ThirdAccountModel thirdAccountModel = thirdBindModel.getAccount();
                                MySharedPreferences.saveSafeUserInfo(thirdAccountModel.getUserName(), thirdAccountModel.getPassword());
                                MySharedPreferences.saveLogin(true);
                                MySharedPreferences.saveThirdState(true);
                                ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));
                                break;
                        }
                        break;
                    case QQ:

                        switch (flag) {
                            case 0:
                                mThirdLoginDateModel = new ThirdLoginDateModel();
                                mThirdLoginDateModel.setPlatform(2);
                                mThirdLoginDateModel.setAccessToken(AccessToken);
                                mThirdLoginDateModel.setUid(Uid);
                                startActivity(ThirdLoginActivity.getCallingIntent(LoginActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                ThirdAccountModel thirdAccountModel = thirdBindModel.getAccount();
                                MySharedPreferences.saveSafeUserInfo(thirdAccountModel.getUserName(), thirdAccountModel.getPassword());
                                MySharedPreferences.saveLogin(true);
                                MySharedPreferences.saveThirdState(true);
                                ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));
                                break;
                        }

                        break;
                    case SINA:
                        switch (flag) {
                            case 0:
                                mThirdLoginDateModel = new ThirdLoginDateModel();
                                mThirdLoginDateModel.setPlatform(3);
                                mThirdLoginDateModel.setAccessToken(AccessToken);
                                mThirdLoginDateModel.setUid(Uid);
                                startActivity(ThirdLoginActivity.getCallingIntent(LoginActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                ThirdAccountModel thirdAccountModel = thirdBindModel.getAccount();
                                MySharedPreferences.saveSafeUserInfo(thirdAccountModel.getUserName(), thirdAccountModel.getPassword());
                                MySharedPreferences.saveLogin(true);
                                MySharedPreferences.saveThirdState(true);
                                ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));
                                break;
                        }
                        break;
                }
            }

        }

        @Override
        public void onResult(List<?> resultList) {

        }

        @Override
        public void onError(Object o) {

        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void findSuccess(String message) {
        if (message.equals("find_success")) {
            getUser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
//        api.detach();
        super.onDestroy();
    }


}
