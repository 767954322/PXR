package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.AlxUrlTextView;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.PersonalMailItemModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.SystemMessageItemModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;

import java.util.List;


/**
 * Created by asus on 2017/1/13.
 */

public class MailDetailActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private TextView tv_mail_title;
    private TextView tv_mail_time;
    private AlxUrlTextView tv_mail_content;
    private SystemMessageItemModel systemMessageItemModel;
    private PersonalMailItemModel personalMailItemModel;
    private RelativeLayout rl_mail;
    private String intentType;
    private Button btn_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mail_detail);
        initViews();
        initEvents();
        initDate();
//        EventBus.getDefault().register(this);

    }

    @Override
    protected void initViews() {
        initTitleBarView();
        tv_mail_title = findViewById(R.id.tv_mail_title);
        tv_mail_time = findViewById(R.id.tv_mail_time);
        tv_mail_content = findViewById(R.id.tv_mail_content);
        rl_mail = findViewById(R.id.rl_mail);
        btn_username = findViewById(R.id.btn_username);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRobotInfo();
            }
        });
    }


    private void initTitleBarView() {
        intentType = getIntent().getStringExtra("from");
        systemMessageItemModel = getIntent().getParcelableExtra("System");
        personalMailItemModel = getIntent().getExtras().getParcelable("Personal");
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
    }

    private void initDate() {

        if ("MailSystemFragment".equals(intentType)) {
            long ll = getIntent().getLongExtra("ctiime", 1);
            titleBarView.setTitleText("系统消息");
            btn_username.setVisibility(View.GONE);
            tv_mail_title.setVisibility(View.VISIBLE);
            tv_mail_title.setText(systemMessageItemModel.getBiaoti());
            tv_mail_time.setText(TimeUtil.millis2String(systemMessageItemModel.getCreatedTime(), TimeUtil.PATTERN_YMD));
            tv_mail_content.setText(systemMessageItemModel.getZhengwen());
            tv_mail_content.setText(Html.fromHtml(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + systemMessageItemModel.getZhengwen()).toString()));
            tv_mail_content.setMovementMethod(LinkMovementMethod.getInstance());


        } else if ("MailRobotFragment".equals(intentType)) {
            titleBarView.setTitleText("私信");
            tv_mail_title.setText(personalMailItemModel.getForword_name());
            tv_mail_title.setVisibility(View.GONE);
            btn_username.setVisibility(View.VISIBLE);
            btn_username.setText(personalMailItemModel.getForword_name());
            tv_mail_time.setText(TimeUtil.millis2String(personalMailItemModel.getCreatedTime(), TimeUtil.PATTERN_YMD));
            tv_mail_content.setText(personalMailItemModel.getMessage());
            tv_mail_content.setText(Html.fromHtml(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + personalMailItemModel.getMessage()).toString()));
            tv_mail_content.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    private void initTitleBarEvents() {
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
//
//    private void deleteMailSystem() {
//        Diting.editMailSystem(mail.getMailId(), new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                mail.setRead(true);
//                EventBus.getDefault().post("readed");
//                MyApplication.unreadLetterNum--;
//                if (!Utils.hasUnreadMail()) {
//                    EventBus.getDefault().post("hideRedPoint");
//                }
//                finish();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                finish();
//            }
//        });
//    }

//    private void deleteMailRobot() {
//        Diting.editMailRobot(mail.getMailId(), new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                mail.setRead(true);
//                EventBus.getDefault().post("readed");
//                if (--MyApplication.unreadMessageNum <= 0) {
//                    EventBus.getDefault().post("NoRobotMail");
//                }
//                if (!Utils.hasUnreadMail()) {
//                    EventBus.getDefault().post("hideRedPoint");
//                }
//                finish();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                finish();
//            }
//        });
//    }


//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onEvent(SystemMessageItemModel mail) {
//        this.mail = mail;
//        tv_mail_title.setText(mail.getBiaoti());
//        tv_mail_time.setText(TimeUtil.getDayTime(mail.getCreatedTime()));
//        tv_mail_content.setText(mail.getZhengwen());
//        tv_mail_content.setText(Html.fromHtml(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + mail.getZhengwen()).toString()));
//        tv_mail_content.setMovementMethod(LinkMovementMethod.getInstance());
////        if (!TextUtils.isEmpty(mail.getUniqueId())) {
////            rl_mail.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    getRobotInfo();
////                }
////            });
////        }
//    }

    private void getRobotInfo() {
        showLoadingDialog("加载中");

        ApiManager.getRobotInfoByUniqueId(personalMailItemModel.getForword_unique_id(), new RobotInfoObserver(mResultCallBack));

//
//
//
//
//        Diting.getRobotByUniqueId(mail.getUniqueId(), new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                gotoChat(response);
//                dismissLoadingDialog();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                dismissLoadingDialog();
//            }
//        });
    }


    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof RobotInfoModel) {
                dismissLoadingDialog();
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;

                dismissLoadingDialog();
                robotInfoModel.setmFrom("isOtherChat");

//                ChatBundle chatBundle = new ChatBundle();
//                chatBundle.setFrom("isOtherChat");
//                chatBundle.setUserName(robotInfoModel.getUsername());
//                chatBundle.setHeadPortrait(robotInfoModel.getHeadImg());
//                chatBundle.setRobotName(robotInfoModel.getName());
//                chatBundle.setWelcome(robotInfoModel.getWelcomes());
//                chatBundle.setAnswer1(robotInfoModel.getInvalidAnswer1());
//                chatBundle.setAnswer2(robotInfoModel.getInvalidAnswer2());
//                chatBundle.setAnswer3(robotInfoModel.getInvalidAnswer3());
                startActivity(NewChatActivity.callingIntent(
                        MailDetailActivity.this, robotInfoModel));


            }
        }

        @Override
        public void onResult(List<?> resultList) {
//            Class<?> aClass = resultList.get(0).getClass();
//            if (aClass.getName().equals(ChildRobotModel.class.getName())) {
//                Utils.sChildRobotModels = (List<ChildRobotModel>) resultList;
//                ApiManager.chooseChildRobot(
//                        Utils.getRobotUniqueIdByChildRobotList(MySharedPreferences.getRobotName()), this);
//            }
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            if (o instanceof String) {
                showShortToast((String) o);
            }
        }
    };



//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    public void onBackPressed() {
        finish();
//        if(mail.isRead()){
//            finish();
//        }else {
//            if(getIntent().getStringExtra("from").equals("MailSystemFragment")){
//                deleteMailSystem();
//            }else {
//                deleteMailRobot();
//            }
//        }
    }
}
