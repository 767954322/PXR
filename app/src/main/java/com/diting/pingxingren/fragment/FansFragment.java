package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.activity.QRCodeActivity;
import com.diting.pingxingren.adapter.MyRankListAdapter;
import com.diting.pingxingren.adapter.RobotRankAdapter;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.LoadListView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by asus on 2017/1/6.
 */

public class FansFragment extends BaseFragment implements LoadListView.ILoadListener, RobotRankAdapter.IconcernListener, LoadListView.RLoadListener, MyRankListAdapter.AttentionListener, SwipeRefreshLayout.OnRefreshListener {
    //    private LoadListView lv_concern;
//    private ValueRankAdapter adapter;
//    private List<RobotConcern> robotFansList = new ArrayList<RobotConcern>();
    private LinearLayout ll_no_fans;
    private LinearLayout ll_main;
    private TextView tv_prompt;
    private int moreCount = 1;
    private boolean isViewCreated;
    private boolean isFisrt = true;
    private Button btn_qrcode;


    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private static final int REQUEST_CODE = 1;
    private MyRankListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fans, null);
//        lv_concern = (LoadListView)view.findViewById(R.id.lv_concern);
        ll_no_fans =   view.findViewById(R.id.ll_no_fans);
        ll_main =   view.findViewById(R.id.ll_main);
        tv_prompt =  view.findViewById(R.id.tv_prompt);
        btn_qrcode =   view.findViewById(R.id.btn_qrcode);
        btn_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(QRCodeActivity.class);
            }
        });
        tv_prompt.setText(R.string.tv_no_fans);
//        ll_main.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Utils.hideSoftInput(getActivity(),v);
//                return false;
//            }
//        });
//        adapter = new ValueRankAdapter(getActivity(),R.layout.item_value_rank, robotFansList);
//        adapter.setListener(this);
//        adapter.setItemClick(true);
//        lv_concern.setAdapter(adapter);
//        lv_concern.setReflashInterface(this);
//        lv_concern.setInterface(this);
//        lv_concern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RobotConcern robotConcern = robotFansList.get(position - 1);
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("robot",robotConcern);
//                startActivity(intent);
//            }
//        });


        mSwipeRefreshWidget =   view.findViewById(R.id.swipe_refresh_widget_myfans);
        mRecyclerView =   view.findViewById(R.id.recycle_view_myfans);

//
//        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorRed,
//                R.color.colorRed, R.color.colorRed,
//                R.color.colorRed);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MyRankListAdapter(getContext());

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRemark(false);
        mAdapter.setListener(this);
        mAdapter.setItemClick(true);
        mSwipeRefreshWidget.setOnRefreshListener(this);


        isViewCreated = true;
        //getRobotFansList(1, false);
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && isFisrt) {
            isFisrt = false;
            getRobotFansList(1, false);
        }
    }

    private void getRobotFansList(final int pageNo, final boolean isFromRefresh) {
        if (!isFromRefresh) {
            showLoadingDialog("加载中");
        }
        Diting.getFansList(pageNo, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
                List<RobotConcern> list = Utils.parseFans(response);
                if (list != null) {
                    ll_no_fans.setVisibility(View.GONE);
                    if (pageNo == 1) {
//                        mAdapter.clear();
                        mAdapter.setData(list);
                    } else {
                        mAdapter.addData(list);
                    }

                    mAdapter.notifyDataSetChanged();
//                    lv_concern.reflashComplete();
//                    lv_concern.loadCompleted();
//                    if(list.size() < 15 && moreCount == 1){
//                        lv_concern.setEnableScroll(false);
//                    }else {
//                        lv_concern.setEnableScroll(true);
//                    }
                    mSwipeRefreshWidget.setRefreshing(false);
                    moreCount++;
                } else {
                    if (pageNo == 1) {
                        ll_no_fans.setVisibility(View.VISIBLE);
                    } else {
                        showShortToast("无更多数据");
//                        lv_concern.loadCompleted();
                    }
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("请求超时！======");
                if (isFromRefresh) {
//                    lv_concern.reflashComplete();
                    mSwipeRefreshWidget.setRefreshing(false);
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLoad() {
        getRobotFansList(moreCount, true);
    }

    @Override
    public void addConcern(final RobotConcern robotConcern) {
        showLoadingDialog("请求中");
        Diting.addConcern(robotConcern.getPhone(), new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                dismissLoadingDialog();
                try {
                    int flag = response.getInt("flg");
                    switch (flag) {
                        case 0:
                            showShortToast("关注成功");
                            robotConcern.setConcerned(true);
                            mAdapter.notifyDataSetChanged();
                            EventBus.getDefault().post("update");
                            break;
                        case 1:
                            showShortToast("只能关注20个机器人");
                            break;
                        case 2:
                            showShortToast("已关注");
                            break;
                    }

//                    Utils.hideSoftInput(getActivity(), lv_concern);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("关注失败");
//                Utils.hideSoftInput(getActivity(), lv_concern);
            }
        });
    }

    @Override
    public void canConcern(RobotConcern robotConcern) {
        showDialog(robotConcern);
    }

    @Override
    public void updateRemark(RobotConcern robotConcern, View view) {
    }

    @Override
    public void getDetail(RobotConcern robotConcern) {

//        startActivityForResult(RobotDetailActivity.getCallingIntent(getActivity(), robotConcern), REQUEST_CODE);

    }

    @Override
    public void onItemClick(RobotConcern robotConcern) {
        // startActivityForResult(RobotDetailActivity.getCallingIntent(getActivity(), robotConcern), REQUEST_CODE);
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtra("robot",robotConcern);
//        startActivity(intent);
        startActivity(NewChatActivity.callingIntent(getActivity(), robotConcern));
    }

    private void showDialog(final RobotConcern robotConcern) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要取消对" + robotConcern.getRobotName() + "的关注吗");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");
                Diting.deleteConcern(robotConcern.getPhone(), new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        dismissLoadingDialog();
                        showShortToast("已取消关注");
                        EventBus.getDefault().post("update");
                        robotConcern.setConcerned(false);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        showShortToast("取消失败，请稍后再试");
                    }
                });
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRobotFansList(moreCount, false);
                    }
                }, 500);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String message) {
        if (message.equals("update")) {
            getRobotFansList(moreCount,true);
        }
    }
    @Override
    public void onRefresh() {
        moreCount = 1;
//        mAdapter.clear();
        getRobotFansList(1, true);
    }
}
