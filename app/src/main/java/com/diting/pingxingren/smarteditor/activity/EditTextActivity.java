package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.diting.pingxingren.R;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.news.listener.OnMClickListener;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.util.StringUtil;

/**
 * 编辑文字页面
 * Created by 2018 on 2018/1/12.
 */

public class EditTextActivity extends BaseActivity implements OnMClickListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, EditTextActivity.class);
        return intent;
    }

    public static Intent getCallIntent(Context context, Bundle bundle) {
        Intent intent = getCallIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallIntent(Context context, String content, boolean isEdit) {
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putBoolean("isEdit", isEdit);
        return getCallIntent(context, bundle);
    }

    private TitleBarView mTitleBarView;
    private EditText mEditText;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_edit_text);

        mTitleBarView = findViewById(R.id.title_bar);
        mTitleBarView.initTitle(true, StringUtil.getString(R.string.edit_text),
                StringUtil.getString(R.string.submit), this);
        mEditText = findViewById(R.id.et_edit);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            boolean isEdit = bundle.getBoolean("isEdit");

            if (isEdit && StringUtil.isNotEmpty(content)) {
                mEditText.setText(content);
            }
        }
    }

    @Override
    public void onMClick(int id) {
        switch (id){
            case R.id.title_btn_right://保存
                String content = mEditText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("content",StringUtil.isNotEmpty(content) ? content : "");
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
