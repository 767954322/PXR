package com.diting.voice;

import android.content.Context;
import android.telecom.InCallService;

import com.diting.voice.data.RequestApiImpl;
import com.diting.voice.utils.BroadcastUtil;
import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.Constants;
import com.diting.voice.utils.LogUtils;
import com.diting.voice.utils.Utils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 17:26.
 * Description: 谛听语音SDK.
 */

public class DITingVoice {

    private static DITingVoice sDITingVoice;
    private static VoiceControl sVoiceControl;
    private String mUserName;

    public void initAll (
            Context context,
            String userName,
            String voiceType,
            Class<?> voiceClass,
            Class<?> videoClass) {
        sVoiceControl = getVoiceControl(context, voiceType);
        initVoiceCall(context, voiceClass,videoClass);
        setUserName(userName);
        createAccount();
    }

    public void initVoiceCall (
            Context context,
            Class<?> voiceClass,
            Class<?> videoClass) {
        EMOptions emOptions = new EMOptions();
        emOptions.setAutoLogin(true);      //自动登录
        EMClient.getInstance().init(context, emOptions);
        EMClient.getInstance().setDebugMode(false);
        CallManagerUtils.getInstance().init(context);
        CallManagerUtils.getInstance().setVoiceClass(voiceClass);
        CallManagerUtils.getInstance().setVideoClass(videoClass);
    }

    public static DITingVoice getInstance() {
        if (sDITingVoice == null)
            sDITingVoice = new DITingVoice();

        return sDITingVoice;
    }

    private DITingVoice() {
    }

    public VoiceControl createVoiceControl (
                boolean hasSemantics,
                ResultCallBack resultCallBack) {
        if (sVoiceControl == null) {
            resultCallBack.onError("初始化引擎失败!");
            return null;
        }

        sVoiceControl.setResultCallBack(resultCallBack);
        sVoiceControl.setRequestApi(RequestApiImpl.newInstance());
        sVoiceControl.setUserName(mUserName);
        sVoiceControl.setHasSemantics(hasSemantics);
        return sVoiceControl;
    }

    private VoiceControl getVoiceControl(Context context, String voiceType) {
        VoiceControl voiceControl;
        switch (voiceType) {
            case Constants.VOICE_TYPE_MICROSOFT :
                voiceControl = MicrosoftVoiceControl.newInstance(context);
                break;
            case Constants.VOICE_TYPE_TENCENT :
                voiceControl = null;
                break;
//            case Constants.VOICE_TYPE_XFYUN :
//                voiceControl = XFYunVoiceControl.newInstance(context);
//                break;
            case Constants.VOICE_TYPE_BAIDU :
                voiceControl = null;
                break;
            default:
                voiceControl = null;
                break;
        }
        return Utils.isNotEmpty(voiceControl) ? voiceControl : null;
    }

    public void registerCallReceiver(Context context, BroadcastUtil.IReceiver receiver) {
        BroadcastUtil.registerReceiver(context, receiver, EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
    }

    public void unRegisterCallReceiver(Context context) {
        BroadcastUtil.unRegisterReceiver(context);
    }

    private void createAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(getUserName(), Constants.DEFAULT_PASSWORD);
                    login(getUserName());
                } catch (HyphenateException e) {
                    int errorCode = e.getErrorCode();
                    switch (errorCode) {
                        case EMError.USER_ALREADY_EXIST :
                            login(getUserName());
                            break;
                        default:
                            LogUtils.e(e.getDescription());
                            break;
                    }
                }
            }
        }).start();
    }

    public void createAccount(final String userName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(userName, Constants.DEFAULT_PASSWORD);
                    login(userName);
                } catch (HyphenateException e) {
                    int errorCode = e.getErrorCode();
                    switch (errorCode) {
                        case EMError.USER_ALREADY_EXIST :
                            login(userName);
                            break;
                        default:
                            LogUtils.e(e.getDescription());
                            break;
                    }
                }
            }
        }).start();
    }

    public void login(final String userName) {
        EMClient.getInstance().login(userName, Constants.DEFAULT_PASSWORD, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.e(userName + "登录成功!");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.e("登录失败!");
                LogUtils.e("errorCode: " + i + ", errorMsg: " + s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    public void onDestroy() {
        EMClient.getInstance().logout(true);
        if (sVoiceControl != null) {
            sVoiceControl.destroy();
            sVoiceControl = null;
        }
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = Utils.isNotEmpty(userName) ? userName : Constants.DITING_USERNAME_XIAODI;
    }
}
