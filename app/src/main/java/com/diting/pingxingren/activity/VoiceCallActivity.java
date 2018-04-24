package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.databinding.ActivityCallVoiceBinding;
import com.diting.pingxingren.util.PermissionUtils;
import com.diting.voice.data.model.CallInfo;
import com.diting.voice.utils.CallEvent;
import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.Constants;
import com.diting.voice.utils.Utils;
import com.diting.voice.voicecall.CallPushProvider;
import com.diting.voice.voicecall.CallResultListener;
import com.diting.voice.voicecall.CallStateListener;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;


/**
 * Creator: Gu FanFan.
 * Date: 2017/7/23, 21:52.
 * Description: 语音通话activity.
 */

public class VoiceCallActivity extends BaseActivity implements View.OnClickListener {

    private ActivityCallVoiceBinding mBinding;
    private VoiceCallActivityHandler mHandler;

    public static Intent getCallingIntent(Context context, String callExt) {
        Intent intent = new Intent();
        intent.setClass(context, VoiceCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Utils.isNotEmpty(callExt)) intent.putExtra("callExt", callExt);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {

        mHandler = new VoiceCallActivityHandler(this);

        requestAudioRecordPermission();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_call_voice);
    }

    private void requestAudioRecordPermission(){
        if (ContextCompat.checkSelfPermission(VoiceCallActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(VoiceCallActivity.this, Manifest.permission.RECORD_AUDIO)) {
                com.diting.pingxingren.util.Utils.showMissingPermissionDialog(VoiceCallActivity.this, "录音");
            } else {
                ActivityCompat.requestPermissions(VoiceCallActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
            }
        }else {
            voiceCall();
        }
    }

    private void voiceCall(){
        CallManagerUtils.getInstance().setCallStateListener(mCallStateListener);
        CallPushProvider pushProvider = new CallPushProvider();
        EMClient.getInstance().callManager().setPushProvider(pushProvider);

        if (CallManagerUtils.getInstance().getCallState() == CallManagerUtils.CallState.DISCONNECTED) {
            // 收到呼叫或者呼叫对方时初始化通话状态监听
            CallManagerUtils.getInstance().setCallState(CallManagerUtils.CallState.CONNECTING);
            CallManagerUtils.getInstance().registerCallStateListener();
            CallManagerUtils.getInstance().attemptPlayCallSound();

            // 如果不是对方打来的，就主动呼叫
            if (!CallManagerUtils.getInstance().isInComingCall()) {
                mCallStateListener.startCountDownTimer();
                CallManagerUtils.getInstance().makeCall(getIntent().getStringExtra("callExt"),MyApplication.toRobotName,MyApplication.toCompanyName,MyApplication.toHeadImgUrl);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initEvents() {
        if (CallManagerUtils.getInstance().isInComingCall()) {
            updateView(true);
            mBinding.callState.setText(R.string.call_connected_is_incoming);
        } else {
            updateView(false);
            mBinding.callState.setText(R.string.call_connecting);
        }



        //mBinding.callName.setText(CallManagerUtils.getInstance().getChatId());
        if(!CallManagerUtils.getInstance().isInComingCall()) {
            mBinding.callName.setText(MyApplication.toRobotName);
        }else {
            //mBinding.callName.setText(((MyApplication)getApplication()).getmCallReceiver().getCallExt().split("#")[0]);
            CallInfo callInfo = new Gson().fromJson(getIntent().getStringExtra("callExt"),CallInfo.class);
            mBinding.callName.setText(callInfo.getRobotName());
        }
        if (CallManagerUtils.getInstance().getCallState() == CallManagerUtils.CallState.ACCEPTED) {
            updateView(false);
            mBinding.callState.setText(R.string.call_accepted);
        }
        mBinding.voiceChangeImage.setActivated(!CallManagerUtils.getInstance().isOpenMic());
        mBinding.speakerChangeImage.setActivated(CallManagerUtils.getInstance().isOpenSpeaker());
        mBinding.speakerChangeImage.setOnClickListener(this);
        mBinding.voiceChangeImage.setOnClickListener(this);
        mBinding.endCallImage.setOnClickListener(this);
        mBinding.answerCallImage.setOnClickListener(this);
        mBinding.rejectCallImage.setOnClickListener(this);
        onSpeaker();
    }

    /**
     * 麦克风开关，主要调用环信语音数据传输方法
     */
    private void onMicrophone() {
        try {
            // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
            if (mBinding.voiceChangeImage.isActivated()) {
                // 设置按钮状态
                mBinding.voiceChangeImage.setActivated(false);
                // 暂停语音数据的传输
                EMClient.getInstance().callManager().resumeVoiceTransfer();
                CallManagerUtils.getInstance().setOpenMic(true);
            } else {
                // 设置按钮状态
                mBinding.voiceChangeImage.setActivated(true);
                // 恢复语音数据的传输
                EMClient.getInstance().callManager().pauseVoiceTransfer();
                CallManagerUtils.getInstance().setOpenMic(false);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扬声器开关
     */
    private void onSpeaker() {
        // 根据按钮状态决定打开还是关闭扬声器
        if (mBinding.speakerChangeImage.isActivated()) {
            // 设置按钮状态
            mBinding.speakerChangeImage.setActivated(false);
            CallManagerUtils.getInstance().closeSpeaker();
            CallManagerUtils.getInstance().setOpenSpeaker(false);
        } else {
            // 设置按钮状态
            mBinding.speakerChangeImage.setActivated(true);
            CallManagerUtils.getInstance().openSpeaker();
            CallManagerUtils.getInstance().setOpenSpeaker(true);
        }
    }



    @Override
    public void onBackPressed() {
        // 拦截返回时间, 进制用户返回。
    }

    private CallStateListener mCallStateListener = new CallStateListener(new CallResultListener() {
        @Override
        public void onCallState(String stateString) {
            Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_UPDATE_STATE;
            message.obj = stateString;
            mHandler.sendMessage(message);
        }

        @Override
        public void onCallError(String errorMsg) {
            Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_UPDATE_STATE;
            message.obj = errorMsg;
            mHandler.sendMessage(message);
        }

        @Override
        public void onCallPhone() {
            /*Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_UPDATE_STATE;
            message.obj = "正在为您拨打对方电话, 请稍后...";
            mHandler.sendMessage(message);*/
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_END);
        }
    });

    private void updateView(boolean isCaller) {
        if (isCaller) {
            mBinding.answerCallImage.setVisibility(View.VISIBLE);
            mBinding.rejectCallImage.setVisibility(View.VISIBLE);
            mBinding.endCallImage.setVisibility(View.GONE);
            mBinding.voiceChangeImage.setVisibility(View.GONE);
            mBinding.speakerChangeImage.setVisibility(View.GONE);
        } else {
            mBinding.endCallImage.setVisibility(View.VISIBLE);
            mBinding.answerCallImage.setVisibility(View.GONE);
            mBinding.rejectCallImage.setVisibility(View.GONE);
            mBinding.voiceChangeImage.setVisibility(View.VISIBLE);
            mBinding.speakerChangeImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接听通话
     */
    public void answerCall() {
        CallManagerUtils.getInstance().answerCall();
        updateView(false);
    }

    /**
     * 拒绝通话
     */
    public void rejectCall() {
        CallManagerUtils.getInstance().rejectCall();
        mCallStateListener.cancelCountDownTimer();
        finish();
    }

    /**
     * 挂断通话
     */
    public void endCall() {
        CallManagerUtils.getInstance().endCall();
        mCallStateListener.cancelCountDownTimer();
        finish();
    }

    public void setCallStateText(String stateText) {
        mBinding.callState.setText(stateText);
        if (stateText.equals(getString(R.string.call_busy))
                || stateText.equals(getString(R.string.call_cancel))
                || stateText.equals(getString(R.string.call_cancel_is_incoming))
                || stateText.equals(getString(R.string.call_disconnected))
                || stateText.equals(getString(R.string.call_reject_is_incoming))
                ||stateText.equals(getString(R.string.call_reject))) {
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_ERROR);
        } else if (stateText.equals("正在为您拨打对方电话, 请稍后...")) {
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_PHONE);
        }
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.answerCallImage :
                mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_ANSWER);
                break;
            case R.id.rejectCallImage :
                mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_REJECT);
                break;
            case R.id.endCallImage :
                mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_END);
                break;
            case R.id.voiceChangeImage :
                onMicrophone();
                break;
            case R.id.speakerChangeImage :
                onSpeaker();
                break;
        }
    }

    private static class VoiceCallActivityHandler extends Handler {
        private WeakReference<VoiceCallActivity> mActivityWeakReference;

        public VoiceCallActivityHandler(VoiceCallActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VoiceCallActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constants.HANDLER_VOICE_CALL_ANSWER :
                        activity.answerCall();
                        break;
                    case Constants.HANDLER_VOICE_CALL_REJECT :
                        activity.rejectCall();
                        break;
                    case Constants.HANDLER_VOICE_CALL_END :
                        CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.CANCEL);
                        activity.endCall();
                        break;
                    case Constants.HANDLER_VOICE_CALL_UPDATE_STATE :
                        activity.setCallStateText((String) msg.obj);
                        break;
                    case Constants.HANDLER_VOICE_CALL_ERROR :
                        activity.finish();
                        break;
                    case Constants.HANDLER_VOICE_CALL_PHONE :
//                        com.diting.semantic.utils.Utils.callPhone(activity,
//                                CallManagerUtils.getInstance().getChatId());
//                        activity.finish();
                        if(!CallManagerUtils.getInstance().isInComingCall()) {
                            CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.NORESPONSE);
                            activity.endCall();
                            PermissionUtils.requestCallPermission(activity, CallManagerUtils.getInstance().getChatId());
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 刷新通话时间显示
     */
    private void refreshCallTime() {
        int t = CallManagerUtils.getInstance().getCallTime();
        mBinding.callState.setText(Utils.second2time(t));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(CallEvent event) {
        if (event.isTime()) {
            // 不论什么情况都检查下当前时间
            refreshCallTime();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == PermissionUtils.MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                PermissionUtils.callPhone(VoiceCallActivity.this,CallManagerUtils.getInstance().getChatId());
            } else
            {
                // Permission Denied
                com.diting.pingxingren.util.Utils.showMissingPermissionDialog(VoiceCallActivity.this,"拨号");
            }
            return;
        }
        if (requestCode == PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                voiceCall();
            } else
            {
                // Permission Denied
                com.diting.pingxingren.util.Utils.showMissingPermissionDialog(VoiceCallActivity.this,"录音");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
