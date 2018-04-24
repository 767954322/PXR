package com.diting.pingxingren.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.activity.HomeActivity;
import com.diting.pingxingren.activity.InvalidQuestionActivity;
import com.diting.pingxingren.activity.MailNewActivity;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.activity.RankActivity;
import com.diting.pingxingren.activity.RobotDetailActivity;
import com.diting.pingxingren.activity.RobotPairActivity;
import com.diting.pingxingren.activity.SkinningActivity;
import com.diting.pingxingren.activity.TreasureChestActivity;
import com.diting.pingxingren.activity.VideoCallActivity;
import com.diting.pingxingren.activity.VoiceCallActivity;
import com.diting.pingxingren.activity.VoicePeopleActivity;
import com.diting.pingxingren.adapter.ChattingAdapter;
import com.diting.pingxingren.adapter.CommonLanguageAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BasePageFragment;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.databinding.FragmentChattingBinding;
import com.diting.pingxingren.dialog.CommonLanguageDialog;
import com.diting.pingxingren.easypermissions.AppSettingsDialog;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.entity.ChatMessageItem;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.ChatBundleModel;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CommonLanguageListObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.presenter.ChattingPresenterImpl;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.CalanderUtil;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.DialogManager;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.PopupWinUtil;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.TextLengthUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.view.ChattingView;
import com.diting.voice.data.model.CallInfo;
import com.diting.voice.utils.CallManagerUtils;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.diting.pingxingren.util.Const.URL_ROBOT;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 09:00.
 * Description: .正聊页面
 */

public class TabChattingFragment extends BasePageFragment implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks, ChattingView, View.OnFocusChangeListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener, ClickListener {

    public static TabChattingFragment createChatFragment() {
        return new TabChattingFragment();
    }

    public static TabChattingFragment createChatFragment(Bundle bundle) {
        TabChattingFragment tabChattingFragment = new TabChattingFragment();
        tabChattingFragment.setArguments(bundle);
        return tabChattingFragment;
    }

    private HomeActivity mHomeActivity;
    private FragmentChattingBinding mBinding;
    private ChattingPresenterImpl mPresenter;
    private RelativeLayout mTabBar;
    private TitleBarView mTitleBarView;
    private LinearLayoutManager mLinearLayoutManager;

    private ChattingAdapter mAdapter;
    private TabChattingFragmentHandler mHandler;
    private ProgressDialog mProgressDialog;
    private CommonLanguageDialog mLanguageDialog;

