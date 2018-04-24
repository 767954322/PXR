package com.diting.pingxingren.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2017/3/23.
 */

public class SpeakApiBase {
    private static final String TAG = "SpeakApiBase";
    private Context context;
    //添加文字转换为语音
    private SpeechSynthesizer mTts;
    private SpeechRecognizer mIat;


    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };
    public SpeakApiBase(Context context) {
        this.context = context;
        mTts = SpeechSynthesizer.createSynthesizer(context, mInitListener);
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
    }



    public void destroyTts() {
        if (mTts != null) {
            mTts.destroy();
        }
    }
    public void destroyIat() {
        if (mIat != null) {
            mIat.destroy();
        }
    }

    public void setTtsParameter(String key, String value) {
        mTts.setParameter(key, value);
    }

    public int text2Voice(String text, String filePath, SynthesizerListener mTtsListener){
        return mTts.synthesizeToUri(text, filePath, mTtsListener);
    }

    public void recognizeStream(){
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.d(TAG,"识别失败,错误码：" + ret);
        } else {
            File file = new File("mnt/sdcard/reverseme.pcm");
//读取文件
            byte[] music = null;
            try {
                InputStream is = new FileInputStream(file);
                music = new byte[is.available()];
                is.read(music);
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (null != music) {
                // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别
                mIat.writeAudio(music, 0, music.length);
                mIat.stopListening();
            } else {
                mIat.cancel();
                Log.d(TAG,"读取音频流失败");
            }
        }
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        private StringBuffer sb = new StringBuffer();
        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG,"开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            Log.d(TAG,error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG,"结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = Utils.parseIatResult(results.getResultString());
            sb.append(text);
            if(isLast) {
                //TODO 最后的结果
                Log.d(TAG,"识别结果是：" + sb.toString());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.d(TAG,"当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

}
