package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyCustomDialog;
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
import com.diting.pingxingren.util.MyCountDownTimer;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ThirdLoginActivity extends BaseActivity implements View.OnClickListener {
    private int platform;
    private String AccessToken;
    private String Uid;
    private String form;
    private static final String EXTRA = "third_Login";
    private ThirdLoginDateModel model;
    private TitleBarView titleBarView;
    private EditText et_username;
    private EditText et_verify_code;
    private Button btn_send_message;
    private Button bt_next_login;

    private String mUserName;
    private String mVerifyCode;

    public static Intent getCallingIntent(Context context, ThirdLoginDateModel thirdLoginDateModel) {
        Intent intent = new Intent();
        intent.setClass(context, ThirdLoginActivity.class);
        intent.putExtra(EXTRA, thirdLoginDateModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_login);
        initViews();
        initEvents();
        initDate();
    }

    private void initDate() {
        model = getIntent().getParcelableExtra(EXTRA);
        platform = model.getPlatform();
        AccessToken = model.getAccessToken();
        Uid = model.getUid();
        form = model.getForm();
        if (!Utils.isEmpty(form)) {
            et_username.setFocusable(false);
            et_username.setText(MySharedPreferences.getUsername());
        }

    }

    @Override
    protected void initViews() {
        initTitleBarView();
        et_username = findViewById(R.id.username);
        et_verify_code = findViewById(R.id.et_verify_code);
        btn_send_message = findViewById(R.id.btn_send_message);
        bt_next_login = findViewById(R.id.bt_next_login);

    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText("手机号绑定");
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initEvents() {
        btn_send_message.setOnClickListener(this);
        bt_next_login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_message:
                sendMessage();
                break;
            case R.id.bt_next_login:
                thirdLogin();
                break;
        }
    }

    private void sendMessage() {
        btn_send_message.setEnabled(false);
        mUserName = et_username.getText().toString();
        if (!Utils.isMobile(mUserName)) {
            showShortToast("请输入正确的手机号");
            btn_send_message.setEnabled(true);
        } else {
            btn_send_message.setEnabled(true);
            ApiManager.bindThirdSendCode(mUserName, mResultCallBack);
        }
    }

    private void thirdLogin() {
        mUserName = et_username.getText().toString();
        mVerifyCode = et_verify_code.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            showShortToast("手机号不能为空");
            return;
        }


        if (!Utils.isMobile(mUserName)) {
            showShortToast("手机号格式错误, 请重新填写.");
            return;
        }
        if (TextUtils.isEmpty(mVerifyCode)) {

            showShortToast("验证码不能为空");
            return;
        }

        bt_next_login.setEnabled(false);
        showLoadingDialog("注册中...");
        switch (platform) {
            case 1:
                ApiManager.bindWeiXin(mUserName, mVerifyCode,
                        Uid,
                        AccessToken, new ThirdIsBindObserver(mResultCallBack));
                break;
            case 2:
                ApiManager.bindQQ(mUserName, mVerifyCode,
                        Uid,
                        AccessToken, new ThirdIsBindObserver(mResultCallBack));
                break;
            case 3:
                ApiManager.bindWeiBo(mUserName, mVerifyCode,
                        Uid,
                        AccessToken, new ThirdIsBindObserver(mResultCallBack));
                break;
        }

    }
    ThirdAccountModel thirdAccountModel ;
    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            dismissLoadingDialog();
            if (result instanceof JSONObject) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    dismissLoadingDialog();
                    if (jsonObject.getString("message").equals("验证码获取成功!")) {
                        showShortToast(jsonObject.getString("message"));
                        new MyCountDownTimer(btn_send_message, 60000, 100).start();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (result instanceof ThirdBindModel) {
                ThirdBindModel thirdBindModel = (ThirdBindModel) result;
                int flag = thirdBindModel.getFlag();
                switch (platform) {
                    case 1:
                        switch (flag) {

                            case 0:
                                showShortToast("验证码错误");
                                break;
                            case 1:
                                  thirdAccountModel = thirdBindModel.getAccount();
                                if (!Utils.isEmpty(form)) {
                                    showShortToast("绑定成功!");
                                    EventBus.getDefault().post("bind_weixin");
                                    finish();
                                } else {


                                    ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));

                                }
                                break;
                        }
                        break;
                    case 2:
                        switch (flag) {

                            case 0:
                                showShortToast("验证码错误");
                                break;
                            case 1:
                                  thirdAccountModel = thirdBindModel.getAccount();
                                if (!Utils.isEmpty(form)) {
                                    showShortToast("绑定成功!");
                                    EventBus.getDefault().post("bind_qq");
                                    finish();
                                } else {


                                    ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));

                                }
                                break;
                        }
                        break;
                    case 3:
                        switch (flag) {

                            case 0:
                                showShortToast("验证码错误");
                                break;
                            case 1:
                                  thirdAccountModel = thirdBindModel.getAccount();
                                if (!Utils.isEmpty(form)) {
                                    showShortToast("绑定成功!");
                                    EventBus.getDefault().post("bing_sina");
                                    finish();
                                } else {

                                    ApiManager.login(thirdAccountModel.getUserName(), thirdAccountModel.getPassword(), new LoginObserver(mResultCallBack));

                                }

                                break;
                        }
                        break;
                }

            } else if (result instanceof LoginResultModel) {

                MySharedPreferences.saveSafeUserInfo(thirdAccountModel.getUserName(), thirdAccountModel.getPassword());
                MySharedPreferences.saveLogin(true);
                MySharedPreferences.saveThirdState(true);
//                    MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
//                    MySharedPreferences.saveLogin(true);
                ApiManager.getUserInfo(new UserInfoObserver(mResultCallBack));

            } else if (result instanceof UserInfoModel) {
                dismissLoadingDialog();

                UserInfoModel userInfoModel = (UserInfoModel) result;
                String robotName = userInfoModel.getRobotName();
                String companyName = userInfoModel.getCompanyName();
                if (Utils.isNotEmpty(robotName) || Utils.isNotEmpty(companyName)) {

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
                    showPriceTipDialog();

                }
            } else if (result instanceof RobotInfoModel) {
                dismissLoadingDialog();
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;

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

            } else if (result instanceof Boolean) {

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
            bt_next_login.setEnabled(true);
            if (o instanceof String)
                showShortToast((String) o);
        }
    };
    private void showPriceTipDialog() {
        final MyCustomDialog myConDialog = new MyCustomDialog(this);
        myConDialog.setTitleStr(getResources().getString(R.string.tips));
        myConDialog.setConcentStr(getResources().getString(R.string.tv_register_tip));
        myConDialog.setTipFirstisShow(View.GONE);
        myConDialog.setContent_tipFirstStr("");
        myConDialog.setContent_tipSecondStr(getResources().getString(R.string.tv_register_tip));
        myConDialog.setOkOnclickListener(getResources().getString(R.string.submit), new MyCustomDialog.onOkOnclickListener() {
            @Override
            public void onOkClick() {
                myConDialog.dismiss();
                startActivity(SettingActivity.class);
                finish();
            }
        });
        myConDialog.show();
    }
}