    private boolean isSend = false;
    private boolean isRobot = false;
    private boolean isRefresh = false;
    private String mUserName;
    private String mRobotName;
    private String mUniqueId;
    private String mWelcome;
    private String mRobotHeadPortrait;
    private Bitmap mRobotHeadBitmap;
    private String mRemindContent;
    private long mRemindTime;
    private ChatMessageItem mChatMessageItem;
    private String mFileUrl;
    private String mFilePath;
    private boolean isOtherChat = false;
    private boolean mExperience = false;
    private CommunicateModel mCommunicateModel;
    private RobotInfoModel mChatBundle;
    private ChatBundleModel chatBundleModel;
    private String mPhone;
    private String mUploadImgUrl;
    private CommonLanguageListModel mDeleteLanguageModel;
    private String answer1;
    private String answer2;
    private String answer3;
    private boolean isScan = false;
    private boolean idAddFollow = false;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_chatting;
    }

    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        mBinding = (FragmentChattingBinding) mViewBinding;

        if (mBundle != null) {
            isOtherChat = mBundle.getBoolean("isOtherChat", false);
            mCommunicateModel = mBundle.getParcelable("robot");
            mChatBundle = mBundle.getParcelable("data");
            chatBundleModel = mBundle.getParcelable("user");
        }

        if (isOtherChat) {
            if (mCommunicateModel != null) {
                mUserName = mCommunicateModel.getNamePinyin();
                mRobotHeadPortrait = mCommunicateModel.getRobotHeadImg();
                mWelcome = mCommunicateModel.getProfile();
                mRobotName = mCommunicateModel.getName();
                mUniqueId = mCommunicateModel.getUniqueId();

            } else if (mChatBundle != null) {
                String from = mChatBundle.getmFrom();
                mExperience = !Utils.isEmpty(from) && from.equals("try");
                mUserName = mChatBundle.getUsername();
                mRobotHeadPortrait = mChatBundle.getRobotHeadImg();
                mWelcome = mChatBundle.getProfile();
                mRobotName = mChatBundle.getName();
                answer1 = mChatBundle.getInvalidAnswer1();
                answer2 = mChatBundle.getInvalidAnswer2();
                answer3 = mChatBundle.getInvalidAnswer3();

                if (mExperience)
                    mUniqueId = Const.UNIQEID_XIAODI;
                else
                    mUniqueId = mChatBundle.getUniqueId();
            } else if (chatBundleModel != null) {
                mUserName = chatBundleModel.getmUserName();
                mRobotHeadPortrait = chatBundleModel.getHeadImg();
                mWelcome = chatBundleModel.getWelcomes();
                mRobotName = chatBundleModel.getName();
                mUniqueId = chatBundleModel.getUniqueId();
            }
            isScan = false;
            if (!isScan) {
//                ApiManager.getRobotInfoByUniqueId(mUniqueId, new RobotInfoObserver(mResultCallBack));
            }

        } else {
            mUserName = MySharedPreferences.getUsername();
            mRobotName = MySharedPreferences.getRobotName();
            answer1 = MySharedPreferences.getUniversalAnswer1();
            answer2 = MySharedPreferences.getUniversalAnswer2();
            answer3 = MySharedPreferences.getUniversalAnswer3();
            mUniqueId = MySharedPreferences.getUniqueId();
            mBinding.setListener(this);
            mTabBar = mActivity.findViewById(R.id.rl_bottom);
        }

        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mLinearLayoutManager.setStackFromEnd(false);
        mBinding.chatRecycler.setLayoutManager(mLinearLayoutManager);
        initTitleBarView(mBinding.titleBar);
    }

    private void initTitleBarView(View view) {
        mTitleBarView = view.findViewById(R.id.titleBar);
        mTitleBarView.setCommonTitle(View.VISIBLE,
                View.VISIBLE, View.GONE, View.VISIBLE);

        mTitleBarView.setTitleText(mRobotName);
        if (!mExperience) {
            mTitleBarView.setBtnRight(R.mipmap.icon_more, null);

            if (!isOtherChat)
                mTitleBarView.setBtnLeft(R.drawable.ic_treasure_chest, null, 30);
            else
                mTitleBarView.setBtnLeft(R.drawable.ic_back, null);
        } else {
            mTitleBarView.setBtnLeft(R.drawable.ic_back, null);
            mTitleBarView.setBtnRightText("排行榜");
        }
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOtherChat) {
                    startToActivity(TreasureChestActivity.class);
                    MobclickAgent.onEvent(mActivity, UmengUtil.EVENT_CLICK_NUM_OF_CHATTING_BOX);
                } else {
                    mActivity.finish();
                }
            }
        });

        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOtherChat) {
                    showTopRightMenu();
                    MobclickAgent.onEvent(mActivity, UmengUtil.EVENT_CLICK_NUM_OF_CHATTING_PLUS);
                } else {
                    if (mExperience)
                        startToActivity(RankActivity.class);
                    else
                        showOtherTopRightMenu();
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        if (mActivity instanceof HomeActivity)
            mHomeActivity = (HomeActivity) mActivity;
        mDialogManager = new DialogManager(mActivity);
        mHandler = new TabChattingFragmentHandler(this);
        List<ChatMessageItem> msg = new ArrayList<>();
        mAdapter = new ChattingAdapter(mActivity, msg);
        // mAdapter.openLoadAnimation();
        setPresenter(new ChattingPresenterImpl(mActivity, this,
                mDTingDBUtil, mUserName, mRobotName, mUuid, isOtherChat, mRobotHeadPortrait, answer1, answer2, answer3, mUniqueId, mAdapter));
        if (Utils.hasLocationPermission(mActivity)) {
            mPresenter.getLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    Constants.REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }

        mAdapter.setMine(!isOtherChat);
//        mAdapter.setOnLongClickListener(mOnLongClickListener);
        mAdapter.setAddCollectionListener(addCollectionListener);
        mAdapter.setVoiceIsRead(mVoiceIsRead);
        mAdapter.setDownloadFile(mDownloadFile);
        mAdapter.setLeftClickListener(mLeftOnClickListener);
        mAdapter.setRightClickListener(mRightOnClickListener);
        mBinding.chatRecycler.setAdapter(mAdapter);

        mPresenter.start();
        if (!isOtherChat) {
            if (MyApplication.unreadMessageNum > 0) {
//                mPresenter.getPrivateMsgCount();
            }

        } else {
            mPresenter.sendProfileMsg(mWelcome, mRobotHeadPortrait);
        }

        // mHandler.post(mGetHeadBitmapRunnable);
        new GetHeadTask().execute(mRobotHeadPortrait);
    }

    @Override
    public void initListener() {
        super.initListener();
        mBinding.etQuestion.setFilters(new InputFilter[]{
                new TextLengthUtil(this.getActivity(), 101, getString(R.string.tip_question))});
        mBinding.etQuestion.setOnFocusChangeListener(this);
        mBinding.etQuestion.addTextChangedListener(mQuestionWatcher);
        mBinding.ivVoice.setOnClickListener(this);
        mBinding.btVoice.setOnLongClickListener(mVoiceButtonOnLongClickListener);
        mBinding.swipeLayout.setOnRefreshListener(this);
        mBinding.btSend.setOnClickListener(this);
        mBinding.chatRecycler.setOnTouchListener(this);
        mBinding.btVoice.setOnTouchListener(mVoiceButtonTouchListener);
        mBinding.ivAdGone.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int skinRes = MySharedPreferences.getSkinRes();
        if (skinRes == -1)
            mBinding.chatLayout.setBackgroundResource(R.drawable.icon_skin_default);
        else if (skinRes == R.drawable.ic_skin_one
                || skinRes == R.drawable.ic_skin_two
                || skinRes == R.drawable.ic_skin_three
                || skinRes == R.drawable.ic_skin_four
                || skinRes == R.drawable.ic_skin_five
                || skinRes == R.drawable.ic_skin_six)
            mBinding.chatLayout.setBackgroundResource(skinRes);
        else
            mBinding.chatLayout.setBackgroundResource(R.drawable.icon_skin_default);

        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.pause();
        MediaManager.release();
        mPresenter.destroy();
        mAdapter.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.ivVoice:
                if (mBinding.btVoice.getVisibility() == View.VISIBLE) {
                    mBinding.etQuestion.setVisibility(View.VISIBLE);
                    mBinding.btVoice.setVisibility(View.GONE);
                    mBinding.ivVoice.setImageResource(R.mipmap.voice_btn_normal);
                } else {
                    setAudioRecordEnable();
                }
                MobclickAgent.onEvent(mActivity, UmengUtil.EVENT_CLICK_NUM_CHATTING_VOICE);

                break;
            case R.id.btSend:
                if (isSend) {
                    String question = mBinding.etQuestion.getText().toString().trim();
                    mPresenter.sendTextMessage(question);
                    mBinding.etQuestion.setText("");
                } else {

                    if (!isOtherChat) {
                        if (mLanguageDialog == null) {
                            mLanguageDialog = new CommonLanguageDialog(mActivity);
                            mLanguageDialog.setAdapterData(null);
                            mLanguageDialog.setListener(mItemClickListener);
                        }

//                        if (MyApplication.sCommonLanguages.size() > 0) {
                        mLanguageDialog.updateData();
//                        } else {
//                            ToastUtils.showShortToastSafe("你还没添加常用语, 请先去知识库添加!");
//                        }
                    } else {

                        if (mLanguageDialog == null) {
                            mLanguageDialog = new CommonLanguageDialog(mActivity);
                            mLanguageDialog.setAdapterData(null);
                            mLanguageDialog.setAddViewisShow();

                        }
                        mLanguageDialog.setListener(mItemClickListener);
//                        ApiManager.getCommonLanguage(mRobotName, "0", new CommonLanguagesObserver(mResultCallBack));
                        ApiManager.getCommonLanguageList(mUniqueId, new CommonLanguageListObserver(mResultCallBack));
                    }
                    MobclickAgent.onEvent(mActivity, UmengUtil.EVENT_CLICK_NUM_CHATTING_COMMON_LANGUAGE);

                }
                break;
            case R.id.ivAdGone:
                mBinding.relAd.setVisibility(View.GONE);
                break;
        }
    }

    private void showTopRightMenu() {
        final TopRightMenu mTopRightMenu = new TopRightMenu(getActivity());
        // 添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        // menuItems.add(new MenuItem(R.mipmap.icon_qr_code, "分享二维码"));
        menuItems.add(new MenuItem(R.mipmap.icon_pair, "速配"));
        menuItems.add(new MenuItem(R.mipmap.icon_share_url, "分享链接"));
        menuItems.add(new MenuItem(R.mipmap.icon_scan, "扫一扫"));
        menuItems.add(new MenuItem(R.drawable.ic_skinning, "换肤"));
        menuItems.add(new MenuItem(R.mipmap.icon_add, "添加内容"));
        /*menuItems.add(new MenuItem(R.mipmap.icon_robot_manage, "机器人管理"));*/
        menuItems.add(new MenuItem(R.mipmap.icon_voice_people, "变音"));
        menuItems.add(new MenuItem(R.mipmap.icon_mail_robot, "私信消息"));
        menuItems.add(new MenuItem(R.mipmap.icon_unknown_knowledge, "未知问题"));


        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(getActivity(), 360))     //默认高度480
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
                                MobclickAgent.onEvent(mActivity, UmengUtil.EVENT_CLICK_NUM_OF_PLUS_ROBOT_PAIR);
