package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;

/**
 * Created by asus on 2017/1/13.
 */

public class RankActivity extends BaseActivity {
    private TitleBarView titleBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rank);
        initViews();
        initEvents();
    }

    private void initTitleBarView() {
        titleBarView =  findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setTitleText("排行榜");
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
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
    protected void initViews() {
        initTitleBarView();
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
    }
}
