package com.diting.pingxingren.presenter;

import com.diting.pingxingren.entity.ChatMessageItem;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 09:48.
 * Description: .
 */

public interface ChattingPresenter extends BasePresenter {

    /**
     * 开始录制.
     */
    void startRecording(long startRecordingTime);

    /**
     * 停止录制.
     */
    void stopRecording();

    /**
     * 取消录制.
     */
    void cancelRecording();

    /**
     * 发送私信消息.
     */
    void sendMailMsg(String mail);

    /**
     * 发送动态消息.
     */
    void sendProfileMsg(String profile, String headImage);

    /**
     * 获取当前位置.
     */
    void getLocation();

    /**
     * 获取私信消息.
     */
    void getPrivateMsgCount();

    /**
     * 保存聊天记录.
     */
    void saveChatMsg(ChatMessageItem msg);

    /**
     * 保存聊天记录.
     */
    void saveChatMsg(ChatMessageItem msg, boolean isVoice);

    /**
     * 获取聊天记录.
     */
    void getChatMsg(int position, String userName);

    /**
     * 发送语音消息.
     */
    void sendVoiceMessage(float seconds, String filePath, String text);

    /**
     * 发送文本消息.
     */
    void sendTextMessage(String text);

    /**
     * 获取答案.
     */
    void getAnswer(String text, boolean isVoice);
}
