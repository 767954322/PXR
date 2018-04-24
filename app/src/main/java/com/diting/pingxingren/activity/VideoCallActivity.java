package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseCallActivity;
import com.diting.pingxingren.databinding.ActivityCallVideoBinding;
import com.diting.voice.data.model.CallInfo;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.util.PermissionUtils;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.Utils;
import com.diting.voice.utils.CallEvent;
import com.diting.voice.utils.CallManagerUtils;
import com.diting.voice.utils.Constants;
import com.diting.voice.voicecall.CallResultListener;
import com.diting.voice.voicecall.CallStateListener;
import com.google.gson.Gson;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.superrtc.sdk.VideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_CALL_PHONE;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.diting.voice.utils.Utils.second2time;

/**
 * Creator: Gu FanFan.
 * Date: 2017/9/4, 16:59.
 * Description: 视频通话.
 */

public class VideoCallActivity extends BaseCallActivity implements ClickListener {

    public static Intent getCallingIntent(Context context, String callExt) {
        Intent intent = new Intent();
        intent.setClass(context, VideoCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(callExt)) {
            intent.putExtra("callExt", callExt);
        }
        return intent;
    }

    // 视频通话帮助类
    private EMCallManager.EMVideoCallHelper mVideoCallHelper;
    // SurfaceView 控件状态，-1 表示通话未接通，0 表示本小远大，1 表示远小本大
    private int mSurfaceState = -1;
    private EMCallSurfaceView mLocalSurface = null;
    private EMCallSurfaceView mOppositeSurface = null;
    private RelativeLayout.LayoutParams mLocalParams = null;
    private RelativeLayout.LayoutParams mOppositeParams = null;
    private ActivityCallVideoBinding mBinding;
    private VideoCallActivityHandler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mHandler = new VideoCallActivityHandler(VideoCallActivity.this);
        mBinding = DataBindingUtil.setContentView(VideoCallActivity.this, R.layout.activity_call_video);
        mBinding.setClickListener(this);

        CallManagerUtils.getInstance().setCallStateListener(mCallStateListener);
        if (CallManagerUtils.getInstance().getCallState() == CallManagerUtils.CallState.DISCONNECTED) {
            // 收到呼叫或者呼叫对方时初始化通话状态监听
            CallManagerUtils.getInstance().setCallState(CallManagerUtils.CallState.CONNECTING);
            CallManagerUtils.getInstance().registerCallStateListener();
            CallManagerUtils.getInstance().attemptPlayCallSound();

            // 如果不是对方打来的，就主动呼叫
            if (!CallManagerUtils.getInstance().isInComingCall()) {
                mCallStateListener.startCountDownTimer();
                CallManagerUtils.getInstance().makeCall(getIntent().getStringExtra("callExt"), MyApplication.toRobotName,MyApplication.toCompanyName,MyApplication.toHeadImgUrl);
                mBinding.robotName.setText(MyApplication.toRobotName);
                Glide.with(this).load(MyApplication.toHeadImgUrl).into(mBinding.userHead);
            } else {
                CallInfo callInfo = new Gson().fromJson(getIntent().getStringExtra("callExt"),CallInfo.class);
                //String[] exts = ((MyApplication)getApplication()).getmCallReceiver().getCallExt().split("#");
                mBinding.robotName.setText(callInfo.getRobotName());
                if(!TextUtils.isEmpty(callInfo.getHeadImg())) {
                    Glide.with(this).load(callInfo.getHeadImg()).into(mBinding.userHead);
                }
            }
        }

        initVideoView();

        // 初始化视频通话帮助类
        mVideoCallHelper = EMClient.getInstance().callManager().getVideoCallHelper();
        // 初始化显示通话画面
        requestCameraPermission();
        //initCallSurface();
        // 判断当前通话时刚开始，还是从后台恢复已经存在的通话
        if (CallManagerUtils.getInstance().getCallState() == CallManagerUtils.CallState.ACCEPTED) {
            mBinding.afterAnswering.setVisibility(View.VISIBLE);
            mBinding.beforeAnswering.setVisibility(View.VISIBLE);
            mBinding.callState.setText(R.string.call_accepted);
            // refreshCallTime();
            // 通话已接通，修改画面显示
            onCallSurface();
        }

