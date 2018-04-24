package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.databinding.ActivityEditVoiceInputTextBinding;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 23, 15:21.
 * Description: .
 */

public class EditVoiceInputTextActivity extends BaseActivity implements View.OnClickListener {

    public static Intent getCallingIntent(Context context, String text) {
        Intent intent = new Intent();
        intent.setClass(context, EditVoiceInputTextActivity.class);
        intent.putExtra("text", text);
        return intent;
    }

    private ActivityEditVoiceInputTextBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_voice_input_text);
        mBinding.setText(getIntent().getStringExtra("text"));
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        mBinding.ivBack.setOnClickListener(this);
        mBinding.tvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvComplete:
                Intent intent = new Intent();
                intent.setClass(this, AddKnowledgeActivity.class);
                intent.putExtra("result", mBinding.etContent.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
