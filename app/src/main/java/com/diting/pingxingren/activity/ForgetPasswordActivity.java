package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.util.MyCountDownTimer;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2017/1/3.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView titleBarView;
    private EditText et_username;
    private EditText et_new_password;
    private EditText et_confirm_password;
    private Button btn_confirm;
    private Button btn_send_message;
    private EditText et_verify_code;
    private LinearLayout ll_main;

    private String mUserName;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);
        initViews();
        initEvents();
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText("忘记密码");
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
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
        et_username =  findViewById(R.id.et_username);
        et_new_password =   findViewById(R.id.et_new_password);
        et_confirm_password =  findViewById(R.id.et_confirm_password);
        et_verify_code =   findViewById(R.id.et_verify_code);
        btn_confirm =   findViewById(R.id.btn_confirm);
        btn_send_message =  findViewById(R.id.btn_send_message);
        ll_main =   findViewById(R.id.ll_main);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_send_message.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        et_username.addTextChangedListener(mtextWatcher);
        et_new_password.addTextChangedListener(mtextWatcher);
        et_confirm_password.addTextChangedListener(mtextWatcher);
        et_verify_code.addTextChangedListener(mtextWatcher);
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(ForgetPasswordActivity.this, v);
                return false;
            }
        });
    }

    private void userCheck() {
        if (isLegal()) {
            btn_confirm.setEnabled(true);
        } else {
            btn_confirm.setEnabled(false);
        }
    }

    private boolean isLegal() {
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(et_new_password.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(et_confirm_password.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(et_verify_code.getText().toString())) {
            return false;
        }
        return true;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                resetPassword();
                break;
            case R.id.btn_send_message:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void resetPassword() {
        mUserName = et_username.getText().toString();
        mPassword = et_new_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();
        String verify_code = et_verify_code.getText().toString();
        btn_confirm.setEnabled(false);
        if (TextUtils.isEmpty(mUserName)) {
            showShortToast("请填写您的账号");
            btn_confirm.setEnabled(true);
            return;
        }
        if (Utils.isEmpty(mPassword)) {
            showShortToast("密码不能为空");
            btn_confirm.setEnabled(true);
            return;
        }
        if (Utils.isEmpty(confirmPassword)) {
            showShortToast("确认密码不能为空");
            btn_confirm.setEnabled(true);
            return;
        }
        if (Utils.isEmpty(verify_code)) {
            showShortToast("验证码不能为空");
            btn_confirm.setEnabled(true);
            return;
        }
        if (!Utils.isPassword(mPassword)) {
            showShortToast("密码格式错误");
            btn_confirm.setEnabled(true);
            return;
        }
        if (!mPassword.equals(confirmPassword)) {
            showShortToast("两次输入密码不一致");
            btn_confirm.setEnabled(true);
            return;
        }

        ApiManager.resetOrChangePassword(mUserName, mPassword, et_verify_code.getText().toString(),
                new ResultMessageObserver(mResultCallBack), true);
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof String) {
                String s = (String) result;
                showShortToast(s);
                if (s.equals("验证码已发送到手机.")) {
                    new MyCountDownTimer(btn_send_message, 60000, 1000).start();
                } else if (s.equals("找回成功, 请重新登录.")) {
                    MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
                    startActivity(LoginActivity.class);
                    finish();
                }
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
    };

    private void sendMessage() {
        btn_send_message.setEnabled(false);
        mUserName = et_username.getText().toString();
        if (!Utils.isMobile(mUserName)) {
            showLongToast("请输入正确的手机号");
            btn_send_message.setEnabled(true);
        } else {
            btn_send_message.setEnabled(true);
            ApiManager.updatePasswordCode(mUserName, new ResultMessageObserver(mResultCallBack));
        }
    }

}
