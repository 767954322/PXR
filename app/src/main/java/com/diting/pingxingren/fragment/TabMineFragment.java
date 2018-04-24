package com.diting.pingxingren.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.activity.ChatActivity;
import com.diting.pingxingren.activity.MailNewActivity;
import com.diting.pingxingren.activity.RobotPairActivity;
import com.diting.pingxingren.activity.SkinningActivity;
import com.diting.pingxingren.activity.TreasureChestActivity;
import com.diting.pingxingren.activity.VideoCallActivity;
import com.diting.pingxingren.activity.VoiceCallActivity;
import com.diting.pingxingren.activity.VoicePeopleActivity;
import com.diting.pingxingren.adapter.AppAdapter;
import com.diting.pingxingren.adapter.MessageAdapter;
import com.diting.pingxingren.adapter.OnRecyclerItemClickListener;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.AudioRecordButton;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.db.DitingDB;
import com.diting.pingxingren.entity.AppGuide;
import com.diting.pingxingren.entity.Mail;
import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.CalanderUtil;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.HttpStringCallBack;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.InstallationUtils;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.PermissionUtils;
import com.diting.pingxingren.util.Pinyin4jUtil;
import com.diting.pingxingren.util.PopupWinUtil;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.TextLengthUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.map.CoordinateConversion;
import com.diting.pingxingren.util.map.Point;
import com.diting.voice.DITingVoice;
import com.diting.voice.data.body.Contact;
import com.diting.voice.data.model.CallInfo;
import com.diting.voice.utils.CallManagerUtils;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.diting.pingxingren.util.Const.URL_ROBOT;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_CALL_PHONE;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.diting.pingxingren.util.PermissionUtils.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR;
import static com.diting.pingxingren.util.Utils.hasUnreadMail;

/**
 * Created by asus on 2017/2/22.
 */

