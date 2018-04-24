package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MultiEditInputView;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Created by asus on 2017/1/16.
 */

public class FeedBackActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private MultiEditInputView et_feedback_content;
    private Button btn_feedback;
    private LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        initViews();
        initEvents();
    }

    private void initTitleBarView(){
        titleBarView = (TitleBarView)findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText(R.string.feedback);
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        et_feedback_content =  findViewById(R.id.et_feedback_content);
        btn_feedback =  findViewById(R.id.feedback);
        ll_main =  findViewById(R.id.ll_main);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_feedback.setEnabled(false);
                String feedback_content = et_feedback_content.getContentText();
                if(TextUtils.isEmpty(feedback_content)){
                    showShortToast("内容不能为空");
                    btn_feedback.setEnabled(true);
                }else {
                    ApiManager.submitFeedback(feedback_content, new ResultCallBack() {
                        @Override
                        public void onResult(Object result) {
                            showShortToast("反馈成功");
                            btn_feedback.setEnabled(true);
                            finish();
                        }

                        @Override
                        public void onResult(List<?> resultList) {

                        }

                        @Override
                        public void onError(Object o) {
                            showShortToast("反馈失败");
                            btn_feedback.setEnabled(true);
                        }
                    });
//                    Diting.feedback(feedback_content, null, new HttpCallBack() {
//                        @Override
//                        public void onSuccess(JSONObject response) {
//                            showShortToast("反馈成功");
//                            btn_feedback.setEnabled(true);
//                            finish();
//                        }
//
//                        @Override
//                        public void onFailed(VolleyError error) {
//                            showShortToast("反馈失败");
//                            btn_feedback.setEnabled(true);
//                        }
//                    });
                }
            }
        });
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(FeedBackActivity.this,v);
                return false;
            }
        });
    }
}
