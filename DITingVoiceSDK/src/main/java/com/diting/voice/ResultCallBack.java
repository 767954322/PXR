package com.diting.voice;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 16:13.
 * Description: 处理结果回调.
 */

public interface ResultCallBack {

    void onSpeechFinish();

    void onChatResult(String chatResult);

    void onRecognitionResult(String recognitionResult);

    void onRecognitionTime(int recognitionTime);

    void onError(String errorMsg);
}