public class TabMineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView msgListView;
    private EditText inputText;
    private MessageAdapter adapter;
    private List<Msg> msgList = new ArrayList<>();
    private Button send;
    private String uuid;
    private SwipeRefreshLayout mSwipeLayout;
    private DitingDB ditingDB;
    private Thread myThread;
    private String username;
    private ImageView iv_voice;
    private AudioRecordButton btn_voice;
    private ListView lv_phrase;
    private ArrayAdapter<String> phraseAdapter;
    private String[] phrases = Const.phraseGroup;
    private boolean isPhrase = true;
    private TitleBarView titleBarView;
    private static final int REQUEST_CODE = 3;
    private String phone;
    private boolean isRunning;
    private boolean isFromContacts;
    private ObjectAnimator mBottomAnimator;
    private View mBottom_bar;
    private LinearLayout mMineLinearLayout;
    private List<AppGuide> appList = new ArrayList<AppGuide>();
    private int[] appResourceIds = {
            R.mipmap.icon_app_time,
            R.mipmap.icon_app_joke,
            R.mipmap.icon_app_story,
            R.mipmap.icon_app_news,
            R.mipmap.icon_app_music,
            R.mipmap.icon_app_weather,
            R.mipmap.icon_app_constellation,
            R.mipmap.icon_app_calculate,
            R.mipmap.icon_app_translate,
            R.mipmap.icon_app_phone,
            R.mipmap.icon_app_transform,
            R.mipmap.icon_app_notify,
            R.mipmap.icon_app_search
    };
    private String[] apps = {"时间", "笑话", "讲故事", "新闻", "音乐", "天气", "星座", "计算", "翻译", "呼叫", "变身", "发私信", "查找"};
    private String[] appExamples = {
            "现在几点了",
            "讲个笑话",
            "讲个故事",
            "看新闻",
            "播放音乐",
            "北京天气",
            "天蝎座",
            "52乘以52",
            "翻译",
            "呼叫小谛",
            "变身小谛",
            "告诉小谛说...",
            "查找小谛"
    };
    private String[] callStrs = {"呼叫小谛", "打电话给小谛", "给小谛打电话"};
    private String[] notifyStrs = {"告诉小谛说我想你了"};
    private String translateStr = "翻译很高兴认识你";
    //百度地图
    private LocationClient locationClient;

    private String city;

    private String remindContent;
    private long time;
    private Msg msg;
    private boolean isNews = false;
    private boolean isVoiceCall = false;
    private boolean isVideoCall = false;
    private Bitmap photo;
    private boolean isRight;
    private String lat = null;
    private String lng = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //initLocation();

        requestLocationPermission();
    }


    private void initLocation() {
        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.registerLocationListener(bdLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String latitude = bdLocation.getLatitude() + "";
            String longitude = bdLocation.getLongitude() + "";
            Point point = CoordinateConversion.bd_google_encrypt(bdLocation.getLatitude(), bdLocation.getLongitude());
            lat = point.getLat() + "";
            lng = point.getLng() + "";
            Diting.updateLocation(latitude, longitude, new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    LogUtils.d("上传位置信息成功");
                }

                @Override
                public void onFailed(VolleyError error) {
                    LogUtils.d("上传位置信息失败");
                }
            });
            LogUtils.d(point.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robot_tab, null);
        ditingDB = MyApplication.ditingDB;
        initViews(view);
        initEvents();
        MobclickAgent.onProfileSignIn(username);
        DITingVoice.getInstance().createAccount(username);
        getMailCount();
        return view;
    }

    private void getMailCount() {
        Diting.searchMailCount(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    MyApplication.unreadLetterNum = response.getInt("unreadLetterNum");
                    MyApplication.unreadMessageNum = response.getInt("unreadMessageNum");
                    if (hasUnreadMail()) {
                        EventBus.getDefault().post("showRedPoint");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initMsg();
            }

            @Override
            public void onFailed(VolleyError error) {
                initMsg();
            }
        });
    }

    private void loadMoreMessage() {
        EMConversation conversation = EMClient.getInstance()
                .chatManager()
                .getConversation("13161202845", EMConversation.EMConversationType.GroupChat, true);
        //String msgId = conversation.getAllMessages().get(0).getMsgId();
        List<EMMessage> list = conversation.getAllMessages();
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            String text = ((TextView) v).getText().toString();
            PopupWinUtil.showPopWindow(text, TabMineFragment.this);
            return false;
        }
    };

    private void initTitleBarView(View view) {
        titleBarView =   view.findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setTitleText("与" + MySharedPreferences.getRobotName() + "沟通");
        titleBarView.setBtnRight(R.mipmap.icon_more, null);
        titleBarView.setBtnLeft(R.drawable.ic_treasure_chest, null, 30);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showAppList();
                startActivity(TreasureChestActivity.class);
            }
        });
        //titleBarView.addSubmenu(titleBarView.getBtnRight());
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSubMenu();
                Utils.hideSoftInput(getContext(),titleBarView);
                showTopRightMenu();
            }
        });
        if (MyApplication.unreadMessageNum > 0) {
            titleBarView.showPoint();
        }
    }


    private void showAppList() {
        appList.clear();
        for (int i = 0; i < apps.length; i++) {
            AppGuide appGuide = new AppGuide();
            appGuide.setAppName(apps[i]);
            appGuide.setAppExample(appExamples[i]);
            appGuide.setResourceId(appResourceIds[i]);
            appList.add(appGuide);
        }
        View parent = getActivity().getWindow().getDecorView();
        View popView = View.inflate(getActivity(), R.layout.popupwin_app_list, null);
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        AppAdapter adapter = new AppAdapter(appList, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        final PopupWindow popupWindow = new PopupWindow(popView, parent.getWidth() - ScreenUtil.dip2px(getActivity(), 50), ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//设置允许在外点击消失
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //item点击事件
                int position = vh.getAdapterPosition();
                String text = appList.get(position).getAppExample();
                if (text.equals("呼叫小谛")) {
                    text = Utils.getRandomString(callStrs);
                } else if (text.equals("告诉小谛说...")) {
                    text = Utils.getRandomString(notifyStrs);
                } else if (text.equals("翻译")) {
                    text = translateStr;
                }
                if (text.equals("变身小谛")) {
                    sendMsg(text, true);
                } else {
                    sendMsg(text, false);
                }
                popupWindow.dismiss();
            }
        });

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void initViews(View view) {
        initTitleBarView(view);
        uuid = InstallationUtils.getUuid(getContext());
        mSwipeLayout =   view.findViewById(R.id.id_swipe_ly);
        msgListView =   view.findViewById(R.id.msg_list_view);
        inputText =   view.findViewById(R.id.input_text);
        send =  view.findViewById(R.id.send);
        username = MySharedPreferences.getUsername();
        iv_voice = view.findViewById(R.id.iv_voice);
        btn_voice =  view.findViewById(R.id.btn_voice);
        lv_phrase =   view.findViewById(R.id.lv_phrase);
        mBottom_bar = getActivity().findViewById(R.id.rl_bottom);
        mMineLinearLayout =   view.findViewById(R.id.llMine);
    }

    private void initEvents() {
        inputText.setFilters(new InputFilter[]{new TextLengthUtil(this.getActivity(), 101, "输入问题的内容不能超过100个字")});
        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_phrase.getVisibility() == View.VISIBLE) {
                    lv_phrase.setVisibility(View.GONE);
                }
                if (btn_voice.getVisibility() == View.VISIBLE) {
                    inputText.setVisibility(View.VISIBLE);
                    btn_voice.setVisibility(View.GONE);
                    iv_voice.setImageResource(R.mipmap.voice_btn_normal);
                    Utils.showSoftInput(getContext(), inputText);
                } else {
                    isVoiceCall = false;
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                            Utils.showMissingPermissionDialog(getActivity(), "录音");
                        } else {
                            TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
                        }
                    } else {
                        setAudioRecordEnable();
                    }
                }
            }
        });

        btn_voice.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath, String text) {
                // TODO Auto-generated method stub
                sendVoice(seconds, filePath, text);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                adapter.stopPlayVoice();
            }
        });

        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_phrase.getVisibility() == View.VISIBLE) {
                    lv_phrase.setVisibility(View.GONE);
                }
            }
        });

        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBottom_bar.setVisibility(View.GONE);
                } else {
                    mBottom_bar.setVisibility(View.VISIBLE);
                }
            }
        });

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //内容改变前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    send.setText("发送");
                    send.setBackgroundResource(R.drawable.send_btn_enable);
                    send.setTextColor(Color.parseColor("#ffffff"));
                    isPhrase = false;
                } else {
                    send.setText("常用语");
                    send.setBackgroundResource(R.drawable.send_btn_disable);
                    send.setTextColor(Color.parseColor("#969696"));
                    isPhrase = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //结果显示在输入框中
            }
        });
        mSwipeLayout.setOnRefreshListener(this);
        //常用语
