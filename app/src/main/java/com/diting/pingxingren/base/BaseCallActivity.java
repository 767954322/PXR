package com.diting.pingxingren.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.voicecall.CallPushProvider;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

/**
 * Creator: Gu FanFan.
 * Date: 2017/9/5, 10:01.
 * Description: .
 */

public class BaseCallActivity extends AppCompatActivity {

    // 震动器
    private Vibrator mVibrator;
    // 呼叫扩展字段
    protected String mCallExt;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        initCallPushProvider();
        // 初始化振动器
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mCallExt = getIntent().getStringExtra("callExt");

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        // 判断当前通话状态，如果已经挂断，则关闭通话界面
        if (CallManagerUtils.getInstance().getCallState() == CallManagerUtils.CallState.DISCONNECTED) {
            finish();
            return;
        } else {
            CallManagerUtils.getInstance().removeFloatWindow();
        }
        super.onResume();
        MobclickAgent.onPageStart(getClassName());
        MobclickAgent.onResume(this);
    }


    protected String getClassName(){
        return this.getClass().getName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClassName());
        MobclickAgent.onPause(this);
    }

    /**
     * 初始化通话推送提供者
     */
    private void initCallPushProvider() {
        CallPushProvider pushProvider = new CallPushProvider();
        EMClient.getInstance().callManager().setPushProvider(pushProvider);
    }

    /**
     * 挂断通话
     */
    protected void endCall() {
        CallManagerUtils.getInstance().endCall();
        finish();
    }

    /**
     * 拒绝通话
     */
    public void rejectCall() {
        CallManagerUtils.getInstance().rejectCall();
        finish();
    }

    /**
     * 接听通话
     */
    public void answerCall() {
        CallManagerUtils.getInstance().answerCall();
    }

    /**
     * 调用系统振动，触发按钮的震动反馈
     */
    protected void vibrate() {
        mVibrator.vibrate(88);
    }

    public String refreshCallTime(int time) {
        int hour = time / 60 / 60;
        int min = time / 60 % 60;
        int sen = time % 60 % 60;
        String timeResult = "";
        if (hour > 9) {
            timeResult = "" + hour;
        } else {
            timeResult = "0" + hour;
        }

        if (min > 9) {
            timeResult += ":" + min;
        } else {
            timeResult += ":0" + min;
        }

        if (sen > 9) {
            timeResult += ":" + sen;
        } else {
            timeResult += ":0" + sen;
        }

        return timeResult;
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }
}