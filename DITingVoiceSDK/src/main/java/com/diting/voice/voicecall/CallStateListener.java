package com.diting.voice.voicecall;

import com.diting.voice.TimerCallBack;
import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.Constants;
import com.diting.voice.utils.CountDownTimerUtils;
import com.diting.voice.utils.LogUtils;
import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/20, 9:49.
 * Description: 通话状态监听类，用来监听通话过程中状态的变化.
 */

public class CallStateListener implements EMCallStateChangeListener {

    private CallResultListener mResultCallBack;
    private CountDownTimerUtils mTimerUtils;

    public CallStateListener(CallResultListener resultCallBack) {
        mResultCallBack = resultCallBack;
        TimerCallBack timerCallBack = new TimerCallBack() {
            @Override
            public void onTick(long time) {
                LogUtils.e("倒计时: " + time);
            }

            @Override
            public void onFinish() {
                mResultCallBack.onCallPhone();
            }
        };
        mTimerUtils = new CountDownTimerUtils(
                Constants.COMMON_COUNT_DOWN_TIME,
                Constants.MILLISECOND,
                timerCallBack);
    }

    public void cancelCountDownTimer() {
        if (mTimerUtils != null)
            mTimerUtils.cancel();
    }

    public void startCountDownTimer(){
        if (mTimerUtils != null)
            mTimerUtils.start();
    }

    @Override
    public void onCallStateChanged(CallState callState, CallError callError) {
        switch (callState) {
            case CONNECTING: // 正在呼叫对方, 没见回调过
                LogUtils.i("正在呼叫对方" + callError);
                CallManagerUtils.getInstance().setCallState(CallManagerUtils.CallState.CONNECTING);
                mResultCallBack.onCallState("正在呼叫对方...");
//                mTimerUtils.start();
                break;
            case CONNECTED: // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                LogUtils.i("正在连接" + callError);
                CallManagerUtils.getInstance().setCallState(CallManagerUtils.CallState.CONNECTED);
                mResultCallBack.onCallState("正在连接...");
                break;
            case ACCEPTED: // 通话已接通
                LogUtils.i("正在通话...");
                CallManagerUtils.getInstance().stopCallSound();
                 CallManagerUtils.getInstance().startCallTime();
                CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.NORMAL);
                CallManagerUtils.getInstance().setCallState(CallManagerUtils.CallState.ACCEPTED);
                mResultCallBack.onCallState("电话已接通");
                mTimerUtils.cancel();
                break;
            case DISCONNECTED: // 通话已中断
                LogUtils.i("通话已结束" + callError);
                // 通话结束，重置通话状态
                if (callError == CallError.ERROR_UNAVAILABLE) {
                    LogUtils.i("对方不在线" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.OFFLINE);
                    mResultCallBack.onCallState("对方不在线");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.ERROR_BUSY) {
                    LogUtils.i("对方正忙" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.BUSY);
                    mResultCallBack.onCallState("对方正忙");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.REJECTED) {
                    LogUtils.i("对方已拒绝" + callError);
                    mTimerUtils.cancel();
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.REJECTED);
                    mResultCallBack.onCallState("对方已拒绝");
                    CallManagerUtils.getInstance().saveCallMessage();
                } else if (callError == CallError.ERROR_NORESPONSE) {
                    LogUtils.i("对方未响应，可能手机不在身边" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.NORESPONSE);
                    mResultCallBack.onCallState("对方未响应, 可能手机不在身边");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.ERROR_TRANSPORT) {
                    LogUtils.i("连接建立失败" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.TRANSPORT);
                    mResultCallBack.onCallState("连接建立失败");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED) {
                    LogUtils.i("双方通讯协议不同" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.DIFFERENT);
                    mResultCallBack.onCallState("双方通讯协议不同");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                    LogUtils.i("双方通讯协议不同" + callError);
                    CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.DIFFERENT);
                    mResultCallBack.onCallState("双方通讯协议不同");
                    mResultCallBack.onCallPhone();
                } else if (callError == CallError.ERROR_NO_DATA) {
                    LogUtils.i("没有通话数据" + callError);
                    mResultCallBack.onCallState("没有通话数据");
                    mResultCallBack.onCallPhone();
                } else {
                    LogUtils.i("通话已结束" + callError);
                    if ((!(CallManagerUtils.getInstance().getEndType() == CallManagerUtils.EndType.NORESPONSE))
                            &&(!(CallManagerUtils.getInstance().getEndType() == CallManagerUtils.EndType.CANCEL))) {
                        CallManagerUtils.getInstance().saveCallMessage();
                    }
                    mResultCallBack.onCallState("通话已结束");

                }
                // 通话结束，保存消息
                //CallManagerUtils.getInstance().endCall();
                //CallManagerUtils.getInstance().saveCallMessage();
                CallManagerUtils.getInstance().reset();
                break;
            case NETWORK_DISCONNECTED:
                LogUtils.i("对方网络不可用");
                mResultCallBack.onCallError("对方网络不可用");
                //mResultCallBack.onCallPhone();
                mResultCallBack.onCallState("通话已结束");
                break;
            case NETWORK_NORMAL:
                LogUtils.i("网络正常");
                break;
            case NETWORK_UNSTABLE:
                if (callError == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                    LogUtils.i("没有通话数据" + callError);
                } else {
                    LogUtils.i("网络不稳定" + callError);
                    mResultCallBack.onCallState("网络不稳定");
                }
                break;
            case VIDEO_PAUSE:
                LogUtils.i("视频传输已暂停");
                break;
            case VIDEO_RESUME:
                LogUtils.i("视频传输已恢复");
                break;
            case VOICE_PAUSE:
                LogUtils.i("语音传输已暂停");
                break;
            case VOICE_RESUME:
                LogUtils.i("语音传输已恢复");
                break;
            default:
                break;
        }
    }

}