//        phraseAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,phrases);
        phraseAdapter = new ArrayAdapter<String>(getContext(), R.layout.phrase_item, phrases);
        lv_phrase.setAdapter(phraseAdapter);
        lv_phrase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_phrase.setVisibility(View.GONE);
                sendMsg(phrases[position], false);
            }
        });

        adapter = new MessageAdapter(getActivity(), msgList);
        adapter.setOnLongClickListener(onLongClickListener);
        adapter.setVoiceIsReadListener(new MessageAdapter.VoiceIsRead() {

            @Override
            public void voiceOnClick(int position) {
                // TODO Auto-generated method stub
                for (int i = 0; i < adapter.getUnReadPosition().size(); i++) {
                    if (adapter.getUnReadPosition().get(i).equals(position + "")) {
                        adapter.getUnReadPosition().remove(i);
                        break;
                    }
                }
            }
        });
        adapter.setIsMine(true);
        adapter.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRight = true;
                ImageUtil.showChoosePicDialog(TabMineFragment.this);
            }
        });
        adapter.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRight = false;
                ImageUtil.showChoosePicDialog(TabMineFragment.this);
            }
        });

//        msgListView.setPullLoadEnable(false);
//        msgListView.setXListViewListener(this);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPhrase) {
                    String content = inputText.getText().toString();
                    sendMsg(content, false);
                } else {
                    togglePhraseGroup();
                }
            }
        });

        msgListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(getActivity(), msgListView);
                lv_phrase.setVisibility(View.GONE);
                adapter.stopPlayVoice();
                inputText.clearFocus();
                return false;
            }
        });
        /*msgListView.setOnTouchListener(new View.OnTouchListener() {
            private float mEndY;
            private float mStartY;
            private int direction;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(getActivity(), msgListView);
                lv_phrase.setVisibility(View.GONE);
                adapter.stopPlayVoice();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndY = event.getY();
                        float v1 = mEndY - mStartY;
                        if (v1 < -3) {
                            // mBottom_bar.setVisibility(View.GONE);
                            mStartY = mEndY;
                            return false;
                        }
//                        if (v1 > 3 && !isRunning && direction == 1) {
//                            direction = 0;
//                            hideBar();
//                            mStartY = mEndY;
//                            return false;
//                        } else if (v1 < -3 && !isRunning && direction == 0) {
//                            direction = 1;
//                            showBar();
//                            mStartY = mEndY;
//                            return false;
//                        }
//                        mStartY = mEndY;

                        break;
                    case MotionEvent.ACTION_UP:
                        mBottom_bar.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });*/
    }

    public void hideBar() {
        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar, "translationY", mBottom_bar.getHeight());
        mBottomAnimator.setDuration(300).start();
        mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
                mBottom_bar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 显示导航栏
     */
    public void showBar() {
//        mBottomAnimator = ObjectAnimator.ofFloat(mBottom_bar, "translationY", 0);
//        mBottomAnimator.setDuration(300).start();
        ValueAnimator mBottomAnimator = ValueAnimator.ofInt(1, 100);
        mBottomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mBottom_bar.getLayoutParams().height = mEvaluator.evaluate(fraction, mBottom_bar.getHeight(), 0);
                mBottom_bar.requestLayout();
            }
        });
        mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isRunning = true;
            }


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRunning = false;
            }
        });
        mBottomAnimator.setDuration(5000).start();
    }

    private void setAudioRecordEnable() {
        inputText.setVisibility(View.GONE);
        btn_voice.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
        Utils.hideSoftInput(getActivity(), inputText);
    }

    private void togglePhraseGroup() {
        if (lv_phrase.getVisibility() == View.GONE) {
            lv_phrase.setVisibility(View.VISIBLE);
        } else {
            lv_phrase.setVisibility(View.GONE);
        }
    }

    public void sendMsg(String content, boolean isFromApp) {
        if (!"".equals(content)) {
            Msg msg = new Msg();
            msg.setContent(content);
            msg.setHeadPortrait(MySharedPreferences.getHeadPortrait());
            msg.setType(Msg.TYPE_SENT);
            msg.setUserName(username);
            msg.setTime(TimeUtil.getNowTimeMills());
            stopGuide();
            adapter.upDateMsg(msg);
            msgListView.setSelection(ListView.FOCUS_DOWN);
            saveMsg(msg);
            inputText.setText("");
            getAnswer(content, isFromApp, false);
        }
    }

    private void callPhone(String phone, boolean isFromContact) {
        adapter.stopMusic();
        if (isFromContact) {
            call(phone);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            startActivity(intent);
        }
    }


    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            //photo = ImageUtil.toRoundBitmap(photo, ImageUtil.tempUri); // 这个时候的图片已经被处理成圆形的了
            if (!isRight) {
                File file = FileSaveUtil.saveBitmap(photo);
                if (file != null) {
                    MySharedPreferences.saveLeftHeadImage(file.getAbsolutePath());
                    adapter.notifyDataSetChanged();
                }
                return;
            }
            if (photo != null) {
                showLoadingDialog("请求中");
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, null, null));
                File file = new File(Utils.getRealFilePath(getActivity(), uri));
                Diting.uploadImage(file, new HttpStringCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        String img_url = null;
                        try {
                            img_url = new JSONObject(response).getString("url") + "?imageMogr2";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setRobotInfo(img_url);
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        if (error.networkResponse == null) {
                            showShortToast("请求超时！");
                        } else {
                            try {
                                showShortToast(new JSONObject(new String(error.networkResponse.data)).getString("message"));
                            } catch (JSONException e) {
                            }
                        }
                    }
                });
            }

        }
    }

    private void setRobotInfo(final String img_url) {
        Diting.setCompanyInfo(MyApplication.companyName, null, img_url, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
//                MyApplication.headPortrait = img_url;

                MySharedPreferences.saveHeadPortrait(img_url);
                EventBus.getDefault().post("updateHeadImage");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                if (error.networkResponse == null) {
                    showShortToast("请求超时！");
                } else {
                    try {
                        showShortToast(new JSONObject(new String(error.networkResponse.data)).getString("message"));
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(phone, isFromContacts);
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启拨号权限");
                Utils.showMissingPermissionDialog(getActivity(), "拨号");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_AUDIO_RECORD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isVoiceCall) {
                    voiceCall(phone);
                } else if (isVideoCall) {
                    videoCall(phone);
                } else {
                    setAudioRecordEnable();
                }

            } else {
                // Permission Denied
                //showShortToast("请到设置中开启录音权限");
                Utils.showMissingPermissionDialog(getActivity(), "录音");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isVideoCall) {
                    requestVideoPermission();
                } else {
                    gotoCapture();
                }
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启相机权限");
                Utils.showMissingPermissionDialog(getActivity(), "相机");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_CALENDAR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addCalander(msg);
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启日历权限");
                Utils.showMissingPermissionDialog(getActivity(), "日历");
            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启位置权限");
                Utils.showMissingPermissionDialog(getActivity(), "位置");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void call(String content) {
        List<Contact> list = Utils.readContacts(getContext());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (Pinyin4jUtil.converterToSpell(content).contains(Pinyin4jUtil.converterToSpell(list.get(i).getName()))) {
                    addCallMsg("拨打手机通讯录电话");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(i).getPhone()));
                    startActivity(intent);
                }
            }
        }
    }

    private void addCallMsg(String text) {
        Msg msg = new Msg();
        msg.setContent(text);
        msg.setType(Msg.TYPE_RECEIVED_TEXT);
        msg.setTime(TimeUtil.getNowTimeMills());
        msg.setUserName(username);
        adapter.upDateMsg(msg);
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        saveMsg(msg);
    }

    private void initMsg() {
        msgList = ditingDB.listMessage(0, username);
        Collections.reverse(msgList);
        adapter.setMessageList(msgList);
        //msgListView.setSelection(msgList.size()-1);
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        //每日第一次登录时间判断
        String loginTime = TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD);
        if (!loginTime.equals(MySharedPreferences.getLoginTime())) {
            MySharedPreferences.saveLoginTime(loginTime);
            addWelcome(true);
        } else {
            addRobotMessage();
        }
    }

    private void addRobotMessage() {
        Diting.searchMailRobot(1, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                List<Mail> mailList = Utils.parseMailRobot(response, true);
                if (mailList != null && mailList.size() != 0) {
                    Mail mail = mailList.get(0);
                    addWelcomeMsg(mail.getTitle() + "对你说：<br>" + mail.getContent(), true);
                    deleteMailRobot(mail);
                } else {
                    addWelcome(false);
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    private void deleteMailRobot(Mail mail) {
        Diting.editMailRobot(mail.getMailId(), new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                MyApplication.unreadMessageNum--;
                if (MyApplication.unreadMessageNum <= 0) {
                    EventBus.getDefault().post("NoRobotMail");
                }
                if (!Utils.hasUnreadMail()) {
                    EventBus.getDefault().post("hideRedPoint");
                }
                LogUtils.d("标为已读成功");
            }

            @Override
            public void onFailed(VolleyError error) {
                LogUtils.d("标为已读失败");
            }
        });
    }

    private void addWelcomeMsg(String welcome, boolean isMail) {
        Msg msg = new Msg();
        msg.setUserName(username);
        msg.setTime(TimeUtil.getNowTimeMills());
        if (isMail) {
            msg.setType(Msg.TYPE_RECEIVED_MAIL);
        } else {
            msg.setType(Msg.TYPE_RECEIVED_TEXT);
        }
        msg.setContent(welcome);
        msgList.add(msg);
        adapter.notifyDataSetChanged();
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        //saveMsg(msg);
    }

    private void addWelcome(final boolean isProfile) {
        Diting.getRobotInfo(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String profile = response.getString("profile");
                    String welcome = response.getString("welcomes");
                    if (isProfile) {
                        addWelcomeMsg(Utils.isEmpty(profile.trim()) ? Utils.getRandomWelcome() : profile, false);
                    } else {
                        addWelcomeMsg(Utils.isEmpty(welcome.trim()) ? Utils.getRandomWelcome() : welcome, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Utils.showMissingPermissionDialog(getActivity(), "位置");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            initLocation();
        }
    }


    private void requestCallPermission(String phone, boolean isFromContacts) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                Utils.showMissingPermissionDialog(getActivity(), "拨号");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            callPhone(phone, isFromContacts);
        }
    }

    private void requestWriteCalendarPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                Utils.showMissingPermissionDialog(getActivity(), "日历");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        } else {
            addCalander(msg);
        }
    }


    private void requestDeleteCalendarPermission(boolean isVoice, boolean isFromApp) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                Utils.showMissingPermissionDialog(getActivity(), "日历");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        } else {
            CalanderUtil.deleteCalendarEvent(getContext(), time);
            msg.setType(Msg.TYPE_RECEIVED_TEXT);

            addMsg(msg, isVoice, isFromApp);
        }
    }


    private void addCalander(Msg msg) {
        CalanderUtil.addCalendarEvent(getContext(), remindContent, remindContent, time);
        addMsg(msg, false, false);
    }

    private void addMsg(Msg msg, boolean isVoice, boolean isFromApp, boolean isSave) {
        msg.setTime(TimeUtil.getNowTimeMills());
        msg.setUserName(username);
        adapter.upDateMsg(msg);
        if (isVoice && msg.getType() == Msg.TYPE_RECEIVED_VOICE) {
            String text = null;
            if (isNews) {
                text = msg.getContent().split("<br/>")[0] + ",请点击链接查看详情";
            } else {
                text = Html.fromHtml(msg.getContent()).toString();
            }
            adapter.speak(text, null);
        }
        if (msg.getType() == Msg.TYPE_RECEIVED_MUSIC) {
            adapter.changeMusicState(msgList.size() - 1);
        }
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        if (isSave) {
            saveMsg(msg);
        }
        if (isFromApp) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMsg("1", false);
                }
            }, 1500);
        }
    }

    private void addMsg(Msg msg, boolean isVoice, boolean isFromApp) {
        addMsg(msg, isVoice, isFromApp, true);
    }


    private void getAnswer(String content, final boolean isFromApp, final boolean isVoice) {
        adapter.stopMusic();
//        if(content.startsWith("打电话给")&&MySharedPreferences.getContactsPermission()){
//            isFromContacts = true;
//            requestCallPermission(content,true);
//        }
//        if(content.equals("附近美食")){
//            final Msg msg = new Msg();
//            msg.setType(Msg.TYPE_RECEIVED_FOOD);
//            Diting.searchFood(new HttpCallBack() {
//                @Override
//                public void onSuccess(JSONObject response) {
//                    LogUtils.d(response);
//                    try {
//                        JSONObject jsonObject = response.getJSONObject("object");
//                        String jsonArray = jsonObject.getString("result").replace("\\","");
//                        msg.setContent(jsonArray);
//                        addMsg(msg,isVoice,isFromApp);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailed(VolleyError error) {
//                    LogUtils.d(error);
//                }
//            });
//            return;
//        }
        Diting.getAnswer(uuid, content, isVoice, lat, lng, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String answer = response.getString("answer");
                    String img_url = response.getString("img_url");
                    String video_url = response.getString("video_url");
                    String audio_url = response.getString("audio_url");
                    String file_url = response.getString("file_url");
                    String domain = response.getString("domain");
                    String scene = response.getString("scene");
                    MyApplication.toRobotName = response.getString("robotName");
                    MyApplication.toCompanyName = response.getString("companyName");
                    MyApplication.toHeadImgUrl = response.getString("headImgUrl");
                    if (!Utils.isEmpty(scene) && scene.equals("新闻")) {
                        isNews = true;
                    } else {
                        isNews = false;
                    }
//                    if(!Utils.isEmpty(response.getString("changeBooks"))) {
//                        jsonArray = response.getJSONArray("changeBooks");
//                    }
                    if (!Utils.isEmpty(response.getString("skip_url"))) {
                        transForm(response);
                    }
                    msg = new Msg();
//                    if(jsonArray != null && jsonArray.length()!=0){
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        Book book = new Book();
//                        book.setAuthor(jsonObject.getString("author"));
//                        book.setName(jsonObject.getString("name"));
//                        book.setPicture(jsonObject.getString("picture"));
//                        book.setPrice(jsonObject.getString("price"));
//                        book.setSynopsis(jsonObject.getString("synopsis"));
//                        book.setShare(jsonObject.getString("share"));
//                        msg.setBook(book);
//                        //msg.setType(Msg.TYPE_RECEIVED_BOOK);
//                    }
//
                    if (!Utils.isEmpty(response.getString("mp3Url"))) {
                        String singerName = Utils.isEmpty(response.getString("singerName")) ? " " : response.getString("singerName");
                        String musicName = Utils.isEmpty(response.getString("musicName")) ? " " : response.getString("musicName");
                        String content = musicName + "," + singerName;
                        msg.setContent(content);
                        msg.setVoicePath(response.getString("mp3Url"));
                        msg.setType(Msg.TYPE_RECEIVED_MUSIC);
                    } else if (!Utils.isEmpty(video_url)) {
                        msg.setContent(answer);
                        msg.setVideoUrl(video_url);
                        msg.setType(Msg.TYPE_RECEIVED_VIDEO);
                    } else if (!Utils.isEmpty(audio_url)) {
                        msg.setContent(answer);
                        msg.setAudioUrl(audio_url);
                        msg.setType(Msg.TYPE_RECEIVED_AUDIO);
                    } else if (!Utils.isEmpty(file_url)) {
                        msg.setContent(answer);
                        msg.setFileUrl(file_url);
                        msg.setType(Msg.TYPE_RECEIVED_FILE);
                    } else if (!Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
                        msg.setContent(answer);
                        msg.setImgUrl(img_url);
                        msg.setType(Msg.TYPE_RECEIVED_TEXT_AND_IMAGE);
                    } else if (Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
                        msg.setImgUrl(img_url);
                        msg.setType(Msg.TYPE_RECEIVED_IMAGE);
                    } else if (!Utils.isEmpty(answer) && Utils.isEmpty(img_url)) {
                        if (isVoice) {
                            msg.setType(Msg.TYPE_RECEIVED_VOICE);
                        } else {
                            msg.setType(Msg.TYPE_RECEIVED_TEXT);
                        }
                        msg.setContent(answer);
                    }
                    if (domain != null && domain.equals("打电话") && Utils.canCall(answer)) {
                        phone = answer;
                        //requestCallPermission(phone,false);
                        //voiceCall(phone);
                        requestAudioRecordPermission();
                    } else if (domain != null && domain.equals("视屏通话") && Utils.canCall(answer)) {
                        phone = answer;
                        //requestCallPermission(phone,false);
                        //voiceCall(phone);
                        requestCameraPermission();
                    } else if (scene != null && scene.equals("通用电话")) {
                        phone = answer.replace("。", "").replace(".", "");
                        requestCallPermission(phone, false);
                    }
                    //提醒
                    else if (domain != null && domain.equals("alarmclock")) {
                        msg.setType(Msg.TYPE_RECEIVED_REMIND);
                        remindContent = response.getString("content");
                        time = response.getLong("time");
                        if (response.getString("action").equals("add")) {
                            requestWriteCalendarPermission();
                        } else {
                         /*   if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
                                    Utils.showMissingPermissionDialog(getActivity(), "日历");
                                } else {
                                    TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                                }
                            } else {
                                CalanderUtil.deleteCalendarEvent(getContext(),  time);
                                msg.setType(Msg.TYPE_RECEIVED_TEXT);

                                addMsg(msg, isVoice, isFromApp);
                            }*/
                            requestDeleteCalendarPermission(isVoice, isFromApp);
                        }
                    }
                    //附近美食
                    else if (domain != null && domain.equals("nearbyCuisine")) {
                        String nearbyCuisine = response.getString("nearbyCuisine").replace("\\", "");
                        if (!Utils.isEmpty(nearbyCuisine)) {
                            msg.setContent(nearbyCuisine);
                            msg.setType(Msg.TYPE_RECEIVED_FOOD);
                            addMsg(msg, isVoice, isFromApp);
                        }
                    } else if (domain != null && domain.equals("无聊游戏")) {
                        String person = response.getString("answer1");
                        String food = response.getString("answer2");
                        String num = response.getString("answer3");
                        addMsg(msg, isVoice, isFromApp);
                        Msg playMsg = new Msg();
                        playMsg.setType(Msg.TYPE_PLAY);
                        playMsg.setFromUserName(person);
                        playMsg.setContent(food);
                        playMsg.setImgUrl(num);
                        addMsg(playMsg, false, false, false);
                    } else {
                        addMsg(msg, isVoice, isFromApp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                showShortToast("请求超时！");
            }
        });
    }

    private void requestAudioRecordPermission() {
        isVoiceCall = true;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                Utils.showMissingPermissionDialog(getActivity(), "录音");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
            }
        } else {
            voiceCall(phone);
        }
    }

    private void requestCameraPermission() {
        isVideoCall = true;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                Utils.showMissingPermissionDialog(getActivity(), "相机");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, PermissionUtils.MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            requestVideoPermission();
        }
    }

    private void requestVideoPermission() {
        isVideoCall = true;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                Utils.showMissingPermissionDialog(getActivity(), "录音");
            } else {
                TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PermissionUtils.MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
            }
        } else {
            videoCall(phone);
        }
    }

    private void voiceCall(String phone) {
        CallManagerUtils.getInstance().setChatId(phone);
        CallManagerUtils.getInstance().setInComingCall(false);
        CallManagerUtils.getInstance().setCallType(CallManagerUtils.CallType.VOICE);
        CallManagerUtils.getInstance().setVoiceClass(VoiceCallActivity.class);
        String head_img = MySharedPreferences.getHeadPortrait();
        if (head_img.equals("")) {
            head_img = "null";
        }
        CallInfo callInfo = new CallInfo(
                MySharedPreferences.getRobotName(), MyApplication.companyName,
                TimeUtil.millis2String(TimeUtil.getNowTimeMills(), TimeUtil.DEFAULT_PATTERN), head_img);
        startActivity(VoiceCallActivity.getCallingIntent(getContext(), new Gson().toJson(callInfo)));
    }

    private void videoCall(String phone) {
        CallManagerUtils.getInstance().setChatId(phone);
        CallManagerUtils.getInstance().setInComingCall(false);
        CallManagerUtils.getInstance().setCallType(CallManagerUtils.CallType.VIDEO);
        CallManagerUtils.getInstance().setVideoClass(VideoCallActivity.class);
        String head_img = MySharedPreferences.getHeadPortrait();
        if (head_img.equals("")) {
            head_img = "null";
        }
        CallInfo callInfo = new CallInfo(MySharedPreferences.getRobotName(),
                MyApplication.companyName, TimeUtil.millis2String(
                        TimeUtil.getNowTimeMills(), TimeUtil.DEFAULT_PATTERN), head_img);
        startActivity(VideoCallActivity.getCallingIntent(getContext(), new Gson().toJson(callInfo)));
    }

    //变身
    private void transForm(JSONObject response) {
        RobotConcern robotConcern = new RobotConcern();
        try {
            robotConcern.setUsername(response.getString("username"));
            robotConcern.setRobotName(response.getString("robotName"));
            robotConcern.setHeadPortrait(response.getString("headImgUrl"));
            robotConcern.setWelcome(response.getString("welcome"));
            robotConcern.setCompanyName(response.getString("companyName"));
            robotConcern.setProfile(response.getString("profile"));
            robotConcern.setUniqueId(response.getString("skip_url").replace("/robot-company/", ""));
            robotConcern.setConcerned(false);
            robotConcern.setFansCount(0);
            robotConcern.setPhone("160");
            robotConcern.setRobotValue(50.0);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("robot", robotConcern);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
        //int position = adapter.getCount();
        int position = msgList.size();
        List<Msg> list = ditingDB.listMessage(position, username);
        if (list != null && list.size() != 0) {
            Collections.reverse(msgList);
            msgList.addAll(list);
            Collections.reverse(msgList);
            adapter.setMessageList(msgList);
//            msgListView.setSelection(adapter.getCount() - position - 1);
            msgListView.setSelection(msgList.size() - position - 1);
        }
    }

    private void saveMsg(final Msg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ditingDB.saveMessage(msg);
            }
        }).start();
    }

    private void guide() {
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Const.questions.length; i++) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        break;
                    }
                    getGuideAnswer(Const.questions[i]);
                }

                for (int i = 1; i <= 20; i++) {
                    if (i != 1) {
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            break;
                        }
                    }
                    getGuideAnswer(Utils.getRandomQuestion());
                }
            }
        });
        myThread.start();
    }

    private void getGuideAnswer(final String question) {
        final Msg msg = new Msg();
        msg.setContent(question);
        msg.setHeadPortrait(MySharedPreferences.getHeadPortrait());
        msg.setType(Msg.TYPE_SENT);
        msg.setTime(TimeUtil.getNowTimeMills());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.upDateMsg(msg);
                msgListView.setSelection(msgList.size() - 1);
                getAnswer(question, true, false);
            }
        });
    }


    private void stopGuide() {
        if (myThread != null && !myThread.isInterrupted()) {
            myThread.interrupt();
        }
    }

    @Override
    public void onResume() {
        //locationClient.start();
        super.onResume();
        int skinRes = MySharedPreferences.getSkinRes();
        if (skinRes == -1)
            mMineLinearLayout.setBackgroundResource(R.drawable.icon_skin_default);
        else if (skinRes == R.drawable.ic_skin_one
                || skinRes == R.drawable.ic_skin_two
                || skinRes == R.drawable.ic_skin_three
                || skinRes == R.drawable.ic_skin_four
                || skinRes == R.drawable.ic_skin_five
                || skinRes == R.drawable.ic_skin_six)
            mMineLinearLayout.setBackgroundResource(skinRes);
        else
            mMineLinearLayout.setBackgroundResource(R.drawable.icon_skin_default);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopGuide();
        //locationClient.stop();
        Diting.cancelAll();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mailReaded(String message) {
        if (message.equals("NoRobotMail")) {
            titleBarView.hidePoint();
        }
        if (message.equals("updateHeadImage")) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        MediaManager.pause();
        MediaManager.release();
        btn_voice.destroyIat();
        adapter.destroy();
        locationClient.stop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopGuide();
        }
    }

    private void sendVoice(final float seconds, final String filePath, final String text) {
        Msg msg = new Msg();
        msg.setUserName(username);
        msg.setHeadPortrait(MySharedPreferences.getHeadPortrait());
        msg.setType(Msg.TYPE_SEND_VOICE);
        msg.setContent(text);
        msg.setVoicePath(filePath);
        msg.setTime(TimeUtil.getNowTimeMills());
        msg.setVoiceTime(seconds);
        adapter.upDateMsg(msg);
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        saveMsg(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAnswer(text, false, true);
            }
        }, 200);

    }


    private void showTopRightMenu() {
        final TopRightMenu mTopRightMenu = new TopRightMenu(getActivity());

//添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem(R.mipmap.icon_qr_code, "分享二维码"));
        menuItems.add(new MenuItem(R.mipmap.icon_pair, "速配"));
        menuItems.add(new MenuItem(R.mipmap.icon_share_url, "分享链接"));
        menuItems.add(new MenuItem(R.mipmap.icon_scan, "扫一扫"));
        menuItems.add(new MenuItem(R.drawable.ic_skinning, "换肤"));
        menuItems.add(new MenuItem(R.mipmap.icon_add, "添加内容"));
        /*menuItems.add(new MenuItem(R.mipmap.icon_robot_manage, "机器人管理"));*/
        menuItems.add(new MenuItem(R.mipmap.icon_voice_people, "变音"));
        menuItems.add(new MenuItem(R.mipmap.icon_mail_robot, "私信消息"));

        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(getActivity(), 280))     //默认高度480
                .setWidth(ScreenUtil.dip2px(getActivity(), 160))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        mTopRightMenu.dismiss();
                        switch (position) {
                            case 0:
                                showShortToast("敬请期待...");
                                startActivity(RobotPairActivity.class);
                                break;
                            case 1:
                                //startActivity(QRCodeActivity.class);
                                gotoShare();
                                break;
                            case 2:
                                isVideoCall = false;
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                                        Utils.showMissingPermissionDialog(getActivity(), "相机");
                                    } else {
                                        TabMineFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                    }
                                } else {
                                    gotoCapture();
                                }
                                break;
                            case 3:
                                startActivity(SkinningActivity.class);
                                break;
                            case 4:
                                startActivity(AddKnowledgeActivity.class);
                                break;
                            /*
                            case 5:
                                Intent intent = new Intent(getActivity(),RobotSettingActivity.class);
                                intent.putExtra("from","MyInfoActivity");
                                startActivity(intent);
                                break;*/
                            case 5:
                                startActivity(VoicePeopleActivity.class);
                                break;
                            case 6:
                                Intent intent1 = new Intent(getActivity(), MailNewActivity.class);
                                intent1.putExtra("from", "MainActivity");
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .showAsDropDown(titleBarView, getActivity().getWindow().getDecorView().getWidth() - ScreenUtil.dip2px(getActivity(), 170), ScreenUtil.dip2px(getActivity(), 6));    //带偏移量

        if (MyApplication.unreadMessageNum > 0) {
            mTopRightMenu.showPoint(5);
        } else {
            mTopRightMenu.hidePoint(5);
        }
    }
    private ShareUtil shareUtil;
    private void gotoShare() {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, URL_ROBOT + MySharedPreferences.getUniqueId());
