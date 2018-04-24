package com.diting.pingxingren.custom;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.diting.pingxingren.R;
import com.diting.pingxingren.entity.VoiceChatMessage;
import com.diting.pingxingren.util.DialogManager;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/4/14.
 */
public class AudioRecordButton3 extends AppCompatButton {
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    private static final int DISTANCE_Y_CANCEL = 50;
    private int mCurrentState = STATE_NORMAL;
    // 已经开始录音
    private boolean isRecording = false;
    private DialogManager mDialogManager;
    private float mTime = 0;
    // 是否触发了onLongClick，准备好了
    private boolean mReady;
    private String mRecognizerPath;

    private SpeechRecognizer mIat;
    private StringBuffer mResultBuffer;
    private long mStartRecognitionTime;
    private float mRecognitionTime;
    private VoiceChatMessage mVoiceChatMessage;

    /**
     * 先实现两个参数的构造方法，布局会默认引用这个构造方法， 用一个 构造参数的构造方法来引用这个方法 * @param context
     */
    public AudioRecordButton3(Context context) {
        this(context, null);
    }

    public AudioRecordButton3(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(getContext());

        InitListener initCallBack = new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS)
                    ToastUtils.showShortToastSafe("讯飞语音引擎初始化失败!");
            }
        };
        mIat = SpeechRecognizer.createRecognizer(context, initCallBack);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startRecording();
                return false;
            }
        });
    }

    private void setParams() {
        mRecognizerPath = Environment.getExternalStorageDirectory() + "/diting/" + TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM2) + ".wav";
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, mRecognizerPath);
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
    }

    //开始录音
    private void startRecording() {
        mStartRecognitionTime = TimeUtil.getNowTimeMills();
        mTime = 0;
        mReady = true;
        setParams();
        mIat.startListening(mRecognizerListener);
    }

    /**
     * 录音完成后的回调，回调给activity，可以获得time和文件的路径
     */
    public interface AudioFinishRecorderListener {
        void onStart();

        void onFinished(float seconds, String filePath, String text);
    }

    private AudioFinishRecorderListener mListener;

    /**
     * 设置录音成功调用的接口
     *
     * @param listener
     */
    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mListener = listener;
    }

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // 准备三个常量
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_DIALOG_DISMISS = 0X112;
    private static final int MSG_RECORDING_FINISH = 0X113;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示应该是在audio end prepare之后回调
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    // 需要开启一个线程来变换音量
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGE:
                    // mDialogManager.updateVoiceLevel(7);
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;
                case MSG_RECORDING_FINISH:
                    mIat.stopListening();
                    mDialogManager.dismissDialog();
                    /*VoiceChatMessage voiceChatMessage = (VoiceChatMessage) msg.obj;
                    if (voiceChatMessage != null)
                        mListener.onFinished(voiceChatMessage.getRecognitionTime(),
                                voiceChatMessage.getRecognitionPath(), voiceChatMessage.getRecognitionText());*/
                    break;
            }
        }
    };

    /**
     * 直接复写这个监听函数
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    // 根据x，y来判断用户是否想要取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 首先判断是否有触发onLongClick事件，没有的话直接返回reset
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                // 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mIat.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);// 持续1.3s
                } else if (mCurrentState == STATE_RECORDING) {//正常录制结束
                    // mIat.stopListening();
                    mHandler.sendEmptyMessage(MSG_RECORDING_FINISH);
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    // cancel
                    mIat.cancel();
                    mHandler.sendEmptyMessage(MSG_DIALOG_DISMISS);
                }
                reset();// 恢复标志位
                break;
        }
        return super.onTouchEvent(event);
    }

    /*
     * 恢复标志位以及状态
     */
    private void reset() {
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    //改变状态
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    this.setBackgroundResource(R.drawable.button_recordnormal);
                    this.setText("按住 说话");
                    break;
                case STATE_RECORDING:
                    this.setBackgroundResource(R.drawable.button_recording);
                    this.setText("松开 结束");
                    if (isRecording) {
                        mDialogManager.recording(R.string.shouzhishanghua);
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    this.setBackgroundResource(R.drawable.button_recording);
                    this.setText("松开手指，取消录音");
                    // dialog want to cancel
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

    @Override
    public boolean onPreDraw() {
        return false;
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            mDialogManager.updateVoiceLevel(i % 3);
        }

        @Override
        public void onBeginOfSpeech() {
            mListener.onStart();
            mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            try {
                JSONTokener JSONToken = new JSONTokener(recognizerResult.getResultString());
                JSONObject JSONObject = new JSONObject(JSONToken);
                String displayText;

                if (mResultBuffer == null)
                    mResultBuffer = new StringBuffer();

                String result = getResult(JSONObject);
                mResultBuffer.append(result);

                if (b) {
                    displayText = mResultBuffer.toString();
                    long endRecognitionTime = TimeUtil.getNowTimeMills();
                    BigDecimal bigDecimal = new BigDecimal((endRecognitionTime - mStartRecognitionTime) / 1000);
                    mRecognitionTime = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                    /*if (mVoiceChatMessage == null)
                        mVoiceChatMessage = new VoiceChatMessage();
                    mVoiceChatMessage.setRecognitionTime(mRecognitionTime);
                    mVoiceChatMessage.setRecognitionText(displayText);
                    mVoiceChatMessage.setRecognitionPath(mRecognizerPath);
                    Message message = new Message();
                    message.what = MSG_RECORDING_FINISH;
                    message.obj = mVoiceChatMessage;
                    mHandler.sendMessage(message);*/
                    mListener.onFinished(mRecognitionTime, mRecognizerPath, displayText);
                    mResultBuffer = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            String errorMsg = speechError.getErrorDescription();

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    /**
     * 处理识别JSON串.
     */
    public String getResult(JSONObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONArray words = jsonObject.getJSONArray("ws");
            JSONArray items;
            JSONObject word;
            for (int i = 0; i < words.length(); i++) {
                items = words.getJSONObject(i).getJSONArray("cw");
                word = items.getJSONObject(0);
                builder.append(word.getString("w"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
