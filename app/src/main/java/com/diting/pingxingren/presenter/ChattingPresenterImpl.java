package com.diting.pingxingren.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.adapter.ChattingAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.db.DTingDBUtil;
import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.model.ChatModel;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.model.PersonalMailItemModel;
import com.diting.pingxingren.model.PersonalMailModel;
import com.diting.pingxingren.model.PublicChatModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChatObserver;
import com.diting.pingxingren.net.observers.PersonalMessageObserver;
import com.diting.pingxingren.net.observers.PublicChatObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.map.CoordinateConversion;
import com.diting.pingxingren.util.map.Point;
import com.diting.pingxingren.view.ChattingView;
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
import java.util.List;

import static com.iflytek.cloud.ErrorCode.ERROR_AUDIO_RECORD;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 09:48.
 * Description: .
 */

public class ChattingPresenterImpl implements ChattingPresenter {

    private LocationClient mLocationClient;
    private DTingDBUtil mDTingDBUtil;
    private ChattingView mChattingView;
    private ChatMessageItem mMsg;
    private String mGoogleLatitude;
    private String mGoogleLongitude;
    private String mUserName;
    private String mRobotName;
    private String mUuid;
    private String mHeadImage;
    private boolean isNews = false;
    private boolean mIsOtherChat = false;
    private boolean mIsVoice = false;
    private Activity mActivity;
    private ChattingAdapter mAdapter;

    private String mRecognizerPath;
    private SpeechRecognizer mIat;
    private StringBuffer mResultBuffer;
    private long mStartRecognitionTime;
    private String mAnswer1;
    private String mAnswer2;
    private String mAnswer3;
    private String mUniqueId;
    private static final String TYPE_TEXT = "TYPE_TEXT";
    private static final String TYPE_AUDIO = "TYPE_AUDIO";
    private static final String TYPE_FILE = "TYPE_FILE";
    private static final String TYPE_PICTURE = "TYPE_PICTURE";
    private static final String SPACE = ";";

    public void setmUniqueId(String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }

    public void setmAnswer1(String mAnswer1) {
        this.mAnswer1 = mAnswer1;
    }

    public void setmAnswer2(String mAnswer2) {
        this.mAnswer2 = mAnswer2;
    }

    public void setmAnswer3(String mAnswer3) {
        this.mAnswer3 = mAnswer3;
    }

