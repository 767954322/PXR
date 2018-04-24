package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * 注册
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView titleBarView;
    private EditText et_username;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_register;
    private Button btn_send_message;
    private EditText et_verify_code;
    private RelativeLayout ll_main;
    private String mUserName;
    private String mPassword;
    private String mConfirmPassword;
    private ImageView iv_agreement;
    private boolean isSelectAgreement = true;
    private TextView tv_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText(R.string.register);
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
        et_password =   findViewById(R.id.et_password);
        et_confirm_password =  findViewById(R.id.et_confirm_password);
        et_verify_code =   findViewById(R.id.et_verify_code);
        btn_register =   findViewById(R.id.btn_register);
        btn_send_message =   findViewById(R.id.btn_send_message);
        ll_main = findViewById(R.id.ll_main);
        iv_agreement = findViewById(R.id.iv_agreement);
        tv_agreement = findViewById(R.id.tv_agreement);

    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_send_message.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        et_username.addTextChangedListener(mTextWatcher);
        et_password.addTextChangedListener(mTextWatcher);
        et_confirm_password.addTextChangedListener(mTextWatcher);
        et_verify_code.addTextChangedListener(mTextWatcher);
        iv_agreement.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(RegisterActivity.this, v);
                return false;
            }
        });

    }

    private void userCheck() {
        if (isLegal()) {
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
        }
    }

    private boolean isLegal() {
        if (TextUtils.isEmpty(et_username.getText().toString())) {


            return false;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
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

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //内容改变前
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            userCheck();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //结果显示在输入框中
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_send_message:
                sendMessage();
                break;
            case R.id.iv_agreement:
                if (isSelectAgreement) {
                    isSelectAgreement = false;
                    iv_agreement.setImageResource(R.drawable.register_agreement_uncheck);

                } else {
                    isSelectAgreement = true;
                    iv_agreement.setImageResource(R.drawable.register_agreement_check);
                }
                break;
            case R.id.tv_agreement:
                Intent intent = new Intent(RegisterActivity.this, WebActivity.class);
                intent.putExtra("url", "http://www.ditingai.com/agreement");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            dismissLoadingDialog();
            if (result instanceof String) {
                String s = (String) result;
                showShortToast(s);
                if (s.equals("验证码已发送到手机.")) {
                    new MyCountDownTimer(btn_send_message, 60000, 1000).start();
                } else if (s.equals("注册成功.")) {
                    MySharedPreferences.saveSafeUserInfo(mUserName, mPassword);
                    startActivity(SettingActivity.class);
                    finish();
                }
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            btn_register.setEnabled(true);
            if (o instanceof String)
                showShortToast((String) o);
        }
    };

    private void register() {
        mUserName = et_username.getText().toString();
        mPassword = et_password.getText().toString();
        mConfirmPassword = et_confirm_password.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            showShortToast("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {

            showShortToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(mConfirmPassword)) {

            showShortToast("确认密码不能为空");
            return;
        }
        if (!Utils.isMobile(mUserName)) {
            showShortToast("手机号格式错误, 请重新填写.");
            return;
        }

        if (!Utils.isPassword(mPassword)) {
            showShortToast("密码格式错误.");
            return;
        }

        if (!mPassword.equals(mConfirmPassword)) {
            showShortToast("两次输入密码不一致.");
            return;
        }

//        if (!isSelectAgreement) {
//            showShortToast("请先同意协议");
//            return;
//        }

        btn_register.setEnabled(false);
        showLoadingDialog("注册中...");
        ApiManager.register(mUserName, mPassword, et_verify_code.getText().toString(), new ResultMessageObserver(mResultCallBack));
    }

    private void sendMessage() {
        btn_send_message.setEnabled(false);
        mUserName = et_username.getText().toString();
        if (!Utils.isMobile(mUserName)) {
            showShortToast("请输入正确的手机号");
            btn_send_message.setEnabled(true);
        } else {
            btn_send_message.setEnabled(true);
            ApiManager.registerCode(mUserName, new ResultMessageObserver(mResultCallBack));
        }
    }
}
