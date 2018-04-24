package com.diting.voice.voicecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.LogUtils;
import com.hyphenate.chat.EMClient;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/24, 15:05.
 * Description: 通话呼叫监听广播实现, 用来监听其他账户对自己的呼叫.
 */

public class CallReceiver extends BroadcastReceiver {

    private  String callExt;

    public CallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 判断环信是否登录成功
        if (!EMClient.getInstance().isLoggedInBefore()) {
            return;
        }

        Intent callIntent = new Intent();
        // 呼叫方的username
        String callFrom = intent.getStringExtra("from");
        // 呼叫类型，有语音和视频两种
        String callType = intent.getStringExtra("type");
        // 呼叫接收方，
        String callTo = intent.getStringExtra("to");


        // 获取通话时的扩展字段
        callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
        LogUtils.d("callExt",callExt);
        if (!"video".equals(callType)) {
            CallManagerUtils.getInstance().setCallType(CallManagerUtils.CallType.VOICE);
            callIntent.setClass(context, CallManagerUtils.getInstance().getVoiceClass());
        }else {
            CallManagerUtils.getInstance().setCallType(CallManagerUtils.CallType.VIDEO);
            callIntent.setClass(context, CallManagerUtils.getInstance().getVideoClass());
        }
        // 初始化通化管理类的一些属性
        CallManagerUtils.getInstance().setChatId(callFrom);
        CallManagerUtils.getInstance().setInComingCall(true);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.putExtra("callExt",callExt);
        context.startActivity(callIntent);
    }

    public String getCallExt() {
        return callExt;
    }
}