    public ChattingPresenterImpl(Activity activity,
                                 ChattingView chattingView,
                                 DTingDBUtil dTingDBUtil,
                                 String userName,
                                 String robotName,
                                 String uuid,
                                 boolean isOtherChat,
                                 String headImage,
                                 String answer1, String answer2, String answer3, String uniqueId,
                                 ChattingAdapter adapter) {
        mActivity = activity;
        mChattingView = chattingView;
        mDTingDBUtil = dTingDBUtil;
        mUserName = userName;
        mRobotName = robotName;
        mUuid = uuid;
        mHeadImage = headImage;
        mIsOtherChat = isOtherChat;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mUniqueId = uniqueId;
        mAdapter = adapter;

        InitListener initCallBack = new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS)
                    ToastUtils.showShortToastSafe("讯飞语音引擎初始化失败!");
            }
        };
        mIat = SpeechRecognizer.createRecognizer(activity, initCallBack);
    }

    public void setRobotName(String robotName) {
        mRobotName = robotName;
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

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            mChattingView.updateVoiceLevel(i % 3);
        }

        @Override
        public void onBeginOfSpeech() {
            mChattingView.onBeginOfSpeech();
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
                    long endRecognitionTime = System.currentTimeMillis();
                    BigDecimal bigDecimal = new BigDecimal((endRecognitionTime - mStartRecognitionTime) / 1000);
                    float recognitionTime = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                    mChattingView.onRecordingFinish(recognitionTime, mRecognizerPath, displayText);
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
            }
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

    @Override
    public void start() {
        getChatMsg(0, mUserName);
    }

    @Override
    public void destroy() {
        if (mLocationClient != null)
            mLocationClient.stop();
    }

    @Override
    public void sendVoiceMessage(float seconds, String filePath, String text) {
        mMsg = new ChatMessageItem();
        mMsg.setUserName(mUserName);
        mMsg.setRobotName(mRobotName);
        mMsg.setType(Constants.CHAT_TYPE_SENT_VOICE);
        mMsg.setHeadPortrait(MySharedPreferences.getRobotHeadPortrait());
        mMsg.setContent(text);
        mMsg.setVoicePath(filePath);
        mMsg.setTime(System.currentTimeMillis());
        mMsg.setVoiceTime(seconds);
        mChattingView.refreshChatRecyclerView(mMsg);
        saveChatMsg(mMsg);
        getAnswer(text, true);
    }

    @Override
    public void sendTextMessage(String text) {
        if (Utils.isEmpty(text)) {
            ToastUtils.showShortToastSafe("内容不能为空");
        } else {
            mMsg = new ChatMessageItem();
            mMsg.setContent(text);
            mMsg.setHeadPortrait(MySharedPreferences.getRobotHeadPortrait());
            mMsg.setType(Constants.CHAT_TYPE_SENT_TEXT);
            mMsg.setUserName(mUserName);
            mMsg.setRobotName(mRobotName);
            mMsg.setTime(System.currentTimeMillis());
            mChattingView.refreshChatRecyclerView(mMsg);
            saveChatMsg(mMsg);
            getAnswer(text, false);
        }
    }

    @Override
    public void startRecording(long startRecordingTime) {
        mStartRecognitionTime = startRecordingTime;
        setParams();
        mIat.startListening(mRecognizerListener);
    }

    @Override
    public void stopRecording() {
        mIat.stopListening();
    }

    @Override
    public void cancelRecording() {
        mIat.cancel();
    }

    @Override
    public void sendMailMsg(String mail) {
        mMsg = new ChatMessageItem();
        mMsg.setContent(mail);
        mMsg.setHeadPortrait(MySharedPreferences.getRobotHeadPortrait());
        mMsg.setType(Constants.CHAT_TYPE_RECEIVED_MAIL);
        mMsg.setUserName(mUserName);
        mMsg.setRobotName(mRobotName);
        mMsg.setTime(System.currentTimeMillis());
        mChattingView.refreshChatRecyclerView(mMsg);
        saveChatMsg(mMsg);
    }


    @Override
    public void sendProfileMsg(String profile, String headImage) {
        mMsg = new ChatMessageItem();

        if (!Utils.isEmpty(profile)) {
            String[] strings = profile.split(SPACE);
            if (strings.length != 2) {//当老用户没有该标记时
                mMsg.setContent(Utils.isEmpty(profile) ? "我很懒! 没有写动态啦!" : profile);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
            } else {
                initWelcomeContent(strings[0], strings[1], mMsg);
            }
        }
//        mMsg.setContent(Utils.isEmpty(profile) ? "我很懒! 没有写动态啦!" : profile);
//        mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);


        mMsg.setHeadPortrait(headImage);
        mMsg.setUserName(mUserName);
        mMsg.setTime(TimeUtil.getNowTimeMills());
        mChattingView.refreshChatRecyclerView(mMsg);
    }

    /**
     * 变化欢迎仪式内容
     *
     * @param type 类型 具体详见顶部static变量
     */
    private void initWelcomeContent(String type, String content, ChatMessageItem chatMessageItem) {
        switch (type) {
            case TYPE_TEXT:
                chatMessageItem.setContent(Utils.isEmpty(content) ? "我很懒! 没有写动态啦!" : content);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
                break;
            case TYPE_PICTURE:

                mMsg.setImgUrl(content);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_IMAGE);
                break;
            case TYPE_AUDIO:
                mMsg.setAudioUrl(content);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_AUDIO);
                break;
            case TYPE_FILE:
                mMsg.setFileUrl(content);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_FILE);
                break;
        }
    }

    @Override
    public void getLocation() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(MyApplication.getInstance());
            mLocationClient.registerLocationListener(mBDLocationListener);
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            option.setIsNeedAddress(true);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        } else {
            mLocationClient.start();
        }
    }

    @Override
    public void getPrivateMsgCount() {

        ApiManager.getPersonalMessageList(MySharedPreferences.getRobotName(), 1, new PersonalMessageObserver(new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                if (result instanceof PersonalMailModel) {
                    PersonalMailModel personalMailModel = (PersonalMailModel) result;
                    if (personalMailModel.getItems() != null && personalMailModel.getItems().size() != 0) {
                        PersonalMailItemModel personalMailItemModel = personalMailModel.getItems().get(0);

                        sendMailMsg(personalMailItemModel.getForword_name() + "对你说：<br>" + personalMailItemModel.getMessage());
                        readMailMsg(personalMailItemModel);
                    }


                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {

            }
        }));


//
//        Diting.searchMailRobot(1, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                List<Mail> mailList = Utils.parseMailRobot(response, true);
//                if (mailList != null && mailList.size() != 0) {
//                    Mail mail = mailList.get(0);
//                    sendMailMsg(mail.getTitle() + "对你说：<br>" + mail.getContent());
//                    readMailMsg(mail);
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//
//            }
//        });
    }

    @Override
    public void saveChatMsg(final ChatMessageItem msg) {
        if (!Utils.isEmpty(msg.getContent())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDTingDBUtil.addMessage(msg);
                }
            }).start();
        }

    }

    @Override
    public void saveChatMsg(ChatMessageItem msg, boolean isVoice) {
        msg.setTime(System.currentTimeMillis());
        msg.setUserName(mUserName);
        msg.setRobotName(mRobotName);
        if (mIsOtherChat)
            msg.setHeadPortrait(mHeadImage);
        mChattingView.refreshChatRecyclerView(msg);
        saveChatMsg(msg);

        if (isVoice && msg.getType() == Constants.CHAT_TYPE_RECEIVED_VOICE) {
            String text;
            if (isNews) {
                text = msg.getContent().split("<br/>")[0] + ",请点击链接查看详情";
            } else {
                text = Html.fromHtml(msg.getContent()).toString();
            }

            mAdapter.speak(text, null);
        } else if (msg.getType() == Constants.CHAT_TYPE_RECEIVED_MUSIC) {
            int position = mAdapter.getData().size() - 1;
            mAdapter.changeMusicState(mAdapter.getViewHolderByPosition(position), position);
        }
    }

    @Override
    public void getChatMsg(int position, String userName) {
        List<ChatMessageItem> msg = mDTingDBUtil.queryMessage(position, userName, mRobotName);
        mChattingView.refreshChatRecyclerView(msg);
    }

    @Override
    public void getAnswer(String text, final boolean isVoice) {
        mIsVoice = isVoice;
        if (!mIsOtherChat) {
//            ApiManager.chat(text, mUuid, "",
//                    mGoogleLatitude, mGoogleLongitude, mIsVoice, MySharedPreferences.getUniversalAnswer1(),
//                    MySharedPreferences.getUniversalAnswer2(),
//                    MySharedPreferences.getUniversalAnswer3(), false,
//                    new ChatObserver(mResultCallBack), null);
            ApiManager.chat(text, mUuid, "",
                    mGoogleLatitude, mGoogleLongitude, mIsVoice, mAnswer1, mAnswer2, mAnswer3, mUniqueId, false,
                    new ChatObserver(mResultCallBack), null);
        } else {
            ApiManager.chat(text, mUuid, mUserName,
                    mGoogleLatitude, mGoogleLongitude, mIsVoice, mAnswer1, mAnswer2, mAnswer3, mUniqueId, true, null,
                    new PublicChatObserver(mResultCallBack));
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof ChatModel) {
                ChatModel chatModel = (ChatModel) result;
                analysisAnswer(chatModel, mIsVoice);
            } else if (result instanceof PublicChatModel) {
                PublicChatModel publicChatModel = (PublicChatModel) result;
                analysisOtherAnswer(publicChatModel);
            } else if (result instanceof String) {
                String s = (String) result;
                LogUtils.e(s);
            } else if (result instanceof RobotInfoModel) {
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                mActivity.startActivity(NewChatActivity.callingIntent(mActivity, robotInfoModel));
            }
        }

        @Override
        public void onResult(List<?> resultList) {

        }

        @Override
        public void onError(Object o) {

        }
    };

    private void analysisAnswer(ChatModel chatModel, boolean isVoice) {
        String answer = chatModel.getAnswer();
        String img_url = chatModel.getImg_url();
        String video_url = chatModel.getVideo_url();
        String audio_url = chatModel.getAudio_url();
        String file_url = chatModel.getFile_url();
        String domain = chatModel.getDomain();
        String scene = chatModel.getScene();
        String skip_url = chatModel.getSkip_url();
        String hyperlink_url = chatModel.getHyperlink_url();

        if (!Utils.isEmpty(scene) && scene.equals("新闻")) {
            isNews = true;
        } else {
            isNews = false;
        }

        if (Utils.isNotEmpty(skip_url)) {
            CommunicateModel communicateModel = new CommunicateModel();
            communicateModel.setNamePinyin(chatModel.getUsername());
            communicateModel.setName(chatModel.getRobotName());
            communicateModel.setRobotHeadImg(chatModel.getHeadImgUrl());
            communicateModel.setProfile(chatModel.getProfile());
            communicateModel.setCompanyName(chatModel.getCompanyName());
            communicateModel.setInvalidAnswer1(mAnswer1);
            communicateModel.setInvalidAnswer2(mAnswer2);
            communicateModel.setInvalidAnswer3(mAnswer3);
            if (!mIsOtherChat)
                communicateModel.setUniqueId(skip_url.replace("/robot-company/", ""));
//            mActivity.startActivity(NewChatActivity.callingIntent(mActivity, communicateModel));
            ApiManager.getRobotInfoByUniqueId(skip_url.replace("/robot-company/", ""), new RobotInfoObserver(mResultCallBack));

        }

        mMsg = new ChatMessageItem();
        if (!Utils.isEmpty(chatModel.getMp3Url())) {
            String singerName = chatModel.getSingerName();
            singerName = Utils.isEmpty(singerName) ? " " : singerName;
            String musicName = chatModel.getMusicName();
            musicName = Utils.isEmpty(musicName) ? " " : musicName;
            String content = musicName + "," + singerName;
            mMsg.setContent(content);
            mMsg.setVoicePath(chatModel.getMp3Url());
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_MUSIC);
        } else if (!Utils.isEmpty(video_url)) {
            mMsg.setContent(answer);
            mMsg.setVideoUrl(video_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_VIDEO);
        } else if (!Utils.isEmpty(audio_url)) {
            mMsg.setContent(answer);
            mMsg.setAudioUrl(audio_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_AUDIO);
        } else if (!Utils.isEmpty(file_url)) {
            mMsg.setContent(answer);
            mMsg.setFileUrl(file_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_FILE);
        } else if (!Utils.isEmpty(hyperlink_url)) {
            mMsg.setContent(answer);
            mMsg.setHyperlinkUrl(hyperlink_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_HYPERLINK);
        } else if (!Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
            mMsg.setContent(answer);
            mMsg.setImgUrl(img_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE);
        } else if (Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
            mMsg.setImgUrl(img_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_IMAGE);
        } else if (!Utils.isEmpty(answer) && Utils.isEmpty(img_url)) {
            if (isVoice) {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_VOICE);
            } else {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
            }
            mMsg.setContent(answer);
        }

        if (domain != null && domain.equals("alarmclock")) {
            String remindContent = chatModel.getContent();
            long remindTime = chatModel.getTime();
            if (chatModel.getAction().equals("add")) {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_REMIND);
                mChattingView.addReminder(mMsg, isVoice, remindContent, remindTime);
            } else {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
                mChattingView.deleteReminder(mMsg, isVoice, remindTime);
            }
        } else if (domain != null && domain.equals("nearbyCuisine")) {
            String nearbyCuisine = chatModel.getNearbyCuisine().replace("\\", "");
            if (!Utils.isEmpty(nearbyCuisine)) {
                mMsg.setContent(nearbyCuisine);
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_FOOD);
                saveChatMsg(mMsg, isVoice);
            }
        } else if (domain != null && domain.equals("打电话") && Utils.canCall(answer)) {
            mChattingView.onVoiceCall(answer);
        } else if (domain != null && domain.equals("视屏通话") && Utils.canCall(answer)) {
            mChattingView.onVideoCall(answer);
        } else if (scene != null && scene.equals("通用电话")) {
            mChattingView.onPhoneCall(answer.replace("。", "").replace(".", ""));
        } else {
            saveChatMsg(mMsg, isVoice);
        }
    }

    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String city = bdLocation.getCity();
            String addr = bdLocation.getAddrStr();
            double latitude = bdLocation.getLatitude();    //获取纬度信息
            double longitude = bdLocation.getLongitude();    //获取经度信息
            Point point = CoordinateConversion.bd_google_encrypt(latitude, longitude); // 百度经纬度转Google经纬度
            mGoogleLatitude = String.valueOf(point.getLat());
            mGoogleLongitude = String.valueOf(point.getLng());
            MySharedPreferences.saveCity(city);
            MySharedPreferences.saveAddr(addr);
            ApiManager.uploadLocation(mGoogleLatitude, mGoogleLongitude, new ResultMessageObserver(mResultCallBack));
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    private void analysisOtherAnswer(PublicChatModel publicChatModel) {

//        String answer = publicChatModel.getAnswer();
//        String img_url = publicChatModel.getImg_url();
//        String skip_url = publicChatModel.getSkip_url();
//

        String answer = publicChatModel.getAnswer();
        String img_url = publicChatModel.getImg_url();
        String video_url = publicChatModel.getVideoUrl();
        String audio_url = publicChatModel.getAudioUrl();
        String file_url = publicChatModel.getFileUrl();


        String skip_url = publicChatModel.getSkip_url();
        String hyperlink_url = publicChatModel.getHyperlinkUrl();


        if (!Utils.isEmpty(skip_url)) {
            CommunicateModel communicateModel = new CommunicateModel();
            communicateModel.setNamePinyin(publicChatModel.getUsername());
            communicateModel.setName(publicChatModel.getRobotName());
            communicateModel.setRobotHeadImg(publicChatModel.getHeadImgUrl());
            communicateModel.setProfile(publicChatModel.getProfile());
            communicateModel.setCompanyName(publicChatModel.getCompanyName());
            communicateModel.setInvalidAnswer1(mAnswer1);
            communicateModel.setInvalidAnswer2(mAnswer2);
            communicateModel.setInvalidAnswer3(mAnswer3);
            communicateModel.setUniqueId(skip_url.replace("/robot-company/", ""));
//            mActivity.startActivity(NewChatActivity.callingIntent(mActivity, communicateModel));

            ApiManager.getRobotInfoByUniqueId(skip_url.replace("/robot-company/", ""), new RobotInfoObserver(mResultCallBack));
        }

        mMsg = new ChatMessageItem();
//        if (!Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
//            mMsg.setContent(answer);
//            mMsg.setImgUrl(img_url);
//            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE);
//        } else if (Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
//            mMsg.setImgUrl(img_url);
//            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_IMAGE);
//        } else if (!Utils.isEmpty(answer) && Utils.isEmpty(img_url)) {
//            if (mIsVoice) {
//                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_VOICE);
//            } else {
//                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
//            }
//            mMsg.setContent(answer);
//        }


        if (!Utils.isEmpty(video_url)) {
            mMsg.setContent(answer);
            mMsg.setVideoUrl(video_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_VIDEO);
        } else if (!Utils.isEmpty(audio_url)) {
            mMsg.setContent(answer);
            mMsg.setAudioUrl(audio_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_AUDIO);
        } else if (!Utils.isEmpty(file_url)) {
            mMsg.setContent(answer);
            mMsg.setFileUrl(file_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_FILE);
        } else if (!Utils.isEmpty(hyperlink_url)) {
            mMsg.setContent(answer);
            mMsg.setHyperlinkUrl(hyperlink_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_HYPERLINK);
        } else if (!Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
            mMsg.setContent(answer);
            mMsg.setImgUrl(img_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT_AND_IMAGE);
        } else if (Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
            mMsg.setImgUrl(img_url);
            mMsg.setType(Constants.CHAT_TYPE_RECEIVED_IMAGE);
        } else if (!Utils.isEmpty(answer) && Utils.isEmpty(img_url)) {
            if (mIsVoice) {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_VOICE);
            } else {
                mMsg.setType(Constants.CHAT_TYPE_RECEIVED_TEXT);
            }
            mMsg.setContent(answer);
        }


        if (!Utils.isEmpty(answer)) {
            saveChatMsg(mMsg, mIsVoice);
        }

    }

    private void readMailMsg(PersonalMailItemModel mail) {

        ApiManager.personalMailIsRead(mail.getId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

                    if (jsonObject.getString("message").equals("保存成功！")) {
//
                        MyApplication.unreadMessageNum--;
                        if (MyApplication.unreadMessageNum <= 0) {
                        }
                        if (!Utils.hasUnreadMail()) {
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {

            }
        });

//        Diting.editMailRobot(mail.getId(), new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                MyApplication.unreadMessageNum--;
//                if (MyApplication.unreadMessageNum <= 0) {
//                }
//                if (!Utils.hasUnreadMail()) {
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//
//            }
//        });
    }
}
