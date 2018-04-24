package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragmentActivity;
import com.diting.pingxingren.custom.BadgeView;
import com.diting.pingxingren.fragment.KnowledgeFragment;
import com.diting.pingxingren.fragment.TabConcernFragment;
import com.diting.pingxingren.fragment.TabMineFragment;
import com.diting.pingxingren.fragment.TabSettingFragment;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.KeyboardChangeListener;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import static com.diting.pingxingren.util.Utils.hasUnreadMail;

/**
 * Created by asus on 2017/1/6.
 */

public class MainActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RelativeLayout rl_bottom;
    private RadioGroup rgTab;
    private RadioButton tab_mine;
    private TabSettingFragment tabSettingFragment;
    private TabConcernFragment tabConcernFragment;
    private TabMineFragment tabMineFragment;
    private KnowledgeFragment tabKnowledgeFragment;
    private BadgeView mBadgeView;
    private Button button;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
        //getMailCount();
    }


    private void getMailCount() {
        Diting.searchMailCount(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    MyApplication.unreadLetterNum = response.getInt("unreadLetterNum");
                    MyApplication.unreadMessageNum = response.getInt("unreadMessageNum");
                    if (hasUnreadMail()) {
                        mBadgeView.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tab_mine.setChecked(true);
            }

            @Override
            public void onFailed(VolleyError error) {
                tab_mine.setChecked(true);
            }
        });
    }

    private void initBadgeView(View view) { //BadgeView的具体使用
        mBadgeView = new BadgeView(this, view);// 创建一个BadgeView对象，view为你需要显示提醒的控件// 需要显示的提醒类容
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
//        mBadgeView.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        //mBadgeView.setBackgroundResource(R.mipmap.icon_message_png); //设置背景图片
        mBadgeView.setBackgroundResource(R.drawable.point);
        mBadgeView.setTextSize(12); // 文本大小
        mBadgeView.setBadgeMargin(40, 20); // 水平和竖直方向的间距
        //mBadgeView.setBadgeMargin(5); //各边间隔
        // mBadgeView.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
        //  mBadgeView.show();只有显示
        // mBadgeView.hide();//影藏显示
    }

    protected void initViews() {
       /* button = (Button) findViewById(R.id.btn_mi);
        initBadgeView(button);*/
        rgTab =   findViewById(R.id.rg_tab);
//        tab_setting = (RadioButton)findViewById(R.id.tab_setting);
//        tab_concern = (RadioButton)findViewById(R.id.tab_concern);
        tab_mine =   findViewById(R.id.tab_mine);
        rl_bottom =   findViewById(R.id.rl_bottom);
    }

    protected void initEvents() {
        rgTab.setOnCheckedChangeListener(this);
        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    rl_bottom.setVisibility(View.GONE);
                } else {
                    rl_bottom.setVisibility(View.VISIBLE);
                }

                if (isFirst) {
                    isFirst = false;
                    rl_bottom.setVisibility(View.VISIBLE);
                } else {
                    rl_bottom.setVisibility(View.GONE);
                }
            }
        });
        tab_mine.setChecked(true);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (tabMineFragment != null) {
            transaction.hide(tabMineFragment);
        }
        if (tabSettingFragment != null) {
            transaction.hide(tabSettingFragment);
        }
        if (tabConcernFragment != null) {
            transaction.hide(tabConcernFragment);
        }
        if (tabKnowledgeFragment != null) {
            transaction.hide(tabKnowledgeFragment);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.tab_chatting:
                if (tabMineFragment == null) {
                    tabMineFragment = new TabMineFragment();
                    transaction.add(R.id.fragment_container, tabMineFragment);
                } else {
                    transaction.show(tabMineFragment);
                }
                break;

            case R.id.tab_communication:
                if (tabConcernFragment == null) {
                    tabConcernFragment = new TabConcernFragment();
                    transaction.add(R.id.fragment_container, tabConcernFragment);
                } else {
                    transaction.show(tabConcernFragment);
                }
                break;

            case R.id.tab_mine:
                if (tabSettingFragment == null) {
                    tabSettingFragment = new TabSettingFragment();
                    transaction.add(R.id.fragment_container, tabSettingFragment);
                } else {
                    transaction.show(tabSettingFragment);
                }
                break;
            case R.id.tab_knowledge:
                if (tabKnowledgeFragment == null) {
                    tabKnowledgeFragment = new KnowledgeFragment();
                    transaction.add(R.id.fragment_container, tabKnowledgeFragment);
                } else {
                    transaction.show(tabKnowledgeFragment);
                }
                break;
            default:
                break;
        }
        Utils.hideSoftInput(this, tab_mine);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        //实现Home键效果
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
        MediaManager.pause();
        MediaManager.release();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mailReaded(String message) {
        if (message.equals("hideRedPoint")) {
            mBadgeView.hide();
        } else if (message.equals("showRedPoint")) {
            mBadgeView.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
        if (tabSettingFragment == null && fragment instanceof TabSettingFragment) {
            tabSettingFragment = (TabSettingFragment) fragment;
        } else if (tabConcernFragment == null && fragment instanceof TabConcernFragment) {
            tabConcernFragment = (TabConcernFragment) fragment;
        } else if (tabMineFragment == null && fragment instanceof TabMineFragment) {
            tabMineFragment = (TabMineFragment) fragment;
        } else if (tabKnowledgeFragment == null && fragment instanceof KnowledgeFragment) {
            tabKnowledgeFragment = (KnowledgeFragment) fragment;
        }
    }

}
