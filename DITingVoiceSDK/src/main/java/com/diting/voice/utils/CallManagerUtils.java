package com.diting.voice.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.NotificationCompat;

import com.diting.voice.R;
import com.diting.voice.data.body.VoiceCallInfo;
import com.diting.voice.data.model.CallInfo;
import com.diting.voice.service.UploadIntentService;
import com.diting.voice.voicecall.CallStateListener;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import static com.diting.voice.utils.CallManagerUtils.EndType.CANCEL;
import static com.diting.voice.utils.CallManagerUtils.EndType.NORESPONSE;
import static com.diting.voice.utils.CallManagerUtils.EndType.NORMAL;
import static com.diting.voice.utils.CallManagerUtils.EndType.REJECT;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/19, 16:26.
 * Description: 实时音视频通话管理类.
 */

public class CallManagerUtils {

    private static CallManagerUtils sCallManagerUtils;
    private Context mContext;
    private AudioManager mAudioManager;

    // 蓝牙
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothHeadset mBluetoothHeadset;

    private SoundPool mSoundPool;
    private int mLoadSoundId;
    private int mStreamID;

    private CallType mCallType = CallType.VIDEO;
    private EndType mEndType = NORMAL;
    private CallState mCallState = CallState.DISCONNECTED;
    private String mChatId;

    private boolean isInComingCall = true;
    private boolean isOpenCamera = true;
    private boolean isOpenMic = true;
    private boolean isOpenSpeaker = true;
    private boolean isOpenRecord = false;
    private boolean isLoaded = false;

    private NotificationManager mNotificationManager;
    private int mCallNotificationId = 0526;

    private CallStateListener mCallStateListener;

    private Class<?> mVideoClass;
    private Class<?> mVoiceClass;

    // 计时器
    private Timer timer;

    // 通话时间
    private int callTime = 0;

    private String fromRobotName;
    private String fromCompanyName;
    private String startTime;
    private String toRobotName;
    private String toCompanyName;
    private String fromHeadImgUrl;
    private String toheadImgUrl;

    /**
     * 私有化构造函数
     */
    private CallManagerUtils() {
    }

    public static CallManagerUtils getInstance() {
        if (sCallManagerUtils == null) {
            synchronized (CallManagerUtils.class) {
                if (sCallManagerUtils == null)
                    sCallManagerUtils = new CallManagerUtils();
            }
        }

        return sCallManagerUtils;
    }

