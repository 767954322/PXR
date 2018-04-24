package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 万能答案添加
 */
public class UniversalAnswerActivity extends BaseActivity {
    private TextView tv_universal_answer1;
    private TextView tv_universal_answer2;
    private TextView tv_universal_answer3;
    private TitleBarView titleBarView;
    private String invalidAnswer3;
    private String invalidAnswer2;
    private String invalidAnswer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_answer);
        initViews();
        initDate();
        initEvents();
        initTitleView();

    }

    private void initDate() {
        if (Utils.isEmpty(MySharedPreferences.getUniversalAnswer1())) {
            tv_universal_answer1.setHint(getString(R.string.universal_answer_tip));
        } else {
            tv_universal_answer1.setText(MySharedPreferences.getUniversalAnswer1());
        }
        if (Utils.isEmpty(MySharedPreferences.getUniversalAnswer2())) {
            tv_universal_answer2.setHint(getString(R.string.universal_answer_tip));
        } else {
            tv_universal_answer2.setText(MySharedPreferences.getUniversalAnswer2());
        }
        if (Utils.isEmpty(MySharedPreferences.getUniversalAnswer3())) {
            tv_universal_answer3.setHint(getString(R.string.universal_answer_tip));
        } else {
            tv_universal_answer3.setText(MySharedPreferences.getUniversalAnswer3());
        }
    }

    private void initTitleView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("万能答案");
        titleBarView.setBtnRightText("完成");
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRobotInfo();
            }
        });
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initViews() {
        tv_universal_answer1 = findViewById(R.id.tv_universal_answer1);
        tv_universal_answer2 = findViewById(R.id.tv_universal_answer2);
        tv_universal_answer3 = findViewById(R.id.tv_universal_answer3);

    }

    public void saveRobotInfo() {
        invalidAnswer1 = tv_universal_answer1.getText().toString().trim();
        invalidAnswer2 = tv_universal_answer2.getText().toString().trim();
        invalidAnswer3 = tv_universal_answer3.getText().toString().trim();
        if (Utils.isEmpty(invalidAnswer1) && Utils.isEmpty(invalidAnswer2) && Utils.isEmpty(invalidAnswer3)) {
            showShortToast("请至少填写一条万能答案");

            return;
        }
        showLoadingDialog("请求中");
        ApiManager.saveRobotInfo(MySharedPreferences.getHeadPortrait(),
                MySharedPreferences.getCompanyName(),
                MySharedPreferences.getRobotName(),
                MySharedPreferences.getProfile(),
                MySharedPreferences.getEnable(),
                MySharedPreferences.getUserSex(),
                MySharedPreferences.getIndustry(),
                MySharedPreferences.getHangYeLevel(),
                MySharedPreferences.getPrice(),
                MySharedPreferences.getShengri(), invalidAnswer1, invalidAnswer2, invalidAnswer3,
                new ResultMessageObserver(new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        String msg = (String) result;
                        if (msg.contains("机器人信息修改成功")) {
                            MySharedPreferences.saveUniversalAnswer1(invalidAnswer1);
                            MySharedPreferences.saveUniversalAnswer2(invalidAnswer2);
                            MySharedPreferences.saveUniversalAnswer3(invalidAnswer3);
                            dismissLoadingDialog();
                            EventBus.getDefault().post("answer");
                            finish();


                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {
                        dismissLoadingDialog();
                        if (o instanceof String) {
                            ToastUtils.showShortToastSafe((String) o);
                        }
                    }
                }));
    }

    @Override
    protected void initEvents() {


    }
}
