package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.SkinAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.util.MySharedPreferences;

/**
 * Creator: Gu FanFan.
 * Date: 2017/10/26, 12:33.
 * Description: 换肤Activity.
 */

public class SkinningActivity extends BaseActivity implements SkinAdapter.SaveSkin {

    private LinearLayout mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        int skinRes = MySharedPreferences.getSkinRes();
        setContentView(R.layout.activity_skinning);

        if (skinRes == -1)
            getWindow().setBackgroundDrawableResource((R.drawable.icon_skin_default));
        else if (skinRes == R.drawable.ic_skin_one
                || skinRes == R.drawable.ic_skin_two
                || skinRes == R.drawable.ic_skin_three
                || skinRes == R.drawable.ic_skin_four
                || skinRes == R.drawable.ic_skin_five
                || skinRes == R.drawable.ic_skin_six)
            getWindow().setBackgroundDrawableResource((skinRes));
        else
            getWindow().setBackgroundDrawableResource((R.drawable.icon_skin_default));

        RecyclerView skinRecyclerView =   findViewById(R.id.skinRecycler);
        skinRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SkinAdapter adapter = new SkinAdapter();
        adapter.setSaveSkin(this);
        adapter.setSkinRes(skinRes);
        skinRecyclerView.setAdapter(adapter);
        mBackView =  findViewById(R.id.llBack);
    }

    @Override
    protected void initEvents() {
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onSave(int skinRes) {
        MySharedPreferences.saveSkinRes(skinRes);
        finish();
    }
}