//                                showShortToast("敬请期待...");
                                startToActivity(RobotPairActivity.class);
                               break;
                            case 1:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_SHARE_LINK);
                                String url = URL_ROBOT + MySharedPreferences.getUniqueId();
//                                String description = Utils.isEmpty(MySharedPreferences.getProfile()) ? Const.commonProfile : MySharedPreferences.getProfile();
                                String description = "我正在平行人APP上玩人工智能，快来跟我一起玩耍吧！";
                                if (shareUtil == null) shareUtil = new ShareUtil(getActivity());
                                shareUtil.shareWeb(url, MySharedPreferences.getRobotName(), MySharedPreferences.getHeadPortrait(), description, umShareListener);
                                break;
                            case 2:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_SCAN);
                                if (Utils.hasCameraPermission(mActivity)) {
                                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                                    startActivityForResult(intent, 5);
                                } else {
                                    EasyPermissions.requestPermissions(TabChattingFragment.this, getString(R.string.rationale_camera),
                                            Constants.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                                }
                                break;
                            case 3:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_SKIN_PEELER);
                                startToActivity(SkinningActivity.class);
                                break;
                            case 4:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_ADD_KNOWLEDGE);
                                startToActivity(AddKnowledgeActivity.class);
                                break;
                            case 5:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_ADD_VOICE_PEOPLE);
                                startToActivity(VoicePeopleActivity.class);
                                break;
                            case 6:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_PERSONAL_MESSAGE);
                                Intent intent1 = new Intent(getActivity(), MailNewActivity.class);
                                intent1.putExtra("from", "MainActivity");
                                startToActivity(intent1);
                                break;
                            case 7:
                                MobclickAgent.onEvent(mActivity,UmengUtil.EVENT_CLICK_NUM_OF_PLUS_INVALID_QUESTION);
                                Intent intent2 = new Intent(getActivity(), InvalidQuestionActivity.class);
                                startToActivity(intent2);
                                break;
                        }
                    }
                }).showAsDropDown(mBinding.titleBar,
                getActivity().getWindow().getDecorView().getWidth()
                        - ScreenUtil.dip2px(getActivity(), 170), ScreenUtil.dip2px(getActivity(), 6));    //带偏移量
    }

    private ShareUtil shareUtil;

    private void showOtherTopRightMenu() {
        final TopRightMenu mTopRightMenu = new TopRightMenu(getActivity());
        // 添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.icon_share_url, "分享链接"));
        menuItems.add(new MenuItem(R.mipmap.icon_qr_code, "分享二维码"));

        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(getActivity(), 100))     //默认高度480
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
                                String url = URL_ROBOT + mUniqueId;//分享的连接
