package com.diting.voice;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.diting.voice.data.ObserverCallback;
import com.diting.voice.data.RequestApiImpl;
import com.diting.voice.data.body.ChatRequestBody;
import com.diting.voice.data.model.PublicChatInfoModel;
import com.diting.voice.data.observer.PublicChatInfoObserver;
import com.diting.voice.utils.Constants;
import com.diting.voice.utils.Utils;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 9:48.
 * Description: .
 */

public abstract class VoiceControl {

    ResultCallBack mResultCallBack;
    private RequestApiImpl mApi;
    private ChatRequestBody mChatRequestBody;
    long mStartRecognitionTime; // 开始录音时间
    long mRecognitionTime; // 录音时间, 单位秒
    SoundPool mSoundPool;
    boolean mHasSemantics= true;
    private boolean mIsLoaded;
    private int mLoadSoundID;
    private int mSoundStreamID;

    private ObserverCallback mObserverCallback = new ObserverCallback() {
        @Override
        public void onComplete() {
        }

        @Override
        public void onNext(Object o) {
            if (o instanceof PublicChatInfoModel) {
                PublicChatInfoModel chatInfoModel = (PublicChatInfoModel) o;
                mResultCallBack.onChatResult(chatInfoModel.getAnswer());
                startTTS(chatInfoModel.getAnswer());
            }
        }

        @Override
        public void onNext(List<?> objects) {
        }

        @Override
        public void onError(String msg) {
            mResultCallBack.onError(msg);
        }
    };

    VoiceControl() {
        mChatRequestBody = new ChatRequestBody();
        mChatRequestBody.setUuid(Utils.getRandomString(12));

        if (Utils.hasLollipop()) {
            mSoundPool = new SoundPool.Builder().setMaxStreams(1).build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                /*soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                startRecognition();*/
                playSound();
                mIsLoaded = true;
            }
        });
    }

    void setUserName(String userName) {
        if (mChatRequestBody == null)
            mChatRequestBody = new ChatRequestBody();
        mChatRequestBody.setUsername(Utils.isNotEmpty(userName) ? userName : Constants.DITING_USERNAME_XIAODI);
    }

    void setHasSemantics(boolean hasSemantics) {
        mHasSemantics = hasSemantics;
    }

    void setRequestApi(RequestApiImpl requestApi) {
        mApi = requestApi;
    }

    void setResultCallBack(ResultCallBack resultCallBack) {
        mResultCallBack = resultCallBack;
    }

    void getTurnedChatInfo(String result) {
        mChatRequestBody.setQuestion(result);
        mApi.getPublicChatInfo(mChatRequestBody, new PublicChatInfoObserver(mObserverCallback));
    }

    /**
     * 开始语音播报.
     */
    public abstract void startTTS(String tts);

    /**
     * 结束语音播报.
     */
    public abstract void endTTS();

    /**
     * 开始语音识别.
     */
    public abstract void startRecognition();

    /**
     * 结束语音识别.
     */
    public abstract void endRecognition();

    /**
     * 释放资源.
     */
    public abstract void destroy();

    public void attemptPlaySound(Context context) {
        if (mIsLoaded) {
            playSound();
        } else {
            mLoadSoundID = mSoundPool.load(context, R.raw.ding, 1);
        }
    }

    private void playSound() {
        if (mSoundPool != null) {
            mSoundStreamID = mSoundPool.play(mLoadSoundID, // 播放资源id；就是加载到SoundPool里的音频资源顺序
                    0.5f,   // 左声道音量
                    0.5f,   // 右声道音量
                    1,      // 优先级，数值越高，优先级越大
                    0,     // 是否循环；0 不循环，-1 循环，N 表示循环次数
                    1);     // 播放速率；从0.5-2，一般设置为1，表示正常播放
        }

        startRecognition();
    }

    public void stopSound() {
        if (mSoundPool != null) {
            // 停止播放音效
            mSoundPool.stop(mSoundStreamID);
        }
    }
}
