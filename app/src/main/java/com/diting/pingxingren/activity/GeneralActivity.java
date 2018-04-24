package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.BadgeView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.voice.DITingVoice;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Creator: Gu FanFan.
 * Date: 2017/10/24, 14:40.
 * Description: 通用Activity.
 */

public class GeneralActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mAuthority;
    private RelativeLayout mVisitors;
    private RelativeLayout mCallLogs;
    private RelativeLayout mMails;
    private RelativeLayout mFontSize;
    private RelativeLayout mBindThird;
    private RelativeLayout mUpdatePass;
    private RelativeLayout mFeedback;
    private RelativeLayout mUpdateVersion;
    private RelativeLayout mLogout;
    private LinearLayout mBackView;
    private ImageView mBack;
    private TitleBarView titleBarView;
    private Button mBtnMail;
    private BadgeView mBadgeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();

        initData();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        if ((MyApplication.unreadLetterNum + MyApplication.unreadMessageNum) > 0) {
            mBadgeView.show();
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_general);
        initTitleBarView();
        mBack = findViewById(R.id.ivBack);
        mAuthority = findViewById(R.id.rlAuthoritySetting);
        mVisitors = findViewById(R.id.rlVisitorsManage);
        mCallLogs = findViewById(R.id.rlCallLog);
        mMails = findViewById(R.id.rlMail);
        mFontSize = findViewById(R.id.rlFontSize);
        mUpdatePass = findViewById(R.id.rlUpdatePassword);
        mFeedback = findViewById(R.id.rlFeedback);
        mUpdateVersion = findViewById(R.id.rlUpdateVersion);
        mLogout = findViewById(R.id.rlLogout);
        mBackView = findViewById(R.id.llBack);
        mBtnMail = findViewById(R.id.btn_mail_redpoint);
        mBindThird = findViewById(R.id.rlBindThird);
        initBadgeView(mBtnMail);

        mUpdatePass.setVisibility(MySharedPreferences.isDefaultRobot() ? View.VISIBLE : View.GONE);
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("通用");

    }

    private void initTitleBarEvents() {
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initEvents() {
        mBack.setOnClickListener(this);
        mAuthority.setOnClickListener(this);
        mVisitors.setOnClickListener(this);
        mCallLogs.setOnClickListener(this);
        mMails.setOnClickListener(this);
        mFontSize.setOnClickListener(this);
        mUpdatePass.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mUpdateVersion.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mBindThird.setOnClickListener(this);
        initTitleBarEvents();
    }

    private void initBadgeView(View mBtnMail) {
        mBadgeView = new BadgeView(this, mBtnMail);// 创建一个BadgeView对象，view为你需要显示提醒的控件// 需要显示的提醒类容
        mBadgeView.setBadgePosition(BadgeView.POSITION_CENTER);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        mBadgeView.setBackgroundResource(R.drawable.point);
        mBadgeView.setTextSize(12); // 文本大小
        mBadgeView.setBadgeMargin(40, 20); // 水平和竖直方向的间距
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.llBack:
                finish();
                break;
            case R.id.rlAuthoritySetting://呼入设置
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_PERMISSIONS_SETTINGS);
                startActivity(SecrecySettingActivity.class);
                break;
            case R.id.rlVisitorsManage://访客管理
                MobclickAgent.onEvent(this, UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_USER_MANAGE);
                startActivity(UserManageActivity.class);
                break;
            case R.id.rlCallLog://通话记录
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_CALL_RECORD);
                startActivity(CallLogActivity.class);
                break;
            case R.id.rlMail://站内消息
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_STATION_MESSAGE);
                startActivity(MailNewActivity.class);
                break;
            case R.id.rlFontSize://字体设置
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_FONT_SIZE);
                startActivity(FontSizeActivity.class);
                break;
            case R.id.rlUpdatePassword://修改密码
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_RESET_PASSWORD);
                startActivity(ChangePasswordActivity.class);
                break;
            case R.id.rlFeedback://意见反馈
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_FEEDBACK);
                startActivity(FeedBackActivity.class);
                break;
            case R.id.rlUpdateVersion://版本更新
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_VERSION_UPDATE);
                startActivity(AboutDitingActivity.class);
                break;
            case R.id.rlLogout:
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_LOGOUT);
                showDialog();
                break;
            case R.id.rlBindThird://其他方式登录
                MobclickAgent.onEvent(this,UmengUtil.EVENT_CLICK_NUM_OF_GENERAL_OTHER_LOGIN);
                startActivity(ThirdCheckBindActivity.class);
                break;
        }
    }

    private void showDialog() {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要退出吗?");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DITingVoice.getInstance().onDestroy();
                    }
                }).start();
                mMyApplication.settingLogout();
                MySharedPreferences.saveLogin(false);
                MySharedPreferences.clearCookie();
                MyApplication.sCommonLanguages.clear();
                MobclickAgent.onProfileSignOff();
                startActivity(LoginActivity.class);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mailReaded(String message) {
        if (message.equals("hideRedPoint")) {
            mBadgeView.hide();
        } else if (message.equals("showRedPoint")) {
            mBadgeView.show();
        }
    }
}
