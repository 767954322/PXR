package com.diting.voice.voicecall;

import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.LogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/20, 16:39.
 * Description: .
 */

public class CallPushProvider implements EMCallManager.EMCallPushProvider {

    @Override
    public void onRemoteOffline(String username) {
        LogUtils.e("onRemoteOffline");
        EMMessage message = EMMessage.createTxtSendMessage("有人正在呼叫你, 请接听...", username);
        if (CallManagerUtils.getInstance().getCallType() == CallManagerUtils.CallType.VIDEO) {
            message.setAttribute("attr_call_video", true);
        } else {
            message.setAttribute("attr_call_voice", true);
        }
        // 设置强制推送
        message.setAttribute("em_force_notification", "true");
        // 设置自定义推送提示
        JSONObject extObj = new JSONObject();
        try {
            extObj.put("em_push_title", "有人正在呼叫你, 请接听...");
            extObj.put("extern", "定义推送扩展内容");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("em_apns_ext", extObj);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override public void onSuccess() {

            }

            @Override public void onError(int i, String s) {

            }

            @Override public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}
