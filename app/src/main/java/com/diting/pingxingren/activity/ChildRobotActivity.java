package com.diting.pingxingren.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.ChildRobotAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.databinding.LayoutCommonRecyclerBinding;
import com.diting.pingxingren.dialog.CreateRobotNotesDialog;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChildRobotsObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 07, 15:23.
 * Description: .
 */

public class ChildRobotActivity extends BaseActivity implements ClickListener {

    private LayoutCommonRecyclerBinding mBinding;
    private ChildRobotAdapter mAdapter;
    private List<ChildRobotModel> mRobotModels = new ArrayList<>();
    private ChildRobotActivityHandler mHandler;
    private CreateRobotNotesDialog mCreateRobotNotesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_common_recycler);
        mBinding.setTitle(getString(R.string.child_robot));
        mBinding.setListener(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        mBinding.titleLayout.ivTitleRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_price_prompt));
        mBinding.titleLayout.ivTitleRight.setVisibility(View.VISIBLE);
        mHandler = new ChildRobotActivityHandler(this);
        mBinding.commonRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ChildRobotAdapter(R.layout.item_child_robot, mRobotModels);
        mAdapter.setClickListener(this);
        mAdapter.openLoadAnimation();
        mBinding.commonRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        EventBus.getDefault().register(this);
        Utils.getChildRobots(mRobotModels, MySharedPreferences.getRobotName());
        mAdapter.setNewData(mRobotModels);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.ivTitleLeft:
                finish();
                break;
            case R.id.ivTitleRight:
                showCreateRobotNotesDialog();
                break;
        }
    }

    private void showCreateRobotNotesDialog() {
        if (mCreateRobotNotesDialog == null)
            mCreateRobotNotesDialog = new CreateRobotNotesDialog(this);
        mCreateRobotNotesDialog.showDialog();
    }

    @Override
    public void onClick(Object o) {
        if (o instanceof ChildRobotModel) {
            ChildRobotModel childRobotModel = (ChildRobotModel) o;
            ApiManager.chooseChildRobot(childRobotModel.getUniqueId(), mResultCallBack);
        } else if (o instanceof String) {
            String s = (String) o;
            if (s.equals("创建")) {
                startActivity(CreateRobotActivity.getCallIntent(this, Utils.getCreateRobotPrice()));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("refreshRobots")) {
            ApiManager.getChildRobots(new ChildRobotsObserver(mResultCallBack));
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof Boolean) {
                boolean b = (boolean) result;
                if (b)
                    ApiManager.getRobotInfo(new RobotInfoObserver(this));
            } else if (result instanceof RobotInfoModel) {
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                MySharedPreferences.saveUniqueId(robotInfoModel.getUniqueId());
                MySharedPreferences.saveProfile(robotInfoModel.getProfile());
                MySharedPreferences.saveRobotName(robotInfoModel.getName());
                MySharedPreferences.saveDefaultRobot(robotInfoModel.getName());
                MySharedPreferences.saveCompanyName(robotInfoModel.getCompanyName());
                MySharedPreferences.saveRobotHeadPortrait(robotInfoModel.getRobotHeadImg());
                EventBus.getDefault().post("chooseChild");
                mHandler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
            if (resultList.size() > 0) {
                Class<?> aClass = resultList.get(0).getClass();
                if (aClass.getName().equals(ChildRobotModel.class.getName())) {
                    Utils.sChildRobotModels.clear();
                    Utils.sChildRobotModels = (List<ChildRobotModel>) resultList;
                    Utils.getChildRobots(mRobotModels, MySharedPreferences.getRobotName());
                    mAdapter.setNewData(mRobotModels);
                }
            }
        }

        @Override
        public void onError(Object o) {
            if (o instanceof Boolean)
                ToastUtils.showShortToastSafe("切换机器人失败!");
            else if (o instanceof String)
                ToastUtils.showShortToastSafe((String) o);
        }
    };

    private static class ChildRobotActivityHandler extends Handler {
        private WeakReference<ChildRobotActivity> mActivityWeakReference;

        public ChildRobotActivityHandler(ChildRobotActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChildRobotActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.finish();
                        break;
                }
            }
        }
    }
}
