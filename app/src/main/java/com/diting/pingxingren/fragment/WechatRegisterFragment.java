package com.diting.pingxingren.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.SettingActivity;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.MyCountDownTimer;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 2017/1/6.
 */

public class WechatRegisterFragment extends BaseFragment implements View.OnClickListener{
    private EditText et_username;
    private EditText et_password;
    private Button btn_register;
    private Button btn_send_message;
    private EditText et_verify_code;
    private String openId;
    private String unionId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat_register,null);
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
        et_username =  view.findViewById(R.id.et_username);
        et_password =  view.findViewById(R.id.et_password);
        et_verify_code =  view.findViewById(R.id.et_verify_code);
        btn_register =  view.findViewById(R.id.btn_register);
        btn_send_message =  view.findViewById(R.id.btn_send_message);
    }

    protected void initEvents(){
        btn_send_message.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        et_username.addTextChangedListener(mtextWatcher);
        et_password.addTextChangedListener(mtextWatcher);
        et_verify_code.addTextChangedListener(mtextWatcher);
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
        if(isLegal()){
            btn_register.setEnabled(true);
        }else {
            btn_register.setEnabled(false);
        }
    }
    private boolean isLegal(){
        if(TextUtils.isEmpty(et_username.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(et_password.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(et_verify_code.getText().toString())){
            return false;
        }
        return true;
    }

    private void register(){
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();
        if(!Utils.isPassword(password)){
            Toast.makeText(getContext(),"密码格式错误",Toast.LENGTH_SHORT).show();
        }else {
            btn_register.setEnabled(false);
            Diting.register(username, password, et_verify_code.getText().toString(),openId, unionId, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    showShortToast("注册成功");
                    MySharedPreferences.saveUser(username, password);
                    startActivity(new Intent(getActivity(),SettingActivity.class));
                    getActivity().finish();
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
                    btn_register.setEnabled(true);
                }
            });
        }
    }

    private void sendMessage(){
        String username = et_username.getText().toString();
        if(!Utils.isMobile(username)){
            Toast.makeText(getContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
        }else {
            Diting.sendMessage(username, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    new MyCountDownTimer(btn_send_message,60000,1000).start();
                    Toast.makeText(getContext(),"短信已发送",Toast.LENGTH_SHORT).show();
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
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_send_message:
                sendMessage();
                break;
        }
    }
}
