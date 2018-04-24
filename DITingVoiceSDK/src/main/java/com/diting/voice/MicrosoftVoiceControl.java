package com.diting.voice;

import android.content.Context;

import com.diting.voice.tts.Synthesizer;
import com.diting.voice.tts.SynthesizerListener;
import com.diting.voice.tts.Voice;
import com.diting.voice.utils.Constants;
import com.diting.voice.utils.LogUtils;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 15:33.
 * Description: 微软语音控制器.
 */

class MicrosoftVoiceControl extends VoiceControl implements ISpeechRecognitionServerEvents {

    private static MicrosoftVoiceControl sVoiceControl;
    private Synthesizer mSynthesizer;
    private MicrophoneRecognitionClient mRecognitionClient;
    private boolean isSpeeching = false;
    private boolean isRecognition = false;

    static MicrosoftVoiceControl newInstance(Context context) {
        if (sVoiceControl == null) {
            synchronized (MicrosoftVoiceControl.class) {
                if (sVoiceControl == null)
                    sVoiceControl = new MicrosoftVoiceControl(context);
            }
        }
        return sVoiceControl;
    }

    private MicrosoftVoiceControl(final Context context) {
        // 文本转语音
        SynthesizerListener synthesizerListener = new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                isSpeeching = true;
            }

            @Override
            public void onSpeakCompleted() {
                isSpeeching = false;
                mResultCallBack.onSpeechFinish();
                attemptPlaySound(context);
            }

            @Override
            public void onSpeakError() {
                isSpeeching = false;
            }
        };

        mSynthesizer = new Synthesizer(Constants.MICROSOFT_API_KEY, synthesizerListener);
        mSynthesizer.SetVoice(new Voice("zh-CN",
                "Microsoft Server Speech Text to Speech Voice (zh-CN, HuihuiRUS)",
                Voice.Gender.Male, true), null);

        // 语音识别
        mRecognitionClient =
                SpeechRecognitionServiceFactory.createMicrophoneClient(
                        SpeechRecognitionMode.ShortPhrase,
                        "zh-CN",
                        this,
                        Constants.MICROSOFT_API_KEY
                );
    }

    @Override
    public void onPartialResponseReceived(String response) {
        LogUtils.e("-----onPartialResponseReceived-----");
        LogUtils.e("response: " + response);
        LogUtils.e("----------");
        mResultCallBack.onRecognitionResult(response);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        int recognitionStatus = recognitionResult.RecognitionStatus.getValue();
        LogUtils.e("ResultStatus: " + recognitionStatus);

        int length = recognitionResult.Results.length;
        if (length > 0) {
            String displayText = recognitionResult.Results[0].DisplayText;
            String lexicalForm = recognitionResult.Results[0].LexicalForm;

            LogUtils.e("-----onFinalResponseReceived-----");
            LogUtils.e("LexicalForm: " + lexicalForm);
            LogUtils.e("DisplayText: " + displayText);
            LogUtils.e("----------");

            if (recognitionStatus == RecognitionStatus.RecognitionSuccess.getValue()) {
                mResultCallBack.onRecognitionResult(displayText);
                endRecognition();
                if (mHasSemantics) getTurnedChatInfo(displayText);
            } else {
                // Error.
                LogUtils.e("Error status: " + recognitionStatus);
            }
        } else {
            // Error.
            mResultCallBack.onError("您好想没有说话");
        }
    }

    @Override
    public void onIntentReceived(String payload) {
        LogUtils.e("-----onIntentReceived-----");
        LogUtils.e("payload: " + payload);
        LogUtils.e("----------");
    }

    @Override
    public void onError(int i, String s) {
        LogUtils.e("-----onError-----");
        LogUtils.e("errorCode: " + i);
        LogUtils.e("errorMsg: " + s);
        LogUtils.e("----------");
    }

    @Override
    public void onAudioEvent(boolean recording) {
        LogUtils.e("-----onAudioEvent-----");
        LogUtils.e("recording: " + recording);
        LogUtils.e("----------");
        isRecognition = recording;
        if (recording) {
            mStartRecognitionTime = System.currentTimeMillis();
        } else {
            long endRecognitionTime = System.currentTimeMillis();
            mRecognitionTime = (endRecognitionTime - mStartRecognitionTime) / 1000;
            mResultCallBack.onRecognitionTime((int) mRecognitionTime);
        }
    }

    public boolean isRecognition() {
        return isRecognition;
    }

    @Override
    public void startTTS(String tts) {
        if (isSpeeching)
            endTTS();
        else
            mSynthesizer.SpeakToAudio(tts);
    }

    @Override
    public void endTTS() {
        mSynthesizer.stopSound();
    }

    @Override
    public void startRecognition() {
        if (isSpeeching)
            return;

        if (isRecognition())
            endRecognition();
        else
            mRecognitionClient.startMicAndRecognition();
    }

    @Override
    public void endRecognition() {
        mRecognitionClient.endMicAndRecognition();
    }

    @Override
    public void destroy() {
        endTTS();
        mSynthesizer = null;
        endRecognition();
        mRecognitionClient = null;
        stopSound();
    }
}
