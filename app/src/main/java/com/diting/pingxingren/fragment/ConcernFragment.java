package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.activity.SearchRobotActivity;
import com.diting.pingxingren.adapter.MyRankListAdapter;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.LoadListView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.HttpJsonArrayCallBack;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by asus on 2017/1/6.
 */

public class ConcernFragment extends BaseFragment implements /*RobotConcernAdapter.IconcernListener,*/ LoadListView.RLoadListener, MyRankListAdapter.AttentionListener, SwipeRefreshLayout.OnRefreshListener {

    //    private LoadListView lv_concern;
//    private ValueRankAdapter adapter;
//    private List<RobotConcern> robotConcernList = new ArrayList<RobotConcern>();
    private LinearLayout ll_no_concern;
    private LinearLayout ll_main;
    private TextView searchView;
    private boolean isViewCreated;
    private boolean isFirst = true;
    private RelativeLayout rl_search;
    private TextView btn_sort;
    //    private List<RobotConcern> searchList = new ArrayList<RobotConcern>();
    private static final int REQUEST_CODE = 1;
    private Button btn_concern;
    private List<Fragment> mFragments;
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    private MyRankListAdapter mAdapter;

    private TabConcernFragment mTabConcernFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        EventBus.getDefault().register(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_concern, null);
//        lv_concern = (LoadListView) view.findViewById(R.id.lv_concern);
        ll_no_concern =  view.findViewById(R.id.ll_no_concern);
        btn_concern =   view.findViewById(R.id.btn_concern);
        ll_main =   view.findViewById(R.id.ll_main);
        rl_search =   view.findViewById(R.id.rl_search);
        searchView =   view.findViewById(R.id.search);
        rl_search.setVisibility(View.VISIBLE);
        btn_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("gotoRankList");
            }
        });
        btn_sort =   view.findViewById(R.id.btn_sort);
        btn_sort.setCompoundDrawables(null, null, null, null);
        // btn_sort.setVisibility(View.VISIBLE);
        btn_sort.setText("通讯录导入");
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(ContactLocalActivity.getCallingIntent(getContext(),robotConcernList));
                EventBus.getDefault().post("gotoRankList");
            }
        });
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(getActivity(), v);
                return false;
            }
        });
//        adapter = new ValueRankAdapter(getActivity(), R.layout.item_value_rank, robotConcernList);
//        adapter.setRemark(true);
//        adapter.setListener(this);
//        adapter.setItemClick(true);
//        lv_concern.setReflashInterface(this);
//        lv_concern.setAdapter(adapter);
//        lv_concern.setTextFilterEnabled(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchRobotActivity.class);
            }
        });


        mSwipeRefreshWidget = view.findViewById(R.id.swipe_refresh_widget_myRank);
        mRecyclerView =  view.findViewById(R.id.recycle_view_myRank);

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


        /*searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    adapter.getFilter().filter(s);
                    //search(s);
                } else {
                    adapter.getFilter().filter(null);
                    //adapter.setData(robotConcernList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        isViewCreated = true;
        //getRobotConcernList();
        return view;
    }


//    private void search(CharSequence s) {
//        searchList.clear();
//        for (int i = 0; i < robotConcernList.size(); i++) {
//            RobotConcern robotConcern = robotConcernList.get(i);
//            if (robotConcern.getRobotName().contains(s)) {
//                searchList.add(robotConcern);
//            }
//        }
//        // adapter.setData(searchList);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && isFirst) {
            isFirst = false;
            getRobotConcernList(false);
        }
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(getUserVisibleHint()&&isFirst){
//            isFirst = false;
//            getRobotConcernList(false);
//        }
//    }

    private void getRobotConcernList(boolean isRefresh) {
        if (!isRefresh) {
            showLoadingDialog("加载中");
        }
        Diting.searchConcern(new HttpJsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray response) {
                dismissLoadingDialog();
                mAdapter.clear();
                List<RobotConcern> list = Utils.parseConcern(response);
                if (list != null) {
                    ll_no_concern.setVisibility(View.GONE);
                    mAdapter.setData(list);
//                    robotConcernList.addAll(list);
                    mAdapter.notifyDataSetChanged();
//                    lv_concern.reflashComplete();
                    mSwipeRefreshWidget.setRefreshing(false);
                } else {
                    ll_no_concern.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                mSwipeRefreshWidget.setRefreshing(false);
                showShortToast("请求超时！");
            }
        });
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.add(1, 1001, 0, "取消关注");
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if(item.getGroupId() == 1) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//            showDialog(robotConcernList.get(info.position));
//        }
//        return super.onContextItemSelected(item);
//    }

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
                        //getRobotConcernList(true);
//                        EventBus.getDefault().post("update");
//                        robotConcernList.remove(robotConcern);
                        if (mAdapter != null) {
                            mAdapter.getRobotConcernList().remove(robotConcern);
                            if (mAdapter.getRobotConcernList().size() == 0)
                                ll_no_concern.setVisibility(View.VISIBLE);
                            else
                                ll_no_concern.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                            if (mFragments == null)
                                mFragments = new ArrayList<Fragment>();
                            mFragments = getFragmentManager().getFragments();
                            for (Fragment fragment : mFragments) {
                                if (fragment instanceof RankListFragment) {
                                    LogUtils.e(fragment.getTag() + ", " + fragment.getClass().toString());
                                    ((RankListFragment) fragment).getRobotRankByValue(false);
                                }
                            }
                        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String message) {
        if (message.equals("update")) {
            getRobotConcernList(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void addConcern(RobotConcern robotConcern) {
    }

    @Override
    public void canConcern(RobotConcern robotConcern) {
        showDialog(robotConcern);
    }

    @Override
    public void updateRemark(final RobotConcern robotConcern, final View view) {
        String remark = robotConcern.getRemark();
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setIsContent(true);
        myDialog.setTitle("添加备注");
//        myDialog.setContentLength(0);//设置备注信息的长度
        if (!Utils.isEmpty(remark)) {
            myDialog.setContentText(remark);
        } else {
            myDialog.setContentText(null);
        }
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
          /*      if (TextUtils.isEmpty(myDialog.getContentText())) {
                    myDialog.dismiss();
                    return;
                }*/
                showLoadingDialog("请求中");
                Diting.updateRemark(robotConcern.getPhone(), myDialog.getContentText(), new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        dismissLoadingDialog();
                        myDialog.dismiss();
                        robotConcern.setRemark(myDialog.getContentText());
                        mAdapter.notifyDataSetChanged();
                        showShortToast("请求成功");
                    }

                    @Override
                    public void onFailed(VolleyError error) {
                        dismissLoadingDialog();
                        myDialog.dismiss();
                        showShortToast("请求失败");
                    }
                });
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
    public void getDetail(RobotConcern robotConcern) {
//        startActivityForResult(RobotDetailActivity.getCallingIntent(getActivity(), robotConcern), REQUEST_CODE);
    }

    @Override
    public void onItemClick(RobotConcern robotConcern) {
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtra("robot", robotConcern);
//        startActivity(intent);
        // startActivityForResult(RobotDetailActivity.getCallingIntent(getActivity(), robotConcern), REQUEST_CODE);
        startActivity(NewChatActivity.callingIntent(getActivity(), robotConcern));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRobotConcernList(false);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void onRefresh() {
        getRobotConcernList(true);
    }
}
