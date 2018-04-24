package com.diting.pingxingren.view;

import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.presenter.ChattingPresenterImpl;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 09:47.
 * Description: .
 */

public interface ChattingView extends BaseView<ChattingPresenterImpl> {

    void updateVoiceLevel(int level);

    void onBeginOfSpeech();

    void onRecordingFinish(float seconds, String filePath, String text);

    void addReminder(ChatMessageItem messageItem, boolean isVoice, String reminderContent, long reminderTime);

    void deleteReminder(ChatMessageItem messageItem, boolean isVoice, long reminderTime);

    void refreshChatRecyclerView(ChatMessageItem msg);

    void refreshChatRecyclerView(List<ChatMessageItem> msg);

    void refreshChatRecyclerView(int position, List<ChatMessageItem> msg);

    void onPhoneCall(String phone);

    void onVoiceCall(String phone);

    void onVideoCall(String phone);
}