        try {
            // 设置默认摄像头为前置
            EMClient.getInstance().callManager().setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        CallManagerUtils.getInstance().setCallCameraDataProcessor();
    }

    private void initVideoView() {
        if (CallManagerUtils.getInstance().isInComingCall()) {
            mBinding.callState.setText(R.string.call_connected_is_incoming);
            mBinding.afterAnswering.setVisibility(View.GONE);
            mBinding.beforeAnswering.setVisibility(View.VISIBLE);
        } else {
            mBinding.callState.setText(R.string.call_connected);
            mBinding.afterAnswering.setVisibility(View.GONE);
            mBinding.beforeAnswering.setVisibility(View.VISIBLE);
            mBinding.answerImage.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化通话界面控件
     */
    private void initCallSurface() {
        // 初始化显示远端画面控件
        mOppositeSurface = new EMCallSurfaceView(mContext);
        mOppositeParams = new RelativeLayout.LayoutParams(0, 0);
        mOppositeParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        mOppositeParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        mOppositeSurface.setLayoutParams(mOppositeParams);
        mBinding.layoutSurface.addView(mOppositeSurface);

        // 初始化显示本地画面控件
        mLocalSurface = new EMCallSurfaceView(mContext);
        mLocalParams = new RelativeLayout.LayoutParams(0, 0);
        mLocalParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        mLocalParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        mLocalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mLocalSurface.setLayoutParams(mLocalParams);
        mBinding.layoutSurface.addView(mLocalSurface);

        mLocalSurface.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onControlLayout();
            }
        });

        mLocalSurface.setZOrderOnTop(false);
        mLocalSurface.setZOrderMediaOverlay(true);

