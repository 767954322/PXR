package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView titleBarView;
    private EditText et_new_password;
    private EditText et_confirm_password;
    private Button btn_confirm;
    private Button btn_send_message;
    private EditText et_verify_code;
    private String mUserName;
    private String mPassword;
    private LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        initViews();
        initEvents();
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText("修改密码");
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
        et_new_password =   findViewById(R.id.et_new_password);
        et_confirm_password =   findViewById(R.id.et_confirm_password);
        et_verify_code =   findViewById(R.id.et_verify_code);
        btn_confirm =  findViewById(R.id.btn_confirm);
        btn_send_message =  findViewById(R.id.btn_send_message);
        ll_main =  findViewById(R.id.ll_main);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_send_message.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        mUserName = MySharedPreferences.getUsername();
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(ChangePasswordActivity.this, v);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                changePassword();
                break;
            case R.id.btn_send_message:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void changePassword() {
        mPassword = et_new_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();
        btn_confirm.setEnabled(false);

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
        if (Utils.isEmpty(et_verify_code.getText().toString())) {
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
        btn_confirm.setEnabled(false);
        ApiManager.resetOrChangePassword(mUserName, mPassword, et_verify_code.getText().toString(),
                new ResultMessageObserver(mResultCallBack), false);
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof String) {
                String s = (String) result;
                showShortToast(s);
                if (s.equals("验证码已发送到手机.")) {
                    new MyCountDownTimer(btn_send_message, 60000, 1000).start();
                } else if (s.equals("密码已修改.")) {
                    MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
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
        ApiManager.updatePasswordCode(mUserName, new ResultMessageObserver(mResultCallBack));
    }
}