    /**
     * 初始化通话管理类
     */
    public void init(Context context) {
        mContext = context;
        // 初始化蓝牙监听
        initBluetoothListener();
        // 初始化音频池
        initSoundPool();
        // 音频管理器
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        /**
         * SDK 3.2.x 版本后通话相关设置，一定要在初始化后，开始音视频功能前设置，否则设置无效
         */
        // 设置通话过程中对方如果离线是否发送离线推送通知，默认 false，这里需要和推送配合使用
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);
        /**
         * 设置是否启用外部输入视频数据，默认 false，如果设置为 true，需要自己调用
         * {@link EMCallManager#inputExternalVideoData(byte[], int, int, int)}输入视频数据
         */
        EMClient.getInstance().callManager().getCallOptions().setEnableExternalVideoData(false);
        // 设置视频旋转角度，启动前和视频通话中均可设置
        //EMClient.getInstance().callManager().getCallOptions().setRotation(90);
        // 设置自动调节分辨率，默认为 true
        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
        /**
         * 设置视频通话最大和最小比特率，可以不用设置，比特率会根据分辨率进行计算，默认最大(800)， 默认最小(80)
         * 这里的带宽是指理想带宽，指单人单线情况下的最低要求
         * >240p: 100k ~ 400kbps
         * >480p: 300k ~ 1Mbps
         * >720p: 900k ~ 2.5Mbps
         * >1080p: 2M  ~ 5Mbps
         */
        EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(800);
        EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(150);
        // 设置视频通话分辨率 默认是(640, 480)
        EMClient.getInstance().callManager().getCallOptions().setVideoResolution(640, 480);
        // 设置通话最大帧率，SDK 最大支持(30)，默认(20)
        EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(30);
        // 设置音视频通话采样率，一般不需要设置，为了减少噪音，可以讲采集了适当调低，这里默认设置32k
        EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(32000);
        // 设置录制视频采用 mov 编码
        EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);
    }

    /**
     * 初始化蓝牙监听
     */
    private void initBluetoothListener() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
                @Override public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    mBluetoothHeadset = (BluetoothHeadset) proxy;
                    LogUtils.d("bluetooth is ");
                }

                @Override public void onServiceDisconnected(int profile) {
                    mBluetoothAdapter = null;
                }
            }, BluetoothProfile.HEADSET);
        }
    }

    /**
     * 初始化 SoundPool.
     */
    private void initSoundPool() {
        if (Utils.hasLollipop()) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(1).build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.MODE_RINGTONE, 0);
        }
    }

    /**
     * 开始呼叫对方
     */
    public void makeCall(String ext,String robot,String company,String headImgUrl) {
//        String[] params = ext.split("#");
//        fromRobotName = params[0];
//        fromCompanyName = params[1];
//        startTime = params[2];
//        fromHeadImgUrl = params[3];
        CallInfo callInfo = new Gson().fromJson(ext,CallInfo.class);
        fromRobotName = callInfo.getRobotName();
        fromCompanyName = callInfo.getCompanyName();
        startTime = callInfo.getTime();
        fromHeadImgUrl = callInfo.getHeadImg();
        toRobotName = robot;
        toCompanyName = company;
        toheadImgUrl = headImgUrl;
        try {
            if (mCallType == CallType.VIDEO) {
                EMClient.getInstance().callManager().makeVideoCall(mChatId, ext);
            } else {
                EMClient.getInstance().callManager().makeVoiceCall(mChatId, ext);
            }
//            setEndType(CANCEL);
        } catch (EMServiceNotReadyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝通话
     */
    public void rejectCall() {
        try {
            // 调用 SDK 的拒绝通话方法
            EMClient.getInstance().callManager().rejectCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
        // 保存一条通话消息
        //saveCallMessage();
        // 通话结束，重置通话状态
        reset();
    }

    /**
     * 结束通话
     */
    public void endCall() {
        try {
            // 调用 SDK 的结束通话方法
            EMClient.getInstance().callManager().endCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
            LogUtils.e("结束通话失败：error " + e.getErrorCode() + " - "  + e.getMessage());
        }
        // 挂断电话调用保存消息方法
        saveCallMessage();
        // 通话结束，重置通话状态
        reset();
    }

    /**
     * 接听通话
     */
    public boolean answerCall() {
        // 接听通话后关闭通知铃音
        stopCallSound();
        // 调用接通通话方法
        try {
            EMClient.getInstance().callManager().answerCall();
            return true;
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打开扬声器
     * 主要是通过扬声器的开关以及设置音频播放模式来实现
     * 1、MODE_NORMAL：是正常模式，一般用于外放音频
     * 2、MODE_IN_CALL：
     * 3、MODE_IN_COMMUNICATION：这个和 CALL 都表示通讯模式，不过 CALL 在华为上不好使，故使用 COMMUNICATION
     * 4、MODE_RINGTONE：铃声模式
     */
    public void openSpeaker() {
        // 检查是否已经开启扬声器
        if (!mAudioManager.isSpeakerphoneOn()) {
            // 打开扬声器
            mAudioManager.setSpeakerphoneOn(true);
        }
        if (mCallState == CallManagerUtils.CallState.ACCEPTED) {
            // 开启了扬声器之后，因为是进行通话，声音的模式也要设置成通讯模式
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            // 在播放通话音效时声音模式需要设置为铃音模式
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
        // setOpenSpeaker(true);

        disconnectBluetoothAudio();
    }

    /**
     * 关闭扬声器，即开启听筒播放模式
     * 更多内容看{@link #openSpeaker()}
     */
    public void closeSpeaker() {
        // 检查是否已经开启扬声器
        if (mAudioManager.isSpeakerphoneOn()) {
            // 关闭扬声器
            mAudioManager.setSpeakerphoneOn(false);
        }
        if (mCallState == CallManagerUtils.CallState.ACCEPTED) {
            // 设置声音模式为通讯模式
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            // 在播放通话音效时声音模式需要设置为铃音模式
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
        // setOpenSpeaker(false);

        connectBluetoothAudio();
    }

    /**
     * 连接蓝牙音频输出设备，通过蓝牙输出声音
     */
    private void connectBluetoothAudio() {
        try {
            if (mBluetoothHeadset != null) {
                mBluetoothHeadset.startVoiceRecognition(mBluetoothHeadset.getConnectedDevices().get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 与蓝牙输出设备断开连接
     */
    private void disconnectBluetoothAudio() {
        try {
            if (mBluetoothHeadset != null) {
                mBluetoothHeadset.stopVoiceRecognition(mBluetoothHeadset.getConnectedDevices().get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载音效资源
     */
    private void loadSound() {
        if (isInComingCall) {
            mLoadSoundId = mSoundPool.load(mContext, R.raw.sound_call_incoming, 1);
        } else {
            mLoadSoundId = mSoundPool.load(mContext, R.raw.sound_calling, 1);
        }
    }

    /**
     * 尝试播放呼叫通话提示音
     */
    public void attemptPlayCallSound() {
        // 检查音频资源是否已经加载完毕
        if (isLoaded) {
            playCallSound();
        } else {
            // 播放之前先去加载音效
            loadSound();
            // 设置资源加载监听，也因为加载资源在单独的进程，需要时间，所以等监听到加载完成才能播放
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    LogUtils.d("SoundPool load complete! loadId: " + mLoadSoundId);
                    isLoaded = true;
                    // 首次监听到加载完毕，开始播放音频
                    playCallSound();
                }
            });
        }
    }

    /**
     * 播放音频
     */
    private void playCallSound() {
        // 打开扬声器
        openSpeaker();
        // 设置音频管理器音频模式为铃音模式
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        // 播放提示音，返回一个播放的音频id，等下停止播放需要用到
        if (mSoundPool != null) {
            mStreamID = mSoundPool.play(mLoadSoundId, // 播放资源id；就是加载到SoundPool里的音频资源顺序
                    0.5f,   // 左声道音量
                    0.5f,   // 右声道音量
                    1,      // 优先级，数值越高，优先级越大
                    -1,     // 是否循环；0 不循环，-1 循环，N 表示循环次数
                    1);     // 播放速率；从0.5-2，一般设置为1，表示正常播放
        }
    }

    /**
     * 关闭音效的播放，并释放资源
     */
    public void stopCallSound() {
        if (mSoundPool != null) {
            // 停止播放音效
            mSoundPool.stop(mStreamID);
            // 卸载音效
            // mSoundPool.unload(mLoadSoundId);
            // 释放资源
            // mSoundPool.release();
        }
    }


    /**
     * 设置通话图像回调处理器
     */
    public void setCallCameraDataProcessor() {
        // 初始化视频数据处理器
        CameraDataProcessor cameraDataProcessor = new CameraDataProcessor();
        // 设置视频通话数据处理类
        EMClient.getInstance().callManager().setCameraDataProcessor(cameraDataProcessor);
    }


    /**
     * ----------------------------- Call state -----------------------------
     * 注册通话状态监听，监听音视频通话状态
     * 状态监听详细实现在 {@link CallStateListener} 类中
     */
    public void registerCallStateListener() {
        EMClient.getInstance().callManager().addCallStateChangeListener(mCallStateListener);
    }

    /**
     * 删除通话状态监听
     */
    private void unregisterCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(mCallStateListener);
        mCallStateListener = null;
    }

    /**
     * 添加通话悬浮窗并发送通知栏提醒
     */
    public void addFloatWindow() {
        // 发送通知栏提醒
        addCallNotification();
        // 开启悬浮窗
        // FloatWindow.getInstance(mContext).addFloatWindow();
    }

    /**
     * 移除通话悬浮窗和通知栏提醒
     */
    public void removeFloatWindow() {
        // 取消通知栏提醒
        cancelCallNotification();
        // 关闭悬浮窗
        // FloatWindow.getInstance(mContext).removeFloatWindow();
    }

//    /**
//     * 通话结束，保存一条记录通话的消息
//     */
//    public void saveCallMessage() {
//        EMMessage message = null;
//        EMTextMessageBody body = null;
//        String content = null;
//        if (isInComingCall) {
//            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            message.setFrom(mChatId);
//        } else {
//            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//            message.setTo(mChatId);
//        }
//        switch (mEndType) {
//            case NORMAL: // 正常结束通话
//                content = String.valueOf(getCallTime());
//                break;
//            case CANCEL: // 取消
//                content = mContext.getString(R.string.call_cancel);
//                break;
//            case CANCELLED: // 被取消
//                content = mContext.getString(R.string.call_cancel_is_incoming);
//                break;
//            case BUSY: // 对方忙碌
//                content = mContext.getString(R.string.call_busy);
//                break;
//            case OFFLINE: // 对方不在线
//                content = mContext.getString(R.string.call_offline);
//                break;
//            case REJECT: // 拒绝的
//                content = mContext.getString(R.string.call_reject_is_incoming);
//                break;
//            case REJECTED: // 被拒绝的
//                content = mContext.getString(R.string.call_reject);
//                break;
//            case NORESPONSE: // 未响应
//                content = mContext.getString(R.string.call_no_response);
//                break;
//            case TRANSPORT: // 建立连接失败
//                content = mContext.getString(R.string.call_connection_fail);
//                break;
//            case DIFFERENT: // 通讯协议不同
//                content = mContext.getString(R.string.call_offline);
//                break;
//            default:
//                // 默认取消
//                content = mContext.getString(R.string.call_cancel);
//                break;
//        }
//        body = new EMTextMessageBody(content);
//        message.addBody(body);
//        message.setStatus(EMMessage.Status.SUCCESS);
//        if (mCallType == CallType.VIDEO) {
//            message.setAttribute("attr_call_video", true);
//        } else {
//            message.setAttribute("attr_call_voice", true);
//        }
//        message.setUnread(false);
//        // 调用sdk的保存消息方法
//        EMClient.getInstance().chatManager().saveMessage(message);
//    }


    /**
     * 通话结束，保存一条记录通话的消息
     */
    public void saveCallMessage() {
        if (!isInComingCall) {
            VoiceCallInfo voiceCallInfo = new VoiceCallInfo();
            voiceCallInfo.setStartTime(startTime);
            voiceCallInfo.setFromRobotName(fromRobotName);
            voiceCallInfo.setFromCompanyName(fromCompanyName);
            voiceCallInfo.setToRobotName(toRobotName);
            voiceCallInfo.setToCompanyName(toCompanyName);
            voiceCallInfo.setCallTime(callTime);
            voiceCallInfo.setFromUserName(EMClient.getInstance().getCurrentUser());
            voiceCallInfo.setToUserName(mChatId);
            voiceCallInfo.setFromHeadImgUrl(fromHeadImgUrl);
            voiceCallInfo.setToheadImgUrl(toheadImgUrl);
            if (mCallType == CallType.VIDEO) {
                voiceCallInfo.setCallType(VoiceCallInfo.CallType.VIDEO);
            } else {
                voiceCallInfo.setCallType(VoiceCallInfo.CallType.VOICE);
            }
            switch (mEndType) {
                case NORMAL: // 正常结束通话
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORMAL);
                    break;
                case CANCEL: // 取消
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORMAL);
                    break;
                case CANCELLED: // 被取消
                    if(callTime > 0) {
                        voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORMAL);
                    }else {
                        voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    }
                    break;
                case BUSY: // 对方忙碌
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    break;
                case OFFLINE: // 对方不在线
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORESPONSE);
                    break;
                case REJECT: // 拒绝的
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    break;
                case REJECTED: // 被拒绝的
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    break;
                case NORESPONSE: // 未响应
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORESPONSE);
                    break;
                case TRANSPORT: // 建立连接失败
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    break;
                case DIFFERENT: // 通讯协议不同
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.REJECT);
                    break;
                default:
                    // 默认取消
                    voiceCallInfo.setEndType(VoiceCallInfo.EndType.NORMAL);
                    break;
            }
            //上传记录到服务器
            UploadIntentService.startActionCallInfo(mContext,voiceCallInfo);
//            Intent intent = new Intent(mContext, UploadIntentService.class);
//            intent.putExtra("voiceCallInfo",voiceCallInfo);
//            mContext.startService(intent);
        }
    }


    /**
     * 发送通知栏提醒，告知用户通话继续进行中
     */
    private void addCallNotification() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

        builder.setContentText("通话进行中，点击恢复");

        builder.setContentTitle(mContext.getString(R.string.app_name));
        Intent intent = new Intent();
        if (mCallType == CallType.VIDEO) {
            intent.setClass(mContext, getVideoClass());
        } else {
            intent.setClass(mContext, getVoiceClass());
        }
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pIntent);
        builder.setOngoing(true);

        builder.setWhen(System.currentTimeMillis());

        mNotificationManager.notify(mCallNotificationId, builder.build());
    }

    /**
     * 取消通话状态通知栏提醒
     */
    public void cancelCallNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(mCallNotificationId);
        }
    }

    /**
     * 释放资源
     */
    public void reset() {
        isOpenCamera = true;
        isOpenMic = true;
        isOpenSpeaker = true;
        isOpenRecord = false;
        // 停止计时
         stopCallTime();
        // 取消注册通话状态的监听
        unregisterCallStateListener();
        // 设置通话状态为已断开
        setCallState(CallState.DISCONNECTED);
        mEndType = NORMAL;
        // 释放音频资源
        if (mSoundPool != null) {
            // 停止播放音效
            mSoundPool.stop(mStreamID);
        }
        // 重置音频管理器
        if (mAudioManager != null) {
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
    }

    public CallType getCallType() {
        return mCallType;
    }

    public void setCallType(CallType callType) {
        mCallType = callType;
    }

    public int getCallTime() {
        return callTime;
    }

    public EndType getEndType() {
        return mEndType;
    }

    public void setEndType(EndType endType) {
        mEndType = endType;
    }

    public String getChatId() {
        return mChatId;
    }

    public void setChatId(String chatId) {
        mChatId = chatId;
    }

    public boolean isInComingCall() {
        return isInComingCall;
    }

    public void setInComingCall(boolean inComingCall) {
        isInComingCall = inComingCall;
    }

    public CallState getCallState() {
        return mCallState;
    }

    public boolean isOpenCamera() {
        return isOpenCamera;
    }

    public void setOpenCamera(boolean openCamera) {
        isOpenCamera = openCamera;
    }

    public boolean isOpenMic() {
        return isOpenMic;
    }

    public void setOpenMic(boolean openMic) {
        isOpenMic = openMic;
    }

    public boolean isOpenSpeaker() {
        return isOpenSpeaker;
    }

    public void setOpenSpeaker(boolean openSpeaker) {
        isOpenSpeaker = openSpeaker;
    }

    public boolean isOpenRecord() {
        return isOpenRecord;
    }

    public void setOpenRecord(boolean openRecord) {
        isOpenRecord = openRecord;
    }

    public void setCallState(CallState callState) {
        mCallState = callState;
    }

    public void setVideoClass(Class<?> cls) {
        mVideoClass = cls;
    }

    public Class<?> getVideoClass() {
        return mVideoClass;
    }

    public void setVoiceClass(Class<?> cls) {
        mVoiceClass = cls;
    }

    public Class<?> getVoiceClass() {
        return mVoiceClass;
    }

    public void setCallStateListener(CallStateListener callStateListener) {
        mCallStateListener = callStateListener;
    }

    /**
     * 通话类型
     */
    public enum CallType {
        VIDEO,  // 视频通话
        VOICE   // 音频通话
    }

    /**
     * 通话状态枚举值
     */
    public enum CallState {
        CONNECTING,     // 连接中
        CONNECTED,      // 连接成功，等待接受
        ACCEPTED,       // 通话中
        DISCONNECTED    // 通话中断

    }

    /**
     * 通话结束状态类型
     */
    public enum EndType {
        NORMAL,     // 正常结束通话
        CANCEL,     // 取消
        CANCELLED,  // 被取消
        BUSY,       // 对方忙碌
        OFFLINE,    // 对方不在线
        REJECT,     // 拒绝的
        REJECTED,   // 被拒绝的
        NORESPONSE, // 未响应
        TRANSPORT,  // 建立连接失败
        DIFFERENT   // 通讯协议不同
    }

    /**
     * 开始通话计时，这里在全局管理器中开启一个定时器进行计时，可以做到最小化，以及后台时进行计时
     */
    public void startCallTime() {
        final CallEvent event = new CallEvent();
        EventBus.getDefault().post(event);
        event.setTime(true);
        if (timer == null) {
            timer = new Timer();
        }
        timer.purge();
        TimerTask task = new TimerTask() {
            @Override public void run() {
                callTime++;
                EventBus.getDefault().post(event);
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    /**
     * 停止计时
     */
    public void stopCallTime() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
        callTime = 0;
    }
}
