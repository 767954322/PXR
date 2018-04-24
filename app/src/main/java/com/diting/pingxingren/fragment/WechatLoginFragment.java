package com.diting.pingxingren.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.HomeActivity;
import com.diting.pingxingren.activity.SettingActivity;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2017/1/6.
 */

public class WechatLoginFragment extends BaseFragment {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private String openId;
    private String unionId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat_login,null);
        Bundle bundle = getArguments();
        if(bundle != null) {
            openId = bundle.getString("openId");
            unionId = bundle.getString("unionId");
        }
        initViews(view);
        initEvents();
        return view;
    }

    private void initViews(View view){
        et_username = view.findViewById(R.id.username);
        et_password =  view.findViewById(R.id.password);
        btn_login =  view.findViewById(R.id.login);
        String username = MySharedPreferences.getUsername();
        String password = MySharedPreferences.getPassword();
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            et_username.setText(username);
            et_password.setText(password);
        }
        userCheck();
    }

    protected void initEvents(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatBind();
            }
        });
        et_username.addTextChangedListener(mtextWatcher);
        et_password.addTextChangedListener(mtextWatcher);
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

    private void userCheck(){
        if(!TextUtils.isEmpty(et_username.getText().toString())&&!TextUtils.isEmpty(et_password.getText().toString())){
            btn_login.setEnabled(true);
        }else {
            btn_login.setEnabled(false);
        }
    }

    //绑定微信
    private void wechatBind() {
        showLoadingDialog("加载中");
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();
        btn_login.setEnabled(false);
        Diting.wechatBind(username, password,openId,unionId, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                MySharedPreferences.saveUser(username,password);
//                startActivity(new Intent(getActivity(),ChatActivity.class));
//                getActivity().finish();
                successLogin();
            }

            @Override
            public void onFailed(VolleyError error) {
                if (error.networkResponse == null) {
                    showShortToast("请求超时！");
                } else {
                    try {
                        showShortToast(new JSONObject(new String(error.networkResponse.data)).getString("message"));
                    } catch (JSONException e) {
                    }
                }
                btn_login.setEnabled(true);
            }
        });
    }

    private void successLogin() {
        Diting.getMyInfo(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String robotName = response.getString("robotName");
                    String companyName = response.getString("companyName");
                    String uniqueId = response.getString("unique_id");
                    MySharedPreferences.saveUniqueId(uniqueId);
                    //MySharedPreferences.savePhoneSwitch(!response.getBoolean("telephoneSwitch"));
                    MySharedPreferences.savePhoneSwitch(response.getInt("telephoneSwitch"));
//                    MyApplication.headPortrait = response.getString("headPortrait");
                    MySharedPreferences.saveHeadPortrait(response.getString("headPortrait"));
                    MySharedPreferences.saveProfile(response.getString("profile"));
                    if(Utils.isEmpty(robotName)||Utils.isEmpty(companyName)){
                        dismissLoadingDialog();
                        showShortToast("绑定成功");
                        startActivity(SettingActivity.class);
                        getActivity().finish();
                    }else {
                        MyApplication.companyName = companyName;
                        MySharedPreferences.saveRobotName(robotName);
                        dismissLoadingDialog();
                        showShortToast("绑定成功");
                        startActivity(HomeActivity.class);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("信息加载失败");
                btn_login.setEnabled(true);
            }
        });
    }

}
