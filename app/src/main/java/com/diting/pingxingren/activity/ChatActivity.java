package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.MessageAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.AudioRecordButton;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.db.DitingDB;
import com.diting.pingxingren.entity.Msg;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.HttpStringCallBack;
//import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.InstallationUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Pinyin4jUtil;
import com.diting.pingxingren.util.PopupWinUtil;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;
import com.diting.voice.data.body.Contact;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.diting.pingxingren.util.Const.URL_ROBOT;
import static com.diting.pingxingren.util.ImageUtil.getLocalOrNetBitmap;

/**
 * Created by zys on 2016/11/21.
 * 聊天主页
 */

public class ChatActivity extends BaseActivity implements View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView msgListView;
    private EditText inputText;
    private MessageAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();
    private Button send;
    private String uuid;
    private TitleBarView titleBarView;
    private SwipeRefreshLayout mSwipeLayout;
    private DitingDB ditingDB;
    private RobotConcern robotConcern;
    private String username;
    private String headPortrait;
    private String welcome;
    private String robotName;
    private String uniqueId;
    private ImageView iv_voice;
    private AudioRecordButton btn_voice;
    private ListView lv_phrase;
    private ArrayAdapter<String> phraseAdapter;
    private boolean isFromContacts;
    private String[] phrases = Const.phraseGroup;
    private boolean isPhrase = true;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_AUDIO_RECORD = 2;
    private String phone;
    private String fromUserName = MySharedPreferences.getUsername();
    private Bitmap photo;
    private Bitmap bitmap;
    private static final String EXTRA = "robot";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_robot_tab);
        ditingDB = MyApplication.ditingDB;
        initViews();
        initEvents();
        initData();
        initMsg();
    }

    public static Intent getCallingIntent(Context context, RobotConcern robotConcern) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA, robotConcern);
        return intent;
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
    }

    private void initTitleBarEvents() {
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            String text = ((TextView) v).getText().toString();
            PopupWinUtil.showPopWindow(text, ChatActivity.this);
            return false;
        }
    };

    @Override
    protected void initViews() {
        initTitleBarView();
        uuid = InstallationUtils.getUuid(this);
        mSwipeLayout =   findViewById(R.id.id_swipe_ly);
        msgListView =   findViewById(R.id.msg_list_view);
        inputText =  findViewById(R.id.input_text);
        send =   findViewById(R.id.send);
        iv_voice =   findViewById(R.id.iv_voice);
        btn_voice =   findViewById(R.id.btn_voice);
        lv_phrase =   findViewById(R.id.lv_phrase);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_phrase.getVisibility() == View.VISIBLE) {
                    lv_phrase.setVisibility(View.GONE);
                }
                if (btn_voice.getVisibility() == View.VISIBLE) {
                    inputText.setVisibility(View.VISIBLE);
                    btn_voice.setVisibility(View.GONE);
                    iv_voice.setImageResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                    Utils.showSoftInput(ChatActivity.this, inputText);
                } else {
                    if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.RECORD_AUDIO)) {
                            Utils.showMissingPermissionDialog(ChatActivity.this, "录音");
                        } else {
                            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
                        }
                    } else {
                        setAudioRecordEnable();
                    }
                }
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


        btn_voice.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath, String text) {
                // TODO Auto-generated method stub
                sendVoice(seconds, filePath, text);
            }

            @Override
            public void onStart() {
                adapter.stopPlayVoice();
                // TODO Auto-generated method stub
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
        phraseAdapter = new ArrayAdapter<String>(this, R.layout.phrase_item, phrases);
        lv_phrase.setAdapter(phraseAdapter);
        lv_phrase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_phrase.setVisibility(View.GONE);
                sendMsg(phrases[position]);
            }
        });
        adapter = new MessageAdapter(this, msgList);
        adapter.setOnLongClickListener(onLongClickListener);
        msgListView.setOnTouchListener(this);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPhrase) {
                    String content = inputText.getText().toString();
                    sendMsg(content);
                } else {
                    togglePhraseGroup();
                }
            }
        });
        adapter.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnnexUtil.showChooseImgDialog("选择头像", ChatActivity.this);
            }
        });
        adapter.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(RobotDetailActivity.getCallingIntent(ChatActivity.this, robotConcern));

            }
        });
    }

    private void setAudioRecordEnable() {
        inputText.setVisibility(View.GONE);
        btn_voice.setVisibility(View.VISIBLE);
        iv_voice.setImageResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
        Utils.hideSoftInput(ChatActivity.this, inputText);
    }

    private void togglePhraseGroup() {
        if (lv_phrase.getVisibility() == View.GONE) {
            lv_phrase.setVisibility(View.VISIBLE);
        } else {
            lv_phrase.setVisibility(View.GONE);
        }
    }

    private void sendMsg(String content) {
        if (!"".equals(content)) {
            Msg msg = new Msg();
            msg.setContent(content);
            msg.setType(Msg.TYPE_SENT);
            msg.setUserName(username);
            msg.setTime(TimeUtil.getNowTimeMills());
            msg.setHeadPortrait(MySharedPreferences.getHeadPortrait());
            adapter.upDateMsg(msg);
            msgListView.setSelection(ListView.FOCUS_DOWN);
            saveMsg(msg);
            inputText.setText("");
            //getAnswer(content,false);
            getAnswer(content, false);
        }
    }

    private void callPhone(String phone, boolean isFromContact) {
        adapter.stopMusic();
        if (isFromContact) {
            call(phone);
        } else {
            addCallMsg("拨打平行人电话");
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
//            startActivity(intent);
        }
    }

    private void requestCallPermission(String phone, boolean isFromContacts) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                Utils.showMissingPermissionDialog(this, "拨号");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            callPhone(phone, isFromContacts);
        }
    }

    private void call(String content) {
        List<Contact> list = Utils.readContacts(this);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (Pinyin4jUtil.converterToSpell(content).contains(Pinyin4jUtil.converterToSpell(list.get(i).getName()))) {
                    addCallMsg("拨打手机通讯录电话");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(i).getPhone()));
