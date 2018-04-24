package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseChangeFragments;
import com.diting.pingxingren.entity.ChatBundle;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.fragment.TabChattingFragment;
import com.diting.pingxingren.model.ChatBundleModel;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.model.RobotInfoModel;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 05, 10:54.
 * Description: .
 */

public class NewChatActivity extends BaseChangeFragments {

    public static Intent callingIntent(Context context) {
        return new Intent(context, NewChatActivity.class);
    }

    public static Intent callingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NewChatActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent callingIntent(Context context, RobotConcern robotConcern) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("robot", robotConcern);
        bundle.putBoolean("isOtherChat", true);
        return callingIntent(context, bundle);
    }

    public static Intent callingIntent(Context context, CommunicateModel communicateModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("robot", communicateModel);
        bundle.putBoolean("isOtherChat", true);
        return callingIntent(context, bundle);
    }

//    public static Intent callingIntent(Context context, ChatBundle chatBundle) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("data", chatBundle);
//        bundle.putBoolean("isOtherChat", true);
//        return callingIntent(context, bundle);
//    }

    public static Intent callingIntent(Context context, RobotInfoModel robotInfoModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", robotInfoModel);
        bundle.putBoolean("isOtherChat", true);
        return callingIntent(context, bundle);
    }
    public static Intent callingIntent(Context context, ChatBundleModel chatBundle) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", chatBundle);
        bundle.putBoolean("isOtherChat", true);
        return callingIntent(context, bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        DataBindingUtil.setContentView(this, R.layout.layout_common_fragment);
        changeFragment(FRAGMENT_TAG_CHATTING, R.id.fragmentContainer,
                TabChattingFragment.createChatFragment(getIntent().getExtras()));
    }
}
