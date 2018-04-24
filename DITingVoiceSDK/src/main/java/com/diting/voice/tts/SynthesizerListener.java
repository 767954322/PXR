package com.diting.voice.tts;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/13, 11:43.
 * Description: 微软语音合成监听.
 */

public interface SynthesizerListener {

    void onSpeakBegin();

    void onSpeakCompleted();

    void onSpeakError();
}
