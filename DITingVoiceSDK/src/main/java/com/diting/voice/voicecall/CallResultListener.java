package com.diting.voice.voicecall;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/20, 16:54.
 * Description: .
 */

public interface CallResultListener {

    void onCallState(String stateString);

    void onCallError(String errorMsg);

    void onCallPhone();
}