//                    startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(phone, isFromContacts);
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启拨号权限");
                Utils.showMissingPermissionDialog(ChatActivity.this, "拨号");

            }
            return;
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_AUDIO_RECORD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAudioRecordEnable();
            } else {
                // Permission Denied
                //showShortToast("请到设置中开启录音权限");
                Utils.showMissingPermissionDialog(ChatActivity.this, "录音");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.getStringExtra("from") != null) {
            username = intent.getStringExtra("username");
            headPortrait = intent.getStringExtra("headPortrait");
            welcome = intent.getStringExtra("welcome");
            robotName = intent.getStringExtra("robotName");
            if (intent.getStringExtra("from").equals("try")) {
                fromUserName = "";
                uniqueId = Const.UNIQEID_XIAODI;
                titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
                titleBarView.setBtnRightText("排行榜");
                titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(RankActivity.class);
                    }
                });
            } else {
                uniqueId = intent.getStringExtra("uniqueId");
                addSubmenu();
            }
        } else {
            robotConcern = intent.getParcelableExtra("robot");
            username = robotConcern.getUsername();
            headPortrait = robotConcern.getHeadPortrait();
            welcome = robotConcern.getWelcome();
            robotName = robotConcern.getRobotName();
            uniqueId = robotConcern.getUniqueId();
            addSubmenu();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Utils.isEmpty(headPortrait)) {
                    bitmap = CodeUtils.createImage(URL_ROBOT + uniqueId, 400, 400, getLocalOrNetBitmap(headPortrait));
                } else {
                    bitmap = CodeUtils.createImage(URL_ROBOT + uniqueId, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_left));
                }
            }
        }).start();
        titleBarView.setTitleText("与" + robotName + "沟通");
    }

    private ShareUtil shareUtil;
    private void showTopRightMenu() {
        final TopRightMenu mTopRightMenu = new TopRightMenu(this);

//添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.icon_share_url, "分享链接"));
        //menuItems.add(new MenuItem(R.mipmap.icon_add,"加关注"));
        menuItems.add(new MenuItem(R.mipmap.icon_qr_code, "分享二维码"));

        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(this, 100))     //默认高度480
                .setWidth(ScreenUtil.dip2px(this, 160))      //默认宽度wrap_content
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
                                String url = URL_ROBOT + uniqueId;//分享的连接
                                String description = Utils.isEmpty(welcome) ? Const.commonProfile : welcome;
                                if(shareUtil == null)shareUtil = new ShareUtil(ChatActivity.this);
                                shareUtil.shareWeb(url, robotName, headPortrait, description,
                                        umShareListener);
                                break;