//                                String description = Utils.isEmpty(mWelcome) ? Const.commonProfile : mWelcome;
                                String description = "我正在平行人APP上玩人工智能，快来跟我一起玩耍吧！";
                                if (shareUtil == null) shareUtil = new ShareUtil(getActivity());
                                shareUtil.shareWeb(url, mRobotName, mRobotHeadPortrait, description,
                                        umShareListener);
                                break;
                            case 1:
                                if (mRobotHeadBitmap != null) {
                                    if (shareUtil == null) shareUtil = new ShareUtil(getActivity());
                                    shareUtil.sharePicture(mRobotHeadBitmap);
                                }
                                break;
                        }
                    }
                }).showAsDropDown(mBinding.titleBar,
                mActivity.getWindow().getDecorView().getWidth()
                        - ScreenUtil.dip2px(getActivity(), 170), ScreenUtil.dip2px(getActivity(), 6));    //带偏移量
    }

    @Override
    public void onClick(Object o) {
    }

    private void setAudioRecordEnable() {
        Utils.hideSoftInput(getActivity(), mBinding.etQuestion);
        mBinding.etQuestion.setVisibility(View.GONE);
        mBinding.btVoice.setVisibility(View.VISIBLE);
        mBinding.ivVoice.setImageResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
        if (!isOtherChat) {
            mTabBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, getActivity()); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CHOOSE_PICTURE:
                    AnnexUtil.startPhotoZoom(data.getData(), getActivity()); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CROP_SMALL_PICTURE:
                    if (data != null) {
                        Message message = new Message();
                        message.what = MSG_UPDATE_HEAD_IMAGE;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                    break;
                case 5:
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }

                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            if (result.startsWith(Const.URL_ROBOT)) {
                                String uniqueId = result.replace(Const.URL_ROBOT, "");
                                isScan = true;
                                ApiManager.getRobotInfoByUniqueId(uniqueId, new RobotInfoObserver(mResultCallBack));
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                                startActivity(intent);
                            }
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            ToastUtils.showShortToastSafe("解析失败");
                        }
                    }
                    break;
                default:
                    UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_LOCATION:
                mPresenter.getLocation();
                break;
            case Constants.REQUEST_CODE_CALENDAR_ADD:
                CalanderUtil.addCalendarEvent(getContext(), mRemindContent, mRemindContent, mRemindTime);
                mPresenter.saveChatMsg(mChatMessageItem, false);
                break;
            case Constants.REQUEST_CODE_CALENDAR_DELETE:
                CalanderUtil.deleteCalendarEvent(getContext(), mRemindTime);
                mPresenter.saveChatMsg(mChatMessageItem, false);
                break;
            case Constants.REQUEST_CODE_CAMERA:
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, 3);
                break;
            case Constants.REQUEST_CODE_STORAGE:
                showNoticeDialog();
                break;
            case Constants.REQUEST_CODE_CALL_AUDIO:
                call(mPhone, true);
                break;
            case Constants.REQUEST_CODE_CALL_VIDEO:
                call(mPhone, false);
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private View.OnLongClickListener mVoiceButtonOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            mTime = 0;
            mReady = true;
            mPresenter.startRecording(TimeUtil.getNowTimeMills());
            return false;
        }
    };

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof UploadImageModel) {
                UploadImageModel uploadImageModel = (UploadImageModel) result;
                mUploadImgUrl = uploadImageModel.getUrl() + "?imageMogr2";

                if (isRobotHeader) {


                    ApiManager.saveRobotInfo(mUploadImgUrl, MySharedPreferences.getCompanyName(), MySharedPreferences.getRobotName(),
                            MySharedPreferences.getProfile(), MySharedPreferences.getEnable(), MySharedPreferences.getUserSex(),
                            MySharedPreferences.getIndustry(), MySharedPreferences.getHangYeLevel(), MySharedPreferences.getPrice(),
                            MySharedPreferences.getShengri(),
                            MySharedPreferences.getUniversalAnswer1(), MySharedPreferences.getUniversalAnswer2(),
                            MySharedPreferences.getUniversalAnswer3(), new ResultMessageObserver(this));

                } else {
//                    MySharedPreferences.saveHeadPortrait(mUploadImgUrl);
                    ApiManager.saveCompanyInfo(MySharedPreferences.getCompanyName(), mUploadImgUrl, new ResultMessageObserver(this));

                }

                mAdapter.notifyDataSetChanged();
            } else if (result instanceof String) {
                String msg = (String) result;
                if (msg.contains("企业信息保存成功")) {
                    MySharedPreferences.saveHeadPortrait(mUploadImgUrl);
                    EventBus.getDefault().post("updateHeadImage");
                    mAdapter.notifyDataSetChanged();
                } else if (msg.contains("机器人信息修改成功")) {
                    MySharedPreferences.saveRobotHeadPortrait(mUploadImgUrl);
                    mAdapter.notifyDataSetChanged();
                }
            } else if (result instanceof RobotInfoModel) {
                if (isScan) {
                    RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                    robotInfoModel.setmFrom("chatting");
//                    ChatBundle chatBundle = new ChatBundle();
//                    chatBundle.setFrom("chatting");
//                    chatBundle.setUserName(robotInfoModel.getUsername());
//                    chatBundle.setHeadPortrait(robotInfoModel.getHeadImg());
//                    chatBundle.setRobotName(robotInfoModel.getName());
//                    chatBundle.setWelcome(robotInfoModel.getWelcomes());
//                    chatBundle.setAnswer1(robotInfoModel.getInvalidAnswer1());
//                    chatBundle.setAnswer2(robotInfoModel.getInvalidAnswer2());
//                    chatBundle.setAnswer3(robotInfoModel.getInvalidAnswer3());
//                    chatBundle.setUniqueId(robotInfoModel.getUniqueId());
                    startActivity(NewChatActivity.callingIntent(
                            mActivity, robotInfoModel));
                } else {
                    RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                    idAddFollow = robotInfoModel.isAttentionState();
                    answer1 = robotInfoModel.getInvalidAnswer1();
                    answer2 = robotInfoModel.getInvalidAnswer2();
                    answer3 = robotInfoModel.getInvalidAnswer3();
                    mUniqueId = robotInfoModel.getUniqueId();
                    mRobotHeadPortrait = robotInfoModel.getRobotHeadImg();
                    if (mPresenter != null) {
                        mPresenter.setmAnswer1(answer1);
                        mPresenter.setmAnswer2(answer2);
                        mPresenter.setmAnswer3(answer3);
                        mPresenter.setmUniqueId(mUniqueId);


                    }
                }
            } else if (result instanceof CommonLanguageListModel) {
                ToastUtils.showShortToastSafe("删除成功!");
                MyApplication.sCommonLanguages.remove(mDeleteLanguageModel);
                mLanguageDialog.updateData();
                if (MyApplication.sCommonLanguages.size() == 0) {
                    mLanguageDialog.dismissDialog();
                }

                EventBus.getDefault().post("refreshLanguages");
            } else if (result instanceof JSONObject) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String message = jsonObject.getString("message");
                    if (message.equals("取消成功！") || message.equals("推荐成功！")) {

                        ToastUtils.showShortToastSafe("删除成功!");
                        MyApplication.sCommonLanguages.remove(mDeleteLanguageModel);
                        mLanguageDialog.updateData();
                        if (MyApplication.sCommonLanguages.size() == 0) {
                            mLanguageDialog.dismissDialog();
                        }

                        EventBus.getDefault().post("refreshLanguages");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onResult(List<?> resultList) {
//            if (resultList.size() > 0) {
//                Class<?> aClass = resultList.get(0).getClass();
//                if (aClass.getName().equals(CommonLanguageModel.class.getName())) {
//                    if (mLanguageDialog == null) {
//                        mLanguageDialog = new CommonLanguageDialog(mActivity);
//                        mLanguageDialog.setAdapterData((List<CommonLanguageModel>) resultList);
//                        mLanguageDialog.setListener(mItemClickListener);
//                        mLanguageDialog.showDialog();
//                    } else {
//                        mLanguageDialog.updateData((List<CommonLanguageModel>) resultList);
//                    }
//                }
//            } else {
//                ToastUtils.showShortToastSafe("此机器人暂时没有常用语.");
//            }

            if (resultList.size() > 0) {
                Class<?> aClass = resultList.get(0).getClass();
                if (aClass.getName().equals(CommonLanguageListModel.class.getName())) {
                    if (mLanguageDialog == null) {
                        mLanguageDialog = new CommonLanguageDialog(mActivity);
                        mLanguageDialog.setAdapterData((List<CommonLanguageListModel>) resultList);
                        mLanguageDialog.setListener(mItemClickListener);
                        mLanguageDialog.showDialog();
                    } else {
                        mLanguageDialog.updateData((List<CommonLanguageListModel>) resultList);
                    }
                }
            } else {
                ToastUtils.showShortToastSafe("此机器人没有设置常用语,请鼓励它设置常用语吧");
            }


        }

        @Override
        public void onError(Object o) {
            if (o instanceof String)
                ToastUtils.showShortToastSafe((String) o);
        }
    };

    private CommonLanguageAdapter.ItemClickListener mItemClickListener = new CommonLanguageAdapter.ItemClickListener() {
        @Override
        public void delete(final CommonLanguageListModel commonLanguage) {
            showDeleteLanguageDialog(commonLanguage);
        }

        @Override
        public void itemClick(CommonLanguageListModel commonLanguage) {
            mLanguageDialog.dismissDialog();
            mPresenter.sendTextMessage(commonLanguage.getQuestion());
        }

        @Override
        public void add() {

            if (isOtherChat) {
                mLanguageDialog.dismissDialog();

            } else {

                mLanguageDialog.dismissDialog();
                mHomeActivity.toKnowledge();
            }

        }
    };

    private void showDeleteLanguageDialog(CommonLanguageListModel commonLanguage) {
        mDeleteLanguageModel = commonLanguage;
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("温馨提示");
        myDialog.setMessage("确定要删除所选项目吗？");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
//                ApiManager.saveOrDeleteCommonLanguage(false, mDeleteLanguageModel.getQues(), "",
//                        Integer.valueOf(mDeleteLanguageModel.getQues_id()),
//                        new CommonLanguageObserver(mResultCallBack));
                ApiManager.addOrDeleteCommonLanguage(mDeleteLanguageModel.getId(), false, false, mResultCallBack);
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

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

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    // mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > mBinding.btVoice.getWidth()) {// 判断是否在左边，右边，上边，下边
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > mBinding.btVoice.getHeight() + DISTANCE_Y_CANCEL) {
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
                    mBinding.btVoice.setBackgroundResource(R.drawable.button_recordnormal);
                    mBinding.btVoice.setText("按住 说话");
                    break;
                case STATE_RECORDING:
                    mBinding.btVoice.setBackgroundResource(R.drawable.button_recording);
                    mBinding.btVoice.setText("松开 结束");
                    if (isRecording) {
                        mDialogManager.recording(R.string.shouzhishanghua);
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    mBinding.btVoice.setBackgroundResource(R.drawable.button_recording);
                    mBinding.btVoice.setText("松开手指，取消录音");
                    mDialogManager.wantToCancel();
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

    private View.OnTouchListener mVoiceButtonTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
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
                        return false;
                    }
                    // 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
                    if (!isRecording || mTime < 0.6f) {
                        mDialogManager.tooShort();
                        mPresenter.cancelRecording();
                        mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);// 持续1.3s
                    } else if (mCurrentState == STATE_RECORDING) {//正常录制结束
                        mHandler.sendEmptyMessage(MSG_RECORDING_FINISH);
                    } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                        mPresenter.cancelRecording();
                        mHandler.sendEmptyMessage(MSG_DIALOG_DISMISS);
                    }
                    reset();// 恢复标志位
                    break;
            }
            return false;
        }
    };

    private TextWatcher mQuestionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                mBinding.btSend.setText(getString(R.string.text_send));
                mBinding.btSend.setBackgroundResource(R.drawable.send_btn_enable);
                mBinding.btSend.setTextColor(Color.WHITE);
                isSend = true;
            } else {
                mBinding.btSend.setText(getString(R.string.text_common_language));
                mBinding.btSend.setBackgroundResource(R.drawable.send_btn_disable);
                mBinding.btSend.setTextColor(Color.parseColor("#969696"));
                isSend = false;
            }
        }
    };

    @Override
    public void setPresenter(ChattingPresenterImpl presenter) {
        mPresenter = presenter;
    }

    public ChattingPresenterImpl getPresenter() {
        return mPresenter;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mBinding.etQuestion) {
            // mLinearLayoutManager.setStackFromEnd(hasFocus);
            if (mTabBar != null) {
                if (hasFocus) {
                    mTabBar.setVisibility(View.GONE);
                } else {
                    mTabBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mBinding.swipeLayout.setRefreshing(false);
        mPresenter.getChatMsg(mAdapter.getData().size(), mUserName);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mBinding.chatRecycler) {
            Utils.hideSoftInput(getActivity(), mBinding.chatRecycler);
            mBinding.etQuestion.clearFocus();
        }
        return false;
    }

    @Override
    public void updateVoiceLevel(int level) {
        mDialogManager.updateVoiceLevel(level);
    }

    @Override
    public void onBeginOfSpeech() {
        mAdapter.stopPlayVoice();
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public void onRecordingFinish(float seconds, String filePath, String text) {
        mPresenter.sendVoiceMessage(seconds, filePath, text);
    }

    @Override
    public void addReminder(ChatMessageItem messageItem, boolean isVoice, String reminderContent, long reminderTime) {
        mChatMessageItem = messageItem;
        mRemindContent = reminderContent;
        mRemindTime = reminderTime;
        if (Utils.hasCalendarPermission(mActivity)) {
            CalanderUtil.addCalendarEvent(getContext(), mRemindContent, mRemindContent, mRemindTime);
            mPresenter.saveChatMsg(mChatMessageItem, isVoice);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_calendar),
                    Constants.REQUEST_CODE_CALENDAR_ADD, Manifest.permission.WRITE_CALENDAR);
        }
    }

    @Override
    public void deleteReminder(ChatMessageItem messageItem, boolean isVoice, long reminderTime) {
        mChatMessageItem = messageItem;
        mRemindTime = reminderTime;

        if (Utils.hasCalendarPermission(mActivity)) {
            CalanderUtil.deleteCalendarEvent(getContext(), mRemindTime);
            mPresenter.saveChatMsg(mChatMessageItem, false);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_calendar),
                    Constants.REQUEST_CODE_CALENDAR_DELETE, Manifest.permission.WRITE_CALENDAR);
        }
    }

    @Override
    public void refreshChatRecyclerView(ChatMessageItem msg) {
        if (mAdapter != null && msg != null) {
            mAdapter.addData(msg);
            mBinding.chatRecycler.scrollToPosition(mAdapter.getData().size() - 1);
            if (mAdapter.getData().size() > 4) {
                mLinearLayoutManager.setStackFromEnd(true);
            } else {
                mLinearLayoutManager.setStackFromEnd(false);
            }
        }
    }

    @Override
    public void refreshChatRecyclerView(List<ChatMessageItem> msg) {
        if (msg != null && msg.size() > 0 && mAdapter != null) {
            Collections.reverse(msg);
            mAdapter.addData(0, msg);
            if (isRefresh) {
                isRefresh = false;
            } else {
                mBinding.chatRecycler.scrollToPosition(mAdapter.getData().size() - 1);
            }

            if (mAdapter.getData().size() > 4) {
                mLinearLayoutManager.setStackFromEnd(true);
            } else {
                mLinearLayoutManager.setStackFromEnd(false);
            }
        }

        if (mBundle != null) {
            String text = mBundle.getString("text");
            if (!Utils.isEmpty(text))
                mPresenter.sendTextMessage(mBundle.getString("text"));
        }
    }

    @Override
    public void refreshChatRecyclerView(int position, List<ChatMessageItem> msg) {
        if (msg != null && msg.size() > 0 && mAdapter != null) {
            Collections.reverse(msg);
            mAdapter.addData(position, msg);
            mBinding.chatRecycler.scrollToPosition(mAdapter.getData().size() - position - 1);
        }
    }

    @Override
    public void onPhoneCall(String phone) {
        mPhone = phone;
    }

    @Override
    public void onVoiceCall(String phone) {
        mPhone = phone;
        call(mPhone, true);
    }

    @Override
    public void onVideoCall(String phone) {
        mPhone = phone;
        if (Utils.hasRecordAudioAndCameraPermission(mActivity)) {
            call(mPhone, false);
        } else {
            EasyPermissions.requestPermissions(TabChattingFragment.this, getString(R.string.rationale_record_audio),
                    Constants.REQUEST_CODE_CALL_VIDEO, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
        }
    }


    private ChattingAdapter.AddCollectionListener addCollectionListener = new ChattingAdapter.AddCollectionListener() {
        @Override
        public void addCollection(ChatMessageItem chatMessageItem) {
            if (isOtherChat) {
                LogUtils.e("---------------" + chatMessageItem.toString());
                LogUtils.e("======" + mRobotName + "======" + mUniqueId);
//                LogUtils.e("---------------");
////                ApiManager.addToCollection();/
//LogUtils.e("chatMessageItem"+chatMessageItem.toString());
                PopupWinUtil.showAddCollPopWindow(chatMessageItem, mRobotName, mUniqueId, TabChattingFragment.this);
                LogUtils.e("---------------");
            } else {
//                String text = ((TextView) v).getText().toString();
                String text = chatMessageItem.getContent();
                PopupWinUtil.showPopWindow(text, TabChattingFragment.this);
            }
        }
    };
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {


        @Override
        public boolean onLongClick(View v) {

            if (isOtherChat) {
                LogUtils.e("---------------");
            } else {
                String text = ((TextView) v).getText().toString();
                PopupWinUtil.showPopWindow(text, TabChattingFragment.this);
            }
            return false;
        }
    };


    //    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
//
//
//        @Override
//        public boolean onLongClick(View v) {
//
//            if (isOtherChat) {
//                LogUtils.e("---------------");
//
//
//            } else {
//                String text = ((TextView) v).getText().toString();
//                PopupWinUtil.showPopWindow(text, TabChattingFragment.this);
//
//            }
//
//
//            return false;
//
//        }
//    };
    private boolean isRobotHeader;
    private View.OnClickListener mLeftOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOtherChat) {
                if (!isScan) {
                    if (mCommunicateModel != null) {

                        mCommunicateModel.setIndustryIds(idAddFollow ? 0 : 1);
                        startToActivity(RobotDetailActivity.getCallingIntent(mActivity, mCommunicateModel));
                    }
                } else {
                    if (mCommunicateModel != null) {
                        startToActivity(RobotDetailActivity.getCallingIntent(mActivity, mCommunicateModel));
                    }
                }
            } else {
                isRobot = true;
                isRobotHeader = true;
                AnnexUtil.showChooseImgDialog("选择头像", TabChattingFragment.this);
            }
        }
    };

    private View.OnClickListener mRightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (!mExperience) {
//
//                isRobot = false;
//                isRobotHeader = false;
////                AnnexUtil.showChooseImgDialog(TabChattingFragment.this);
//            }
        }
    };

    private ChattingAdapter.VoiceIsRead mVoiceIsRead = new ChattingAdapter.VoiceIsRead() {
        @Override
        public void voiceOnClick(int position) {
            for (int i = 0; i < mAdapter.getUnReadPosition().size(); i++) {
                if (mAdapter.getUnReadPosition().get(i).equals(position + "")) {
                    mAdapter.getUnReadPosition().remove(i);
                    break;
                }
            }
        }
    };

    private ChattingAdapter.DownloadFile mDownloadFile = new ChattingAdapter.DownloadFile() {
        @Override
        public void onDownload(String fileUrl) {
            mFileUrl = fileUrl;
            if (Utils.hasStoragePermission(mActivity)) {
                showNoticeDialog();
            } else {
                EasyPermissions.requestPermissions(mActivity,
                        getString(R.string.rationale_photo),
                        Constants.REQUEST_CODE_STORAGE_VIDEO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    };

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
                ToastUtils.showShortToastSafe(t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("确定下载此文件吗?")
                .setOnCancelListener(new DialogInterface.OnCancelListener() { //
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss(); // 取消显示对话框
                    }
                }).setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FileUtil.createOrExistsDir(Constants.FILE_PATH_FILES);
                mFilePath = Constants.FILE_PATH_FILES + "/" + String.valueOf(mFileUrl.hashCode())
                        + "." + Utils.getFileExtensionFromUrl(mFileUrl);
                if (FileUtil.isFileExists(mFilePath)) {
                    ToastUtils.showShortToast("文件已保存至" + mFilePath);
                } else {
                    showDownloadDialog();
                    download();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("正在下载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    private void download() {
        // 下载APK，并且替换安装
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//得到sd卡的状态
            FinalHttp finalhttp = new FinalHttp();
            finalhttp.download(mFileUrl, mFilePath, new AjaxCallBack<File>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    super.onFailure(t, errorNo, strMsg);
                    mProgressDialog.dismiss();
                    ToastUtils.showShortToastSafe("下载失败.");
                }

                /**
                 * count:为总的大小
                 * current:为当前下载的大小
                 */
                @Override
                public void onLoading(long count, long current) {//正在下载
                    super.onLoading(count, current);
                    int progress = (int) (current * 100 / count);
                    mProgressDialog.setProgress(progress);
                }

                /**
                 * file t：表示文件的路径
                 */
                @Override
                public void onSuccess(File t) {
                    super.onSuccess(t);
                    mProgressDialog.dismiss();
                }
            });
        } else {
            ToastUtils.showShortToastSafe("没有sdcard!");
        }
    }

    private void updateHeadImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap headBitmap = extras.getParcelable("data");
        if (headBitmap != null) {
            if (isRobot) {

                File file = FileSaveUtil.saveBitmap(headBitmap);
//                if (file != null) {
//                    MySharedPreferences.saveLeftHeadImage(file.getAbsolutePath());
//                    mAdapter.notifyDataSetChanged();
//                }
                String mAnnexRoPath = file.getAbsolutePath();
                File annexRoFile = FileUtil.getFileByPath(mAnnexRoPath);
                ApiManager.uploadImage(annexRoFile, new UploadImageObserver(mResultCallBack));

            } else {
                File file = FileSaveUtil.saveBitmap(headBitmap);
                String mAnnexPath = file.getAbsolutePath();
                File annexFile = FileUtil.getFileByPath(mAnnexPath);
                ApiManager.uploadImage(annexFile, new UploadImageObserver(mResultCallBack));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("chooseChild")) {
            mRobotName = MySharedPreferences.getRobotName();
            mTitleBarView.setTitleText(mRobotName);
            mAdapter.setNewData(null);
            mPresenter.setRobotName(mRobotName);
            mPresenter.getChatMsg(0, mUserName);
        } else if (message.equals("updateHeadImage")) {
            mRobotName = MySharedPreferences.getRobotName();
            mTitleBarView.setTitleText(mRobotName);
        }else if(message.equals("answer")){
            mPresenter.setmAnswer1(MySharedPreferences.getUniversalAnswer1());
            mPresenter.setmAnswer2(MySharedPreferences.getUniversalAnswer2());
            mPresenter.setmAnswer3(MySharedPreferences.getUniversalAnswer3());
        }
    }

    private void call(String phone, boolean isVoice) {
        CallManagerUtils.getInstance().setChatId(phone);
        CallManagerUtils.getInstance().setInComingCall(false);
        CallManagerUtils.getInstance().setCallType(isVoice ?
                CallManagerUtils.CallType.VOICE : CallManagerUtils.CallType.VIDEO);
        if (isVoice)
            CallManagerUtils.getInstance().setVoiceClass(VoiceCallActivity.class);
        else
            CallManagerUtils.getInstance().setVideoClass(VideoCallActivity.class);
        String head_img = MySharedPreferences.getRobotHeadPortrait();
        if (head_img.equals("")) {
            head_img = "null";
        }
        CallInfo callInfo = new CallInfo(MySharedPreferences.getRobotName(),
                MySharedPreferences.getCompanyName(), TimeUtil.millis2String(
                TimeUtil.getNowTimeMills(), TimeUtil.DEFAULT_PATTERN), head_img);
        if (isVoice)
            startActivity(VoiceCallActivity.getCallingIntent(getContext(), new Gson().toJson(callInfo)));
        else
            startActivity(VideoCallActivity.getCallingIntent(getContext(), new Gson().toJson(callInfo)));
    }

    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_DIALOG_DISMISS = 0X112;
    private static final int MSG_RECORDING_FINISH = 0X113;
    private static final int MSG_UPDATE_HEAD_IMAGE = 0X114;

    private static class TabChattingFragmentHandler extends Handler {
        private WeakReference<TabChattingFragment> mActivityWeakReference;

        public TabChattingFragmentHandler(TabChattingFragment activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TabChattingFragment activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_AUDIO_PREPARED:
                        // 显示应该是在audio end prepare之后回调
                        activity.mDialogManager.showRecordingDialog();
                        activity.isRecording = true;
                        // 需要开启一个线程来变换音量
                        new Thread(activity.mGetVoiceLevelRunnable).start();
                        break;
                    case MSG_VOICE_CHANGE:
                        activity.mDialogManager.updateVoiceLevel((Integer) msg.obj);
                        break;
                    case MSG_DIALOG_DISMISS:
                        activity.mDialogManager.dismissDialog();
                        break;
                    case MSG_RECORDING_FINISH:
                        activity.mPresenter.stopRecording();
                        activity.mDialogManager.dismissDialog();
                        break;
                    case MSG_UPDATE_HEAD_IMAGE:
                        Intent data = (Intent) msg.obj;
                        activity.updateHeadImage(data);
                        break;
                }
            }
        }
    }

    class GetHeadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            if (Utils.isNotEmpty(mUniqueId)) {
                if (!Utils.isEmpty(strings[0])) {
                    bitmap = CodeUtils.createImage(URL_ROBOT + mUniqueId, 400, 400,
                            AnnexUtil.getLocalOrNetBitmap(strings[0]));
                } else {
                    bitmap = CodeUtils.createImage(URL_ROBOT + mUniqueId, 400, 400,
                            BitmapFactory.decodeResource(getResources(), R.mipmap.icon_left));
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mRobotHeadBitmap = bitmap;
        }
    }
}
