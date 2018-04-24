package com.diting.pingxingren.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.EditVoiceInputTextActivity;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.TextLengthUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
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

import java.lang.ref.WeakReference;

import static com.iflytek.cloud.ErrorCode.ERROR_AUDIO_RECORD;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 21, 15:28.
 * Description: .
 */

public class VoiceInputDialog extends DefaultDialog implements View.OnClickListener {

    private TextView mInputText;
    private TextView mInputTip;
    private TextView mCancelText;
    private TextView mConfirmText;
    private ImageView mCancelImage;
    private ImageView mInputImage;
    private ImageView mInputImageBack;
    private InputResultCallBack mResultCallBack;
    private SpeechRecognizer mIat;
    private StringBuffer mResultBuffer;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private int mCurrentState = STATE_NORMAL;
    private boolean isRecording = false;
    private String mCurrentText;
    private VoiceInputDialogHandler mHandler;
    private float mTime = 0;
    private boolean mReady;

    public VoiceInputDialog(Activity activity) {
        super(activity);
        mHandler = new VoiceInputDialogHandler(this);
        mDialog = DialogUtil.createDialog(activity, R.layout.layout_voice_input, R.style.CustomDialog);
        mInputText = mDialog.findViewById(R.id.tvInputText);
        mInputTip = mDialog.findViewById(R.id.tvInputTip);
        mCancelText = mDialog.findViewById(R.id.tvCancel);
        mConfirmText = mDialog.findViewById(R.id.tvConfirm);
        mCancelImage = mDialog.findViewById(R.id.ivCancel);
        mInputImage = mDialog.findViewById(R.id.ivVoice);
        mInputImageBack = mDialog.findViewById(R.id.ivVoiceBack);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            layoutParams.width = ScreenUtil.getScreenWidth(activity);
            layoutParams.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.5);
            dialogWindow.setAttributes(layoutParams);
        }

        initListener();
    }

    private void initListener() {
        mInputText.setFilters(new InputFilter[]{new TextLengthUtil(mActivity, 241, "输入答案的问题内容不能超过240个字")});
        InitListener initCallBack = new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS)
                    ToastUtils.showShortToastSafe("讯飞语音引擎初始化失败!");
            }
        };
        mIat = SpeechRecognizer.createRecognizer(mActivity, initCallBack);

        mInputImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCurrentText = mInputText.getText().toString();
                mTime = 0;
                mReady = true;
                setParams();
                mIat.startListening(mRecognizerListener);
                return false;
            }
        });

        mInputImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        changeState(STATE_RECORDING);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mReady) {
                            reset();
                            return false;
                        }

                        if (!isRecording || mTime < 0.6f) {
                            ToastUtils.showShortToastSafe("录制时间太短.");
                        } else if (mCurrentState == STATE_RECORDING) {//正常录制结束
                            mIat.stopListening();
                        }
                        reset();
                        break;
                }
                return false;
            }
        });

        mCancelImage.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
        mConfirmText.setOnClickListener(this);
        mInputText.setOnClickListener(this);
    }

    public void setResultCallBack(InputResultCallBack resultCallBack) {
        mResultCallBack = resultCallBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCancel:
            case R.id.tvCancel:
                dismissDialog();
                break;
            case R.id.tvConfirm:
                mResultCallBack.onResult(mInputText.getText().toString());
                resetView();
                break;
            case R.id.tvInputText:
                mActivity.startActivityForResult(EditVoiceInputTextActivity.getCallingIntent(
                        mActivity, mInputText.getText().toString()), 6);
                break;
        }
    }

    public String getmRecognizerPath() {
        return mRecognizerPath;
    }

    private String mRecognizerPath;
    private void setParams() {
        mRecognizerPath = Environment.getExternalStorageDirectory() + "/diting/" + TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM2) + ".wav";
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, mRecognizerPath);
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            Message message = new Message();
            message.what = MSG_VOICE_CHANGE;
            message.obj = i % 3;
            mHandler.sendMessage(message);
        }

        @Override
        public void onBeginOfSpeech() {
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
                displayText = mCurrentText + mResultBuffer.toString();
                mInputText.setText(displayText);

                if (b) {
                    displayText = mCurrentText + mResultBuffer.toString();
                    Message message = new Message();
                    message.what = MSG_RECORDING_FINISH;
                    message.obj = displayText;
                    mHandler.sendMessage(message);
                    mResultBuffer = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if (speechError.getErrorCode() == ERROR_AUDIO_RECORD) {
                ToastUtils.showShortToastSafe("录音权限被屏蔽或者录音设备损坏！\n请在设置中检查是否开启权限！");
            } else {
                ToastUtils.showShortToastSafe("您并没有说话");
                mConfirmText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    private void updateVoiceLevel(int level) {
        if (mInputImageBack.getVisibility() == View.GONE)
            mInputImageBack.setVisibility(View.VISIBLE);
        int resId;
        if (level >= 1 && level < 2) {
            resId = mActivity.getResources().getIdentifier("ic_voice_recording1",
                    "drawable", mActivity.getPackageName());
            mInputImageBack.setMaxHeight(55);
            mInputImageBack.setMaxWidth(55);
        } else if (level >= 2 && level < 3) {
            resId = mActivity.getResources().getIdentifier("ic_voice_recording2",
                    "drawable", mActivity.getPackageName());
            mInputImageBack.setMaxHeight(65);
            mInputImageBack.setMaxWidth(65);
        } else {
            resId = mActivity.getResources().getIdentifier("ic_voice_recording3",
                    "drawable", mActivity.getPackageName());
            mInputImageBack.setMaxHeight(75);
            mInputImageBack.setMaxWidth(75);
        }
        if (Utils.hasJellyBean())
            mInputImageBack.setImageResource(resId);
//            mInputImageBack.setBackground(ContextCompat.getDrawable(mActivity, resId));
    }

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

    public interface InputResultCallBack {
        void onResult(String result);
    }

    //改变状态
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    mInputTip.setVisibility(View.VISIBLE);
                    mCancelImage.setVisibility(View.VISIBLE);
                    mInputImageBack.setVisibility(View.GONE);
                    mCancelText.setVisibility(View.GONE);
                    mConfirmText.setVisibility(View.GONE);
                    break;
                case STATE_RECORDING:
                    mInputTip.setVisibility(View.GONE);
                    mCancelImage.setVisibility(View.GONE);
                    mCancelText.setVisibility(View.GONE);
                    mConfirmText.setVisibility(View.GONE);
                    break;
            }
        }
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

    public void resetView() {
        mInputText.setText("");
        mInputTip.setVisibility(View.VISIBLE);
        mCancelImage.setVisibility(View.VISIBLE);
        mInputImageBack.setVisibility(View.GONE);
        mCancelText.setVisibility(View.GONE);
        mConfirmText.setVisibility(View.GONE);
        dismissDialog();
    }

    private Runnable mGetTimeRunnable = new Runnable() {
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

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_RECORDING = 0X112;
    private static final int MSG_RECORDING_FINISH = 0X113;

    private static class VoiceInputDialogHandler extends Handler {
        private WeakReference<VoiceInputDialog> mActivityWeakReference;

        public VoiceInputDialogHandler(VoiceInputDialog activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VoiceInputDialog activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_AUDIO_PREPARED:
                        activity.isRecording = true;
                        new Thread(activity.mGetTimeRunnable).start();
                        break;
                    case MSG_VOICE_CHANGE:
                        activity.updateVoiceLevel((Integer) msg.obj);
                        break;
                    case MSG_RECORDING_FINISH:
                        String result = (String) msg.obj;
                        if (Utils.isNotEmpty(result)) {
                            activity.mInputText.setText(result);
                            activity.mCancelText.setVisibility(View.VISIBLE);
                            activity.mConfirmText.setVisibility(View.VISIBLE);
                            activity.mInputTip.setVisibility(View.GONE);
                            activity.mInputImageBack.setVisibility(View.GONE);
                            activity.mCancelImage.setVisibility(View.GONE);
                        } else {
                            activity.mCancelImage.setVisibility(View.VISIBLE);
                            activity.mInputTip.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        }
    }
}
