package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by asus on 2017/1/6.
 */

public class EditInvalidQuestionActivity extends BaseActivity{
    private TitleBarView titleBarView;
    private TextView tv_question;
    private EditText et_answer;
    private Button btn_commit;
    private LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_invalid_question);
        initViews();
        initEvents();
    }
    private void initTitleBarView(){
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.VISIBLE);
        titleBarView.setTitleText("编辑");
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarView.setBtnRightText("完成");
    }
    @Override
    protected void initViews() {
        initTitleBarView();
        tv_question =  findViewById(R.id.tv_question);
        et_answer =  findViewById(R.id.et_answer);
        btn_commit = titleBarView.getBtnRight();
        ll_main =  findViewById(R.id.ll_main);
        tv_question.setText(getIntent().getStringExtra("question"));
    }

    @Override
    protected void initEvents() {
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInvalidQuestion();
            }
        });
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(EditInvalidQuestionActivity.this,v);
                return false;
            }
        });
    }

    private void editInvalidQuestion(){
        btn_commit.setEnabled(false);
        String question = tv_question.getText().toString();
        String answer = et_answer.getText().toString();
        if(TextUtils.isEmpty(answer)){
            showShortToast("答案不能为空");
            btn_commit.setEnabled(true);
            return;
        }
        showLoadingDialog("请求中");

        ApiManager.invalidQuestionEdit(question, answer, new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    dismissLoadingDialog();
                    showShortToast(jsonObject.getString("message"));
//                    if (jsonObject.getString("message").equals("添加成功！")) {
//                        showShortToast("编辑成功");
                        EventBus.getDefault().post("edit_success");
                        dismissLoadingDialog();
                        finish();
//                    } else {
//                        showShortToast("编辑失败");
//                        dismissLoadingDialog();
//                        btn_commit.setEnabled(true);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {
                if (o instanceof String)
                    showShortToast((String) o);
            }
        });



//        Diting.editInvalidQuestion(question, answer, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//
//
////                "message": "添加成功！"
//                showShortToast("编辑成功");
//                EventBus.getDefault().post("edit_success");
//                dismissLoadingDialog();
//                finish();
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                showShortToast("编辑失败");
//                dismissLoadingDialog();
//                btn_commit.setEnabled(true);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
