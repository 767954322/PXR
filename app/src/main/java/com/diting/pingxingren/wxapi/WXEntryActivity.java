package com.diting.pingxingren.wxapi;


import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {



//public class WXEntryActivity extends WXCallbackActivity /* implements IWXAPIEventHandler */{

//    private String code;
//    private IWXAPI api;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        api = WXAPIFactory.createWXAPI(this, Const.APP_ID);
//        api.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            switch (baseResp.errCode) {
//                case BaseResp.ErrCode.ERR_OK:
//                    break;
//                case BaseResp.ErrCode.ERR_COMM:
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//
//                    break;
//            }
//        } else {
//            switch (baseResp.errCode) {
//                case BaseResp.ErrCode.ERR_OK:
//                    if (baseResp instanceof SendAuth.Resp) {
//                        code = ((SendAuth.Resp) baseResp).code;
//                        Log.e("ErrorCode", "code---" + code);
//                        getAccess_token(code);
//                    }
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    finish();
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    finish();
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 通过code获取access_token
//     *
//     * @param code
//     * @return
//     */
//    private void getAccess_token(String code) {
//        final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + WXAppSecret +
//                "&code=" + code + "&grant_type=authorization_code";
//        Diting.getAccessToken(url, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                try {
//                    final String openId = response.getString("openid");
//                    final String unionId = response.getString("unionid");
//                    Log.d("openId: ", openId);
//                    Log.d("unionId: ", unionId);
//                    wechatLogin(openId, unionId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                finish();
//            }
//        });
//    }
//
//    private void wechatLogin(final String openId, final String unionId) {
//        Diting.wechatLogin(openId, unionId, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//
//                successLogin();
////                Toast.makeText(WXEntryActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
////                startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                Intent intent = new Intent(WXEntryActivity.this, WechatBindActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("openId", openId);
//                bundle.putString("unionId", unionId);
//                intent.putExtra("wechat", bundle);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    private void successLogin() {
//        Diting.getMyInfo(new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                try {
//                    String robotName = response.getString("robotName");
//                    String companyName = response.getString("companyName");
//                    String uniqueId = response.getString("unique_id");
//                    MySharedPreferences.saveUniqueId(uniqueId);
//                    //MySharedPreferences.savePhoneSwitch(!response.getBoolean("telephoneSwitch"));
//                    MySharedPreferences.savePhoneSwitch(response.getInt("telephoneSwitch"));
////                    MyApplication.headPortrait = response.getString("headPortrait");
//                    MySharedPreferences.saveHeadPortrait(response.getString("headPortrait"));
//                    MySharedPreferences.saveProfile(response.getString("profile"));
//                    if (Utils.isEmpty(robotName) || Utils.isEmpty(companyName)) {
//                        Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(WXEntryActivity.this, SettingActivity.class));
//                        finish();
//                    } else {
//                        MyApplication.companyName = companyName;
//                        MySharedPreferences.saveRobotName(robotName);
//                        Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(WXEntryActivity.this, HomeActivity.class));
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                Toast.makeText(WXEntryActivity.this, "信息加载失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
