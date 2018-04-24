package com.diting.pingxingren.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.diting.pingxingren.R;
import com.diting.pingxingren.util.DialogManager;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.math.BigDecimal;

import static com.iflytek.cloud.ErrorCode.ERROR_AUDIO_RECORD;

public class AudioRecordButton extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = "AudioRecordButton";
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int OVERTIME = 60;
    private int mCurrentState = STATE_NORMAL;
    // 已经开始录音
    private boolean isRecording = false;
    private DialogManager mDialogManager;
    private float mTime = 0;
    // 是否触发了onlongclick，准备好了
    private boolean mReady;
    private String saveDir = FileSaveUtil.voice_dir;
    private SpeechRecognizer mIat;
    private String filePath;
    private StringBuffer sb;

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

    /**
     * 先实现两个参数的构造方法，布局会默认引用这个构造方法， 用一个 构造参数的构造方法来引用这个方法 * @param context
     */

    public AudioRecordButton(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AudioRecordButton(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mDialogManager = new DialogManager(getContext());
        setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mListener.onStart();
                mReady = true;
                setParams();
                sb = new StringBuffer();
                mIat.startListening(recognizerListener);
                return false;
            }
        });
        // TODO Auto-generated constructor stub
    }

    private void setParams() {
        filePath = Environment.getExternalStorageDirectory() + "/diting/" + TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM2) + ".wav";
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, filePath);
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
    }

    private RecognizerListener recognizerListener = new RecognizerListener() {
        private Long time1, time2;

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            mDialogManager.updateVoiceLevel(i % 3 + 1);
        }

        @Override
        public void onBeginOfSpeech() {
            if (isTouch) {
                time1 = TimeUtil.getNowTimeMills();
                mTime = 0;
                mDialogManager.showRecordingDialog();
                isRecording = true;
                new Thread(mGetVoiceLevelRunnable).start();
            }
        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String text = Utils.parseIatResult(recognizerResult.getResultString());
            sb.append(text);
            if (b) {
                time2 = TimeUtil.getNowTimeMills();
                isRecording = false;
                Log.d(TAG, "识别结果是：" + sb.toString());
                if (mListener != null) {// 并且callbackActivity，保存录音
                    BigDecimal bigDecimal = new BigDecimal((time2 - time1) / 1000);
                    float f1 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP)
                            .floatValue();
                    mListener.onFinished(f1, filePath, sb.toString());
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if (speechError.getErrorCode() == ERROR_AUDIO_RECORD) {
                Toast.makeText(getContext(), "录音权限被屏蔽或者录音设备损坏！\n请在设置中检查是否开启权限！",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "您并没有说话",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /**
     * 录音完成后的回调，回调给activiy，可以获得mtime和文件的路径
     */
    public interface AudioFinishRecorderListener {
        void onStart();

        void onFinished(float seconds, String filePath, String text);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(
            AudioFinishRecorderListener listener) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 直接复写这个监听函数
     */
    private boolean isTouch = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
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
                // 首先判断是否有触发onlongclick事件，没有的话直接返回reset
                isTouch = false;
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                // 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mIat.cancel();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDialogManager.dismissDialog();
                        }
                    }, 1300);
                } else if (mCurrentState == STATE_RECORDING) {// 正常录制结束
                    //isRecording = false;
                    mDialogManager.dismissDialog();
                    mIat.stopListening();
//				BigDecimal b = new BigDecimal(mTime);
//				float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP)
//						.floatValue();
//				mListener.onFinished(f1, filePath, sb.toString());
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    //mAudioManager.cancel();
                    mIat.cancel();
                    mDialogManager.dismissDialog();
                }
                isRecording = false;
                reset();// 恢复标志位
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                reset();
                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * 回复标志位以及状态
     */
    private void reset() {
        // TODO Auto-generated method stub
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        // TODO Auto-generated method stub

        if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    private void changeState(int state) {
        // TODO Auto-generated method stub
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.button_recordnormal);
                    setText(R.string.normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.button_recording);
                    setText(R.string.recording);
                    if (isRecording) {
                        mDialogManager.recording(R.string.shouzhishanghua);
                        // 复写dialog.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.button_recording);
                    setText(R.string.want_to_cancle);
                    // dialog want to cancel
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }

    @Override
    public boolean onPreDraw() {
        // TODO Auto-generated method stub
        return false;
    }

    public void destroyIat() {
        if (mIat != null) {
            mIat.destroy();
        }
    }
}