//                            case 1:
//                                addConcern(robotConcern);
//                                break;
                            case 1:
                                if(shareUtil == null)shareUtil = new ShareUtil(ChatActivity.this);
                                shareUtil.sharePicture(bitmap);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .showAsDropDown(titleBarView, getWindow().getDecorView().getWidth() - ScreenUtil.dip2px(this, 170), ScreenUtil.dip2px(this, 6));    //带偏移量

    }

    private void addConcern(final RobotConcern robotConcern) {
        showLoadingDialog("请求中");
        Diting.addConcern(robotConcern.getPhone(), new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
                try {
                    int flag = response.getInt("flg");
                    switch (flag) {
                        case 0:
                            showShortToast("关注成功");
                            robotConcern.setConcerned(true);
                            adapter.notifyDataSetChanged();
                            break;
                        case 1:
                            showShortToast("只能关注20个机器人");
                            break;
                        case 2:
                            showShortToast("已关注");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("关注失败");
            }
        });
    }

    private void addSubmenu() {
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnRight(R.mipmap.icon_more, null);
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopRightMenu();
            }
        });
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

    private void initMsg() {
        msgList = ditingDB.listOtherMessage(0, username, fromUserName);
        if (!msgList.isEmpty()) {
            Collections.reverse(msgList);
            adapter.setMessageList(msgList);
            msgListView.setSelection(msgList.size() - 1);
        }
        Msg msg = new Msg();
        msg.setType(Msg.TYPE_RECEIVED_TEXT);
        msg.setHeadPortrait(headPortrait);
        msg.setUserName(username);
        msg.setTime(TimeUtil.getNowTimeMills());
        if (Utils.isEmpty(welcome)) {
            msg.setContent("我很懒！没有写动态啦！");
        } else {
            msg.setContent(welcome);
        }
        adapter.upDateMsg(msg);
    }

    private void getAnswer(String content, final boolean isVoice) {
//        if(content.startsWith("打电话给")&&MySharedPreferences.getContactsPermission()){
//            isFromContacts = true;
//            requestCallPermission(content,true);
//        }
        Diting.getRemoteAnswer(username, uuid, content, isVoice, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String answer = response.getString("answer");
                    String img_url = response.getString("img_url");
                    //变身
                    if (!Utils.isEmpty(response.getString("skip_url"))) {
                        transForm(response);
                    }
                    Msg msg = new Msg();
                    if (!Utils.isEmpty(answer) && !Utils.isEmpty(img_url)) {
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
                    if (Utils.isMobile(answer) || answer.equals("4009003381")) {
                        phone = answer;
                        requestCallPermission(phone, false);
                    } else {
                        msg.setTime(TimeUtil.getNowTimeMills());
                        msg.setUserName(username);
                        msg.setHeadPortrait(headPortrait);
                        adapter.upDateMsg(msg);
                        if (isVoice && msg.getType() == Msg.TYPE_RECEIVED_VOICE) {
                            adapter.speak(Html.fromHtml(msg.getContent()).toString(), null);
                        }
                        msgListView.setSelection(ListView.FOCUS_DOWN);
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                msgListView.setSelection(ListView.FOCUS_DOWN);
                            }
                        }, 100);
                        saveMsg(msg);
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
            robotConcern.setConcerned(false);
            robotConcern.setFansCount(0);
            robotConcern.setPhone("160");
            robotConcern.setRobotValue(50.0);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("robot", robotConcern);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Utils.hideSoftInput(ChatActivity.this, msgListView);
        lv_phrase.setVisibility(View.GONE);
        adapter.stopPlayVoice();
        return false;
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
        int position = msgList.size();
        List<Msg> list = ditingDB.listOtherMessage(position, username, fromUserName);
        if (list != null && list.size() != 0) {
            Collections.reverse(msgList);
            msgList.addAll(list);
            Collections.reverse(msgList);
            adapter.setMessageList(msgList);
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

    private void sendVoice(final float seconds, final String filePath, String text) {
        Msg msg = new Msg();
        msg.setUserName(username);
        msg.setHeadPortrait(MySharedPreferences.getHeadPortrait());
        msg.setType(Msg.TYPE_SEND_VOICE);
        msg.setVoicePath(filePath);
        msg.setContent(text);
        msg.setTime(TimeUtil.getNowTimeMills());
        msg.setVoiceTime(seconds);
        adapter.upDateMsg(msg);
        msgListView.setSelection(ListView.FOCUS_DOWN);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(ListView.FOCUS_DOWN);
            }
        }, 100);
        saveMsg(msg);
        getAnswer(text, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, ChatActivity.this); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CHOOSE_PICTURE:
                    AnnexUtil.startPhotoZoom(data.getData(), ChatActivity.this); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上并上传图片
                    }
                    break;
                default:
                    UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            }
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
            if (photo != null) {
                showLoadingDialog("请求中");
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photo, null, null));
                File file = new File(Utils.getRealFilePath(ChatActivity.this, uri));
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

}
