package com.diting.pingxingren.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.HomeActivity;
import com.diting.pingxingren.activity.InvalidQuestionActivity;
import com.diting.pingxingren.activity.MyLuckyActivity;
import com.diting.pingxingren.activity.UserManageActivity;
import com.diting.pingxingren.adapter.HomeAppAdapter;
import com.diting.pingxingren.base.BasePageFragment;
import com.diting.pingxingren.databinding.FragmentHomeBinding;
import com.diting.pingxingren.entity.HomeAppItem;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.news.activity.OneMainActivity;
import com.diting.pingxingren.smarteditor.activity.EditorHomeActivity;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.pingxingren.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 17, 11:59.
 * Description: .
 */

public class TabHomeFragment extends BasePageFragment implements ClickListener {

    private HomeActivity mHomeActivity;
    private FragmentHomeBinding mBinding;
    private BaseQuickAdapter mAdapter;
    private String mSubTitle;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        mBinding = (FragmentHomeBinding) mViewBinding;
        mBinding.titleLayout.ivTitleLeft.setVisibility(View.GONE);
        mBinding.titleLayout.btTitleRight.setVisibility(View.GONE);
        mBinding.setTitle(getString(R.string.tab_home_title));
        mBinding.setListener(this);
        mBinding.appRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    }

    @Override
    public void initData() {
        super.initData();
        if (mActivity instanceof HomeActivity) {
            mHomeActivity = (HomeActivity) mActivity;
        }

        mAdapter = new HomeAppAdapter(R.layout.item_home_application, Utils.sHomeAppItems);
        mAdapter.openLoadAnimation();
        mBinding.appRecycler.setAdapter(mAdapter);

        initRobotInfo();
    }

    private void initRobotInfo() {
        mBinding.tvRobotName.setText("机器人名称:" + MySharedPreferences.getRobotName());
        mBinding.tvCompanyName.setText("主人名称:" + MySharedPreferences.getCompanyName());
        mBinding.tvConcernCount.setText("粉丝数 : " + MySharedPreferences.getFansCount());
        mBinding.tvValueCount.setText("价值 :" + MySharedPreferences.getRobotVal());
        String headImage = MySharedPreferences.getHeadPortrait();

//        Glide.with(getActivity()).load(headImage)
//                .placeholder(R.mipmap.icon_right)
//                .error(R.mipmap.icon_right)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(mPhoto);
        if (Utils.isNotEmpty(headImage)) {
//            Glide.with(getActivity()).load(headImage)
//                .placeholder(R.mipmap.icon_right)
//                .error(R.mipmap.icon_right)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(mBinding.ivPhoto);
            Glide.with(getActivity()).load(headImage).error(R.mipmap.icon_right).into(mBinding.ivPhoto);
        } else {
            mBinding.ivPhoto.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.icon_right));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeAppItem homeAppItem = Utils.sHomeAppItems.get(position);
                String title = homeAppItem.getTitle();
                mSubTitle = homeAppItem.getSubTitle();
                switch (title) {
                    case "访客管理":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_USER_MANAGE);
                        startToActivity(UserManageActivity.class);
                        break;
                    case "未知问题":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_INVALID_QUESTION);
                        startToActivity(InvalidQuestionActivity.class);
                        break;
                    case "抽奖有礼":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_LUCK_DRAW);
                        startToActivity(MyLuckyActivity.class);
                        break;
                    case "语音记事":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_VOICE_NOTES);
                        startToActivity(EditorHomeActivity.getCallIntent(mActivity));
                        break;
                    case "智能小编":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_SMALL_EDITOR);
                        mHomeActivity.toKnowledge();
                        break;
                    case "每日新闻":
                        MobclickAgent.onEvent(mHomeActivity,UmengUtil.EVENT_CLICK_NUM_OF_HOME_DAILY_NEWS);
                        startToActivity(OneMainActivity.class);
                        break;
//                    case "日程提醒":
//                        intent = new Intent(Intent.ACTION_VIEW);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setDataAndType(Uri.parse("content://com.android.calendar/"), "time/epoch");
//                        getActivity().startActivity(intent);
//                        break;
                    default:
                        if (mHomeActivity != null)
                            mHomeActivity.toChatting(true, mSubTitle);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btChat:
                MobclickAgent.onEvent(mHomeActivity, UmengUtil.EVENT_CLICK_NUM_OF_HOME_TALK);
                if (mHomeActivity != null)
                    mHomeActivity.toChatting(false, "");
                break;
            case R.id.layout_info:
//                if (mHomeActivity != null)
//                    mHomeActivity.toChatting(false, "");
                break;
        }
    }

    @Override
    public void onClick(Object o) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("chooseChild")) {
            initRobotInfo();
        } else if (message.equals("updateHeadImage")) {
//            Glide.with(this).load(MySharedPreferences.getHeadPortrait()).into(mBinding.ivPhoto);
//            mBinding.tvRobotName.setText(MySharedPreferences.getRobotName());
//            mBinding.tvCompanyName.setText(MySharedPreferences.getCompanyName());
            initRobotInfo();
        }
    }
}
