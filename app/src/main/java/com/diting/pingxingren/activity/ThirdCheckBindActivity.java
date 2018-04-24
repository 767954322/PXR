package com.diting.pingxingren.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.ThirdBindModel;
import com.diting.pingxingren.model.ThirdCheckBindModel;
import com.diting.pingxingren.model.ThirdLoginDateModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ThirdCheckBindObserver;
import com.diting.pingxingren.net.observers.ThirdIsBindObserver;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.Utils;
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
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 26, 15:38.
 * Description: .
 */

public class ThirdCheckBindActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mRelWeiXin;
    private RelativeLayout mRelQQ;
    private RelativeLayout mRelSina;

    private TextView WeiXinState;
    private TextView QQState;
    private TextView SinaState;

    private boolean qq;
    private boolean weixin;
    private boolean weibo;
    //微信登录
    private UMShareAPI mShareAPI;
    private SHARE_MEDIA platform;
    private String AccessToken;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareAPI = UMShareAPI.get(this);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        mShareAPI.setShareConfig(config);
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
        initDate();
    }

    private void initDate() {
        ApiManager.checkBindThird(MySharedPreferences.getUsername(), new ThirdCheckBindObserver(mResultCallBack));
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_third_check_bind);
        initTitleBarView();
        mRelWeiXin = findViewById(R.id.rlBindWeiXin);
        mRelQQ = findViewById(R.id.rlBindQQ);
        mRelSina = findViewById(R.id.rlBindSina);
        WeiXinState = findViewById(R.id.tv_bindWeiXin_state);
        QQState = findViewById(R.id.tv_bindQQ_state);
        SinaState = findViewById(R.id.tv_bindSina_state);
    }

    private void initTitleBarView() {
        TitleBarView titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText(StringUtil.getString(R.string.bind_third));
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
        mRelWeiXin.setOnClickListener(this);
        mRelQQ.setOnClickListener(this);
        mRelSina.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBindWeiXin:
                if (!weixin) {
                    platform = SHARE_MEDIA.WEIXIN;
                    thridLogin(platform);
                }
                break;
            case R.id.rlBindQQ:
                if (!qq) {
                    platform = SHARE_MEDIA.QQ;
                    thridLogin(platform);
                }
                break;
            case R.id.rlBindSina:
                if (!weibo) {
                    platform = SHARE_MEDIA.SINA;
                    thridLogin(platform);
                }
                break;
        }

    }

    private void thridLogin(SHARE_MEDIA platform) {
        mShareAPI.deleteOauth(ThirdCheckBindActivity.this, platform, null);
        mShareAPI.getPlatformInfo(ThirdCheckBindActivity.this, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            Toast.makeText(getApplicationContext(), "Authorize start", Toast.LENGTH_SHORT).show();
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

    private void checkBingThird(Map<String, String> data) {
        switch (platform) {
            case WEIXIN:
                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("openid");
                ApiManager.isBindWeiXin(AccessToken,Uid, new ThirdIsBindObserver(mResultCallBack));
                break;
            case QQ:
                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("openid");
                ApiManager.isBindQQ(AccessToken,Uid, new ThirdIsBindObserver(mResultCallBack));
                break;
            case SINA:
                if (!Utils.isEmpty(data.get("access_token"))) {
                    AccessToken = data.get("access_token");
                } else {
                    AccessToken = data.get("accessToken");
                }
                Uid = data.get("uid");
                ApiManager.isBindWeiBo(AccessToken,Uid, new ThirdIsBindObserver(mResultCallBack));
                break;
        }
    }

    private ThirdLoginDateModel mThirdLoginDateModel;
    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof ThirdCheckBindModel) {
                ThirdCheckBindModel model = (ThirdCheckBindModel) result;
                qq = model.isQq();
                weixin = model.isWeixin();
                weibo = model.isWeibo();
                QQState.setText(qq ? "已绑定" : "未绑定");
                WeiXinState.setText(weixin ? "已绑定" : "未绑定");
                SinaState.setText(weibo ? "已绑定" : "未绑定");
            } else if (result instanceof ThirdBindModel) {
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
                                mThirdLoginDateModel.setForm("bind");
                                startActivity(ThirdLoginActivity.getCallingIntent(ThirdCheckBindActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                showShortToast("已绑定过请选择其他微信号");
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
                                mThirdLoginDateModel.setForm("bind");
                                startActivity(ThirdLoginActivity.getCallingIntent(ThirdCheckBindActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                showShortToast("已绑定过请选择其他QQ号");

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
                                mThirdLoginDateModel.setForm("bind");
                                startActivity(ThirdLoginActivity.getCallingIntent(ThirdCheckBindActivity.this, mThirdLoginDateModel));
                                break;
                            case 1:
                                showShortToast("已绑定过请选择其他新浪号");
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
            dismissLoadingDialog();

            if (o instanceof String)
                showShortToast((String) o);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindState(String message) {
        if (message.equals("bind_qq")) {
            QQState.setText("已绑定");


        } else if (message.equals("bing_sina")) {
            SinaState.setText("已绑定");
        } else if (message.equals("bind_weixin")) {
            WeiXinState.setText("已绑定");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
