package com.diting.pingxingren.entity;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 02, 17:16.
 * Description: .
 */

public class VoiceChatMessage {

    private float mRecognitionTime;
    private String mRecognitionText;
    private String mRecognitionPath;

    public float getRecognitionTime() {
        return mRecognitionTime;
    }

    public void setRecognitionTime(float recognitionTime) {
        mRecognitionTime = recognitionTime;
    }

    public String getRecognitionText() {
        return mRecognitionText;
    }

    public void setRecognitionText(String recognitionText) {
        mRecognitionText = recognitionText;
    }

    public String getRecognitionPath() {
        return mRecognitionPath;
    }

    public void setRecognitionPath(String recognitionPath) {
        mRecognitionPath = recognitionPath;
    }
}
