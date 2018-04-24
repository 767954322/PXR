package com.diting.pingxingren.app;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.diting.pingxingren.activity.VideoCallActivity;
import com.diting.pingxingren.activity.VoiceCallActivity;
import com.diting.pingxingren.db.DitingDB;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.push.CustomNotificationHandler;
import com.diting.pingxingren.util.CrashHandler;
import com.diting.pingxingren.util.LogUtils;
import com.diting.voice.DITingVoice;
import com.diting.voice.voicecall.CallReceiver;
import com.hyphenate.chat.EMClient;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends MultiDexApplication {

    private static MyApplication sApplication;
    public static RequestQueue requestQueue;
    public static SharedPreferences userPreferences;
    public static DitingDB ditingDB;
    public static int unreadLetterNum = 0;
    public static int unreadMessageNum = 0;
    public static   int shareTimes =0 ;//用户分享过几次
    public static  int sharerStatusNum  =0 ;//用户只能分享几次
    private CallReceiver mCallReceiver;
    public static String toRobotName = null;
    public static String toCompanyName = null;
    public static String companyName = null;
    //    public static String headPortrait = "";
    public static String toHeadImgUrl = "";
//    public static List<CommonLanguageModel> sCommonLanguages = new ArrayList<>();
    public static List<CommonLanguageListModel> sCommonLanguages = new ArrayList<>();
    private List<Activity> activityList = new LinkedList<Activity>();

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == sApplication) {
            synchronized (MyApplication.class) {
                sApplication = new MyApplication();
            }
        }
        return sApplication;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 容器中清除Activity
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public void finishSingleActivityByClass(Class cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }
        finishSingleActivity(tempActivity);
    }

    public void removeOtherActivity(Class cls) {
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            if (activity.getClass().equals(cls)) {

            } else {
                activity.finish();
                activityList.remove(i);
            }
        }
    }


    public void removeAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.removeAll(activityList);
    }

    public void settingLogout() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        // 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
        requestQueue = Volley.newRequestQueue(this);
        userPreferences = getSharedPreferences("user", MODE_PRIVATE);
        // ditingDB = DitingDB.getInstance(this);

        //百度地图初始化
        SDKInitializer.initialize(this);

        //环信初始化
        DITingVoice.getInstance().initVoiceCall(MyApplication.this, VoiceCallActivity.class, VideoCallActivity.class);

        // 设置通话广播监听器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (mCallReceiver == null) {
            mCallReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        getApplicationContext().registerReceiver(mCallReceiver, callFilter);


        //科大讯飞语音初始化
        StringBuffer param = new StringBuffer();
        param.append("appid=" + "57e87237");
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MyApplication.this, param.toString());
        ZXingLibrary.initDisplayOpinion(this);
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        //友盟统计
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(false);
        //友盟分享
        UMShareAPI.get(this);

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        // 测试
        UMConfigure.init(this, "5a167a9cb27b0a2d5200005d", "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "855a4ce9ace8098e7de0397a044cb27b");
        // 正式
//      UMConfigure.init(this, "5a27aadef43e48685500012d", "Umeng",
//                UMConfigure.DEVICE_TYPE_PHONE, "afbdae1db6430ea1171cc31f4516115f");
        // 推送
        initPush();
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {

        //微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//        PlatformConfig.setWeixin("wxea17da39e0324580", "194481bd687c17262f94e6499ac53511");
        PlatformConfig.setWeixin("wxc6fbeded8b899a3b", "091b357ab5db270805c71464ae552994");
        //新浪微博
        PlatformConfig.setSinaWeibo("2112440201", "1b582c564589bc98f68930405bce11b0","https://api.weibo.com/oauth2/default.html");
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
//        /*最新的版本需要加上这个回调地址，可以在微博开放平台申请的应用获取，必须要有*/
//        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        //QQ
        PlatformConfig.setQQZone("101452215", "804db90c5859a9619c7f3a3a35bc0479");

    }

    private Handler mHandler;

    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    private void initPush() {
        mHandler = new Handler(getMainLooper());
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        mPushAgent.setMessageHandler(mUmengMessageHandler);
        CustomNotificationHandler customNotificationHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(customNotificationHandler);

        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtils.e(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e(s + ", " + s1);
            }
        });
    }

    private UmengMessageHandler mUmengMessageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(Context context, final UMessage uMessage) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // 对自定义消息的处理方式，点击或者忽略
                }
            });
        }

        @Override
        public Notification getNotification(Context context, UMessage uMessage) {
            switch (uMessage.builder_id) {
                case 1:
                    Notification.Builder builder = new Notification.Builder(context);
                    /*RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                    builder.setContent(myNotificationView)
                            .setSmallIcon(getSmallIconId(context, msg))
                            .setTicker(msg.ticker)
                            .setAutoCancel(true);*/

                    return builder.getNotification();
                default:
                    return super.getNotification(context, uMessage);
            }
        }
    };
}