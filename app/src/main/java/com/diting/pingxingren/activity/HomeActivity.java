package com.diting.pingxingren.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseChangeFragments;
import com.diting.pingxingren.custom.BadgeView;
import com.diting.pingxingren.easypermissions.AppSettingsDialog;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.fragment.KnowledgeFragment;
import com.diting.pingxingren.fragment.TabChattingFragment;
import com.diting.pingxingren.fragment.TabConcernFragment;
import com.diting.pingxingren.fragment.TabHomeFragment;
import com.diting.pingxingren.fragment.TabSettingFragment;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.UnreadMailModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CommonLanguageListObserver;
import com.diting.pingxingren.net.observers.UnreadMailObserver;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.pingxingren.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 10, 11:11.
 * Description: 首页.
 */

public class HomeActivity extends BaseChangeFragments implements RadioGroup.OnCheckedChangeListener,
        EasyPermissions.PermissionCallbacks {

    private RadioGroup mTabGroup;
    private Button mHomeButton;
    private Fragment mFragment;
    private RadioButton mChatRadioButton;
    private RadioButton mKnowledgeButton;
    private Button mMainButton;
    private TabChattingFragment mTabChattingFragment;
    private TabConcernFragment mTabConcernFragment;
    private KnowledgeFragment mTabKnowledgeFragment;
    private TabSettingFragment mTabSettingFragment;
    private TabHomeFragment mTabHomeFragment;
    private boolean mHomeButtonChecked = true;
    private Drawable mHomeN;
    private Drawable mHomeP;
    private boolean mIsSendText = false;
    private String mSendText;
    private BadgeView mBadgeView;
    private boolean isUpdateLanguage = false;

    @Override
    protected void initView() {
        super.initView();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Utils.hasM()) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + packageName));
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        }

        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mHomeN = ContextCompat.getDrawable(this, R.drawable.ic_home_n);
        mHomeP = ContextCompat.getDrawable(this, R.drawable.ic_home_p);

        mTabGroup =  findViewById(R.id.rg_tab);
        mChatRadioButton =  findViewById(R.id.tab_chatting);
        mKnowledgeButton =   findViewById(R.id.tab_knowledge);
        mChatRadioButton.setChecked(true);
        mHomeButton =  findViewById(R.id.tab_home);

        mMainButton = findViewById(R.id.btn_main);
        changeContentFragment(FRAGMENT_TAG_HOME);
        setHomeButtonDrawables(true);
        initBadgeView(mMainButton);
        ApiManager.getUnreadMailCount(new UnreadMailObserver(mResultCallBack));
        ApiManager.getLuckyCount(mResultCallBack);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTabGroup.setOnCheckedChangeListener(this);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHomeButtonChecked) {
                    changeContentFragment(FRAGMENT_TAG_HOME);
                    MobclickAgent.onEvent(HomeActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_HOME_TAB);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
//        ApiManager.getCommonLanguage(null, "-1", new CommonLanguagesObserver(mResultCallBack));
        ApiManager.getCommonLanguageList(MySharedPreferences.getUniqueId(), new CommonLanguageListObserver(mResultCallBack));
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof UnreadMailModel) {
                UnreadMailModel unreadMailModel = (UnreadMailModel) result;
                MyApplication.unreadLetterNum = unreadMailModel.getUnreadLetterNum();
                MyApplication.unreadMessageNum = unreadMailModel.getUnreadMessageNum();
                if (Utils.hasUnreadMail()) {
                    mBadgeView.show();
                }
            } else if (result instanceof JSONObject) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String shareTimes = jsonObject.getString("shareTimes");//用户分享过几次
                    int sharerStatusNum = jsonObject.getInt("sharerStatusNum");//用户只能分享几次
                    MyApplication.sharerStatusNum = sharerStatusNum;
                    if (!Utils.isEmpty(shareTimes)) {

                        MyApplication.shareTimes = Integer.valueOf(shareTimes);
                    } else {
                        MyApplication.shareTimes = 0;
                    }
                    if (!Utils.hasLuckyCount()) {
                        mBadgeView.show();
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
//                    MyApplication.sCommonLanguages = (List<CommonLanguageModel>) resultList;
//                    if (isUpdateLanguage)
//                        EventBus.getDefault().post("add_success");
//                }
//            }
            if (resultList.size() > 0) {
                Class<?> aClass = resultList.get(0).getClass();
                if (aClass.getName().equals(CommonLanguageListModel.class.getName())) {
                    MyApplication.sCommonLanguages = (List<CommonLanguageListModel>) resultList;
//                    if (isUpdateLanguage)
//                        EventBus.getDefault().post("add_success");
                }
            }
        }

        @Override
        public void onError(Object o) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.hasRecordAudioPermission(this)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_record_audio),
                    Constants.REQUEST_CODE_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_chatting:
                changeContentFragment(FRAGMENT_TAG_CHATTING);
                MobclickAgent.onEvent(HomeActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_CHATTING_TAB);
                break;
            case R.id.tab_communication:
                changeContentFragment(FRAGMENT_TAG_COMMUNICATION);
                MobclickAgent.onEvent(HomeActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_COMMUNICATE_TAB);
                break;
            case R.id.tab_knowledge:
                changeContentFragment(FRAGMENT_TAG_KNOWLEDGE);
                MobclickAgent.onEvent(HomeActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_SMALL_EDITOR_TAB);
                break;
            case R.id.tab_mine:
                changeContentFragment(FRAGMENT_TAG_MINE);
                MobclickAgent.onEvent(HomeActivity.this, UmengUtil.EVENT_CLICK_NUM_OF_MINE_TAB);
                break;
        }
    }

    public void changeContentFragment(String tag) {
        switch (tag) {
            case FRAGMENT_TAG_CHATTING://聊天
                setHomeButtonDrawables(false);
                if (mTabChattingFragment == null) {
                    if (mIsSendText) {
                        Bundle bundle = new Bundle();
                        bundle.putString("text", mSendText);
                        mTabChattingFragment = TabChattingFragment.createChatFragment(bundle);
                    } else {
                        mTabChattingFragment = TabChattingFragment.createChatFragment();
                    }
                }
                mFragment = mTabChattingFragment;
                break;
            case FRAGMENT_TAG_COMMUNICATION://沟通
                setHomeButtonDrawables(false);
                if (mTabConcernFragment == null)
                    mTabConcernFragment = new TabConcernFragment();
                mFragment = mTabConcernFragment;
                break;
            case FRAGMENT_TAG_KNOWLEDGE://小编
                setHomeButtonDrawables(false);
                if (mTabKnowledgeFragment == null)
                    mTabKnowledgeFragment = new KnowledgeFragment();
                mFragment = mTabKnowledgeFragment;
                break;
            case FRAGMENT_TAG_MINE://我的
                setHomeButtonDrawables(false);
                if (mTabSettingFragment == null)
                    mTabSettingFragment = new TabSettingFragment();
                mFragment = mTabSettingFragment;
                break;
            case FRAGMENT_TAG_HOME://首页
                setHomeButtonDrawables(true);
                if (mTabHomeFragment == null)
                    mTabHomeFragment = new TabHomeFragment();
                mFragment = mTabHomeFragment;
                break;
        }

        if (Utils.isEmpty(mCurrentFragmentTag) || !mCurrentFragmentTag.equals(tag))
            changeFragment(tag, R.id.fragment_container, mFragment);
    }

    private void initBadgeView(View view) { //BadgeView的具体使用
        mBadgeView = new BadgeView(this, view);// 创建一个BadgeView对象，view为你需要显示提醒的控件// 需要显示的提醒类容
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        mBadgeView.setBackgroundResource(R.drawable.point);
        mBadgeView.setTextSize(12); // 文本大小
        mBadgeView.setBadgeMargin(40, 20); // 水平和竖直方向的间距


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaManager.pause();
        MediaManager.release();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setHomeButtonDrawables(boolean isHome) {
        if (isHome) {
            mTabGroup.clearCheck();
            mHomeButtonChecked = true;
            mHomeButton.setCompoundDrawablesWithIntrinsicBounds(null, mHomeP, null, null);
        } else {
            mHomeButton.setCompoundDrawablesWithIntrinsicBounds(null, mHomeN, null, null);
            mHomeButtonChecked = false;
        }
    }

    public void toChatting(boolean isSendText, String text) {
        mIsSendText = isSendText;
        mSendText = text;
        if (mTabChattingFragment != null && !Utils.isEmpty(mSendText)) {
            mTabChattingFragment.getPresenter().sendTextMessage(mSendText);
        }

        changeContentFragment(FRAGMENT_TAG_CHATTING);
        mChatRadioButton.setChecked(true);
    }

    public void toKnowledge() {
        changeContentFragment(FRAGMENT_TAG_KNOWLEDGE);
        mKnowledgeButton.setChecked(true);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((TimeUtil.getNowTimeMills() - exitTime) > 2000) {
            ToastUtils.showShortToastSafe("再按一次退出程序");
//            Toast.makeText(HomeActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT);
            exitTime = TimeUtil.getNowTimeMills();
        } else {
            ApiManager.clearCookie();
            finish();
            System.exit(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("hideRedPoint")) {
            if (!Utils.hasUnreadMail() && Utils.hasLuckyCount()) {
                mBadgeView.hide();
            }
        } else if (message.equals("showRedPoint")) {
            mBadgeView.show();
        } else if (message.equals("chooseChild")) {
            changeContentFragment(FRAGMENT_TAG_CHATTING);
            mChatRadioButton.setChecked(true);
        } else if (message.equals("getCommonLanguage")) {
            isUpdateLanguage = true;
            ApiManager.getCommonLanguageList(MySharedPreferences.getUniqueId(), new CommonLanguageListObserver(mResultCallBack));
//            ApiManager.getCommonLanguage(null, "-1", new CommonLanguagesObserver(mResultCallBack));
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
            case Constants.REQUEST_CODE_RECORD_AUDIO:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
