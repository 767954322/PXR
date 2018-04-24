package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.SelectIndustryAdapter;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.databinding.LayoutCommonRecyclerBinding;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.util.Utils;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 15, 18:16.
 * Description: .
 */

public class SelectIndustryActivity extends BaseActivity implements SelectIndustryAdapter.IndustryItemClick, ClickListener {

    public static Intent callingIntent(Context context, String industry) {
        Intent intent = new Intent(context, SelectIndustryActivity.class);
        intent.putExtra("industry", industry);
        return intent;
    }

    private LayoutCommonRecyclerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_common_recycler);
        mBinding.setListener(this);
        mBinding.setTitle(getString(R.string.industry_select).replace("请", ""));
        initViews();
    }

    @Override
    protected void initViews() {
        SelectIndustryAdapter adapter = new SelectIndustryAdapter(Utils.sIndustryEntityList);
        adapter.setItemClick(this);
        mBinding.commonRecycler.addItemDecoration(new RecyclerViewDivider(this, RecyclerViewDivider.VERTICAL));
        mBinding.commonRecycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.commonRecycler.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
    }

    @Override
    public void onItemClick(String industry) {
        Intent intent = new Intent();
        intent.putExtra("industry", industry);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTitleLeft:
                finish();
                break;
        }
    }

    @Override
    public void onClick(Object o) {

    }
}
