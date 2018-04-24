package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import org.json.JSONObject;

/**
 * Created by asus on 2017/1/16.
 */

public class CaicaiActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private Button btn_commit;
    private EditText et_content;
    private LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_caicai);
        initViews();
        initEvents();
    }

    private void initTitleBarView(){
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText("采采");
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
        et_content = (EditText) findViewById(R.id.et_content);
        btn_commit = (Button)findViewById(R.id.btn_commit);
        ll_main = (LinearLayout)findViewById(R.id.ll_main);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_commit.setEnabled(false);
                String content = et_content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    showShortToast("内容不能为空");
                    btn_commit.setEnabled(true);
                }else {
                    Diting.feedback(content, null, new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            showShortToast("反馈成功");
                            btn_commit.setEnabled(true);
                            finish();
                        }

                        @Override
                        public void onFailed(VolleyError error) {
                            showShortToast("反馈失败");
                            btn_commit.setEnabled(true);
                        }
                    });
                }
            }
        });
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(CaicaiActivity.this,v);
                return false;
            }
        });
    }
}