        // 设置本地和远端画面的显示方式，是填充，还是居中
        mLocalSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        mOppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        // 设置通话画面显示控件
        EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
    }

    /**
     * 控制界面的显示与隐藏
     */
    private void onControlLayout() {
    }

    /**
     * 接通通话，这个时候要做的只是改变本地画面 view 大小，不需要做其他操作
     */
    private void onCallSurface() {
        mBinding.beforeAnswering.setVisibility(View.GONE);
        mBinding.afterAnswering.setVisibility(View.VISIBLE);

        // 更新通话界面控件状态
        mSurfaceState = 0;

        int width = ScreenUtil.dip2px(this,96);
        int height = ScreenUtil.dip2px(this,128);
        int rightMargin = ScreenUtil.dip2px(this,16);
        int topMargin = ScreenUtil.dip2px(this,96);

        mLocalParams = new RelativeLayout.LayoutParams(width, height);
        mLocalParams.width = width;
        mLocalParams.height = height;
        mLocalParams.rightMargin = rightMargin;
        mLocalParams.topMargin = topMargin;
        mLocalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mLocalSurface.setLayoutParams(mLocalParams);

        mLocalSurface.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                changeCallSurface();
            }
        });

        mOppositeSurface.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onControlLayout();
            }
        });
    }

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallSurface() {
        if (mSurfaceState == 0) {
            mSurfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(mOppositeSurface, mLocalSurface);
        } else {
            mSurfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(mLocalSurface, mOppositeSurface);
        }
    }

    /**
     * 切换摄像头
     */
    private void changeCamera() {
        // 根据切换摄像头开关是否被激活确定当前是前置还是后置摄像头
        try {
            if (EMClient.getInstance().callManager().getCameraFacing() == 1) {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(0);
            } else {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(1);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    public void callAccepted() {
        mBinding.callState.setText(R.string.call_accepted);
        onCallSurface();
    }

    private CallStateListener mCallStateListener = new CallStateListener(new CallResultListener() {
        @Override
        public void onCallState(String stateString) {
            Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_UPDATE_STATE;
            message.obj = stateString;
            mHandler.sendMessage(message);
        }

        @Override
        public void onCallError(String errorMsg) {
            Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_ERROR;
            message.obj = errorMsg;
            mHandler.sendMessage(message);
        }


        @Override
        public void onCallPhone() {
            /*Message message = new Message();
            message.what = Constants.HANDLER_VOICE_CALL_ERROR;
            message.obj = "正在为您拨打对方电话, 请稍后...";
            mHandler.sendMessage(message);*/
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_END);
        }
    });

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.rejectImage :
                mCallStateListener.cancelCountDownTimer();
                rejectCall();
                break;
            case R.id.answerImage :
                answerCall();
                break;
            case R.id.endVideoCall :
                mCallStateListener.cancelCountDownTimer();
                endCall();
                break;
            case R.id.switchCamera :
                changeCamera();
                break;
        }
    }

    @Override
    public void onClick(Object o) {
    }

    private static class VideoCallActivityHandler extends Handler {
        private WeakReference<VideoCallActivity> mActivityWeakReference;

        public VideoCallActivityHandler(VideoCallActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoCallActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constants.HANDLER_VOICE_CALL_UPDATE_STATE:
                        String state = (String) msg.obj;
                        activity.setCallStateText(state);
                        break;
                    case Constants.HANDLER_VOICE_CALL_ERROR:
                        activity.finish();
                        break;
                    case Constants.HANDLER_VOICE_CALL_PHONE:
                        if(!CallManagerUtils.getInstance().isInComingCall()) {
                            CallManagerUtils.getInstance().setEndType(CallManagerUtils.EndType.NORESPONSE);
                            activity.mCallStateListener.cancelCountDownTimer();
                            activity.endCall();
                            PermissionUtils.requestCallPermission(activity, CallManagerUtils.getInstance().getChatId());
                        }
                        break;
                }
            }
        }
    }

    private void setCallStateText(String stateText) {
        mBinding.callState.setText(stateText);
        if (stateText.equals("电话已接通")) {
            callAccepted();
        } else if (stateText.equals(getString(R.string.call_busy))
                || stateText.equals(getString(R.string.call_cancel))
                || stateText.equals(getString(R.string.call_cancel_is_incoming))
                || stateText.equals(getString(R.string.call_disconnected))
                || stateText.equals(getString(R.string.call_reject_is_incoming))
                ||stateText.equals(getString(R.string.call_reject))) {
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_ERROR);
        } else if (stateText.equals("正在为您拨打对方电话, 请稍后...")) {
            mHandler.sendEmptyMessage(Constants.HANDLER_VOICE_CALL_PHONE);
        }
    }

    private void requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                Utils.showMissingPermissionDialog(this,"相机");
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }else {
            requestVideoPermission();
        }
    }

    private void requestVideoPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
                Utils.showMissingPermissionDialog(this,"录音");
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
            }
        }else {
           initCallSurface();
        }
    }

    /**
     * 刷新通话时间显示
     */
    private void refreshCallTime() {
        int t = CallManagerUtils.getInstance().getCallTime();
        mBinding.callState.setText(second2time(t));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(CallEvent event) {
        if (event.isTime()) {
            // 不论什么情况都检查下当前时间
            refreshCallTime();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                PermissionUtils.callPhone(VideoCallActivity.this,CallManagerUtils.getInstance().getChatId());
            } else
            {
                // Permission Denied
                //showShortToast("请到设置中开启拨号权限");
                Utils.showMissingPermissionDialog(VideoCallActivity.this,"拨号");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_AUDIO_RECORD)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initCallSurface();

            }else {
                // Permission Denied
                //showShortToast("请到设置中开启录音权限");
                Utils.showMissingPermissionDialog(VideoCallActivity.this,"录音");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                    requestVideoPermission();
            }else {
                // Permission Denied
                //showShortToast("请到设置中开启相机权限");
                Utils.showMissingPermissionDialog(VideoCallActivity.this,"相机");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