//        shareIntent.setType("text/plain");
//        startActivity(Intent.createChooser(shareIntent, "share"));
//        UMWeb web = new UMWeb(URL_ROBOT + MySharedPreferences.getUniqueId());
//        web.setTitle(MySharedPreferences.getRobotName());//标题
//        if(!MySharedPreferences.getHeadPortrait().equals("")) {
//            web.setThumb(new UMImage(getContext(),MySharedPreferences.getHeadPortrait()));  //缩略图
//        }
//        web.setDescription(Utils.isEmpty(MySharedPreferences.getProfile()) ? Const.commonProfile : MySharedPreferences.getProfile());  //描述
//        new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE)
//                .withMedia(web)
//                .setCallback(umShareListener)
//                .open();
        String url = URL_ROBOT + MySharedPreferences.getUniqueId();
        String description = Utils.isEmpty(MySharedPreferences.getProfile()) ? Const.commonProfile : MySharedPreferences.getProfile();
        if(shareUtil == null)shareUtil = new ShareUtil(getActivity());
        shareUtil.shareWeb(url, MySharedPreferences.getRobotName(), MySharedPreferences.getHeadPortrait(),
                description,umShareListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case ImageUtil.TAKE_PICTURE:
                    ImageUtil.startPhotoZoom(ImageUtil.tempUri, TabMineFragment.this); // 开始对图片进行裁剪处理
                    break;
                case ImageUtil.CHOOSE_PICTURE:
                    ImageUtil.startPhotoZoom(data.getData(), TabMineFragment.this); // 开始对图片进行裁剪处理
                    break;
                case ImageUtil.CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上并上传图片
                    }
                    break;
                case REQUEST_CODE:
                    handleScanResult(data);
                    break;
                default:
                    UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //处理扫描结果（在界面上显示）
    private void handleScanResult(Intent data) {
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                //showShortToast(result);
                if (result.startsWith(Const.URL_ROBOT)) {
                    gotoChat(result);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(intent);
                }
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                showShortToast("解析失败");
            }
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                showShortToast(t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    private void gotoChat(final String result) {
        String uniqueId = result.replace(Const.URL_ROBOT, "");
        Diting.getRobotByUniqueId(uniqueId, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("from", "ChatLog");
                    intent.putExtra("headPortrait", response.getString("headImg"));
                    intent.putExtra("username", response.getString("username"));
                    intent.putExtra("welcome", response.getString("welcomes"));
                    intent.putExtra("robotName", response.getString("name"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {

            }
        });
    }

    private void gotoCapture() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //修改字体大小
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String message) {
        if (message.equals("fontSizeChange")) {

            adapter.notifyDataSetChanged();
        }
    }
}
