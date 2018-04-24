package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.TreasureChestAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.dialog.TreasureChestDialog;
import com.diting.pingxingren.util.UmengUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Creator: Gu FanFan.
 * Date: 2017/10/30 0030, 16:42.
 * Description: 百宝箱Activity.
 */

public class TreasureChestActivity extends BaseActivity implements View.OnClickListener, TreasureChestAdapter.TreasureItemClick {

    private LinearLayout mBackView;
    private TreasureChestDialog mChestDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skinning);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        TextView titleView =  findViewById(R.id.title);
        titleView.setText(getString(R.string.title_treasure_chest));
        mBackView =   findViewById(R.id.llBack);
        RecyclerView treasureRecyclerView =   findViewById(R.id.skinRecycler);
        treasureRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        TreasureChestAdapter adapter = new TreasureChestAdapter();
        adapter.setTreasureItemClick(this);
        treasureRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
        }
    }

    @Override
    public void onClick(int position) {
        switch (position) {
            case 0:
                startActivity(UserManageActivity.class);
                MobclickAgent.onEvent(this, UmengUtil.EVENT_CLICK_NUM_OF_BOX_USER_MANAGE);

                break;
            case 1:
                startActivity(MailNewActivity.class);
                MobclickAgent.onEvent(this, UmengUtil.EVENT_CLICK_NUM_OF_BOX_STATION_MESSAGE);

                break;
            case 2:
            case 3:
            case 4:
            case 5:
                if (mChestDialog == null)
                    mChestDialog = new TreasureChestDialog(this, position);
                mChestDialog.setPosition(position);
                mChestDialog.showDialog();
                break;
        }
    }
}
