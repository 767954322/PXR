package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.NewChatActivity;
import com.diting.pingxingren.activity.RankActivity;
import com.diting.pingxingren.activity.SearchRobotActivity;
import com.diting.pingxingren.adapter.MyRankListAdapter;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.LoadListView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.HttpJsonArrayCallBack;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.diting.pingxingren.adapter.RobotRankAdapter.SORT_BY_FANS;
import static com.diting.pingxingren.adapter.RobotRankAdapter.SORT_BY_QUESTION;
import static com.diting.pingxingren.adapter.RobotRankAdapter.SORT_BY_VALUE;

/**
 * Created by asus on 2017/1/6.
 */

public class RankListFragment extends BaseFragment implements View.OnClickListener, MyRankListAdapter.AttentionListener, LoadListView.RLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private int sort_type;
    //    private LoadListView lv_rank;
//    private ValueRankAdapter mAdapter;
//    private ValueRankAdapter mSearchAdapter;
//    private ListView lv_search;
    // private ClearEditText searchView;
    private TextView searchView;
    //    private List<RobotConcern> robotRankList = new ArrayList<RobotConcern>();
//    private List<RobotConcern> searchRobotList = new ArrayList<RobotConcern>();
    private FrameLayout fl_rank_list;
    private LinearLayout ll_no_concern;
    private TextView tv_prompt;
    private TextView btn_sort;
    private RelativeLayout rl_search;
    private PopupWindow popupWindow;
    private boolean isViewCreated;
    private boolean isFirst = true;
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private static final int REQUEST_CODE = 1;

    private MyRankListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_concern, null);
        initViews(view);
        initEvents();
        isViewCreated = true;
        EventBus.getDefault().register(this);
        if (getActivity() instanceof RankActivity) {
            //getRobotByQuestion(false);
//            getRobotRankByValue(false);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && isFirst) {
            isFirst = false;
            //getRobotByQuestion(false);
//            getRobotRankByValue(false);
        }
    }
    private void initViews(View view) {
//        lv_rank = (LoadListView) view.findViewById(R.id.lv_concern);
//        lv_search = (ListView) view.findViewById(R.id.lv_search);
        searchView =  view.findViewById(R.id.search);
        fl_rank_list =   view.findViewById(R.id.fl_rank_list);
        ll_no_concern =   view.findViewById(R.id.ll_no_concern);
        tv_prompt =   view.findViewById(R.id.tv_prompt);
        rl_search =   view.findViewById(R.id.rl_search);
        rl_search.setVisibility(View.VISIBLE);
        btn_sort =   view.findViewById(R.id.btn_sort);
//        line = view.findViewById(R.id.line);
//        line.setVisibility(View.VISIBLE);
        tv_prompt.setText("没有找到机器人!");

        mSwipeRefreshWidget =   view.findViewById(R.id.swipe_refresh_widget_myRank);
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


    }

    private void initEvents() {
//        mAdapter = new MyRankListAdapter(getActivity(), R.layout.item_value_rank, robotRankList);


//        mSearchAdapter = new ValueRankAdapter(getActivity(), R.layout.item_value_rank, robotRankList);
        mAdapter.setListener(this);
//        mSearchAdapter.setListener(this);
        mAdapter.setItemClick(true);
//        mSearchAdapter.setItemClick(true);
//        lv_rank.setReflashInterface(this);
//        lv_rank.setInterface(new LoadListView.ILoadListener() {
//            @Override
//            public void onLoad() {
//                lv_rank.loadCompleted();
//            }
//        });
        if (getActivity() instanceof RankActivity) {
            mAdapter.setLogin(false);
//            mSearchAdapter.setLogin(false);
        }
        // searchAdapter.setSortType(SORT_BY_VALUE);
        btn_sort.setOnClickListener(this);
        searchView.setOnClickListener(this);
        mSwipeRefreshWidget.setOnRefreshListener(this);
//        lv_rank.setAdapter(mAdapter);
//        lv_search.setAdapter(mSearchAdapter);
//        //lv_rank.setOnScrollListener(onScrollListener);
//        lv_search.setOnScrollListener(onScrollListener);
        /*searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btn_sort.setEnabled(false);
                    searchRobot(s.toString());
                } else {
                    btn_sort.setEnabled(true);
                    lv_search.setVisibility(View.GONE);
                    fl_rank_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
//        lv_rank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RobotConcern robotConcern = robotRankList.get(position - 1);
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("robot", robotConcern);
//                startActivity(intent);
//            }
//        });
//        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RobotConcern robotConcern = searchRobotList.get(position);
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("robot", robotConcern);
//                startActivity(intent);
//            }
//        });
        //getRobotByQuestion(false);
    }
//
//    private void searchRobot(String newText) {
//        searchRobotList.clear();
//        fl_rank_list.setVisibility(View.GONE);
////        lv_search.setVisibility(View.VISIBLE);
//        Diting.cancelAll();
//        Diting.searchRobot(newText, new HttpJsonArrayCallBack() {
//            @Override
//            public void onSuccess(JSONArray response) {
//                List<RobotConcern> list = Utils.parseRank(response);
//                if (list != null) {
//                    ll_no_concern.setVisibility(View.GONE);
//                    searchRobotList.addAll(list);
////                    mSearchAdapter.notifyDataSetChanged();
//                } else {
////                    ll_no_concern.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
////                ll_no_concern.setVisibility(View.VISIBLE);
//            }
//        });
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isFirst) {
            isFirst = false;
            getRobotRankByValue(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        lv_rank.setFocusable(true);
//        lv_rank.setFocusableInTouchMode(true);
//        lv_rank.requestFocus();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser && isVisible()){
//            getRobotByQuestion();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

//    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            Utils.hideSoftInput(getActivity(), lv_search);
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//        }
//    };

    public void getRobotRankByValue(boolean isFromRefresh) {
//        btn_sort.setText(R.string.sort_by_fans);
        btn_sort.setText("价值排序");

        if (!isFromRefresh) {
            showLoadingDialog("加载中");
        }
        //adapter.setByValue(true);
        Diting.rankByValue(new HttpJsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray response) {
                sort_type = SORT_BY_VALUE;
                // mAdapter.setSortType(SORT_BY_VALUE);
//                robotRankList.clear();
                mAdapter.clear();
                List<RobotConcern> list = Utils.parseRank(response);
                if (list != null) {
//                    robotRankList.addAll(list);
                    mAdapter.setData(list);
                    mAdapter.notifyDataSetChanged();
//                    lv_rank.reflashComplete();
                    mSwipeRefreshWidget.setRefreshing(false);


                    dismissLoadingDialog();
                }

            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("请求失败");
                mSwipeRefreshWidget.setRefreshing(false);
            }
        });
    }

    private void getRobotRankByFans(boolean isFromRefresh) {
//        btn_sort.setText(R.string.sort_by_value);
        btn_sort.setText("粉丝排序");
        if (!isFromRefresh) {
            showLoadingDialog("加载中");
        }
        //adapter.setByValue(false);
        Diting.rankByFans(new HttpJsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray response) {
                sort_type = SORT_BY_FANS;
                // adapter.setSortType(SORT_BY_FANS);
                mAdapter.clear();
                List<RobotConcern> list = Utils.parseRank(response);
                if (list != null) {
                    mAdapter.setData(list);
//                    robotRankList.addAll(list);
                    mAdapter.notifyDataSetChanged();
//                    lv_rank.reflashComplete();
                    mSwipeRefreshWidget.setRefreshing(false);
                    dismissLoadingDialog();
                }

            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("请求失败");
                mSwipeRefreshWidget.setRefreshing(false);
            }
        });
    }

    private void getRobotByQuestion(boolean isFromRefresh) {
        btn_sort.setText("问答排序");
        if (!isFromRefresh) {
            showLoadingDialog("加载中");
        }
        //adapter.setByValue(false);
        Diting.rankByQuestion(new HttpJsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray response) {

                sort_type = SORT_BY_QUESTION;
                // adapter.setSortType(SORT_BY_QUESTION);
//                robotRankList.clear();
                mAdapter.clear();
                List<RobotConcern> list = Utils.parseRank(response);
                if (list != null) {
//                    robotRankList.addAll(list);
                    mAdapter.setData(list);
                    mAdapter.notifyDataSetChanged();
//                    lv_rank.reflashComplete();
                    mSwipeRefreshWidget.setRefreshing(false);
                    dismissLoadingDialog();
                }

            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("请求失败");
                mSwipeRefreshWidget.setRefreshing(false);
            }
        });
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
                        mSwipeRefreshWidget.setRefreshing(false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sort:
//                if(!isByValue){
//                    getRobotRankByValue();
//                }else {
//                    getRobotRankByFans();
//                }
                showSortMenu();
                break;
            case R.id.search:
                startActivity(SearchRobotActivity.class);
                break;
            default:
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            String sort_type = ((TextView) v).getText().toString();
            if (sort_type.equals("价值排序")) {
                getRobotRankByValue(false);
            }
            if (sort_type.equals("粉丝排序")) {
                getRobotRankByFans(false);
            }
            if (sort_type.equals("问答排序")) {
                getRobotByQuestion(false);
            }
        }
    };


    private void showSortMenu() {
        View popupView = View.inflate(getActivity(), R.layout.popupwin_sort, null);
        initPopupWindow(popupView);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//设置允许在外点击消失
        popupWindow.showAsDropDown(btn_sort, 0, -ScreenUtil.dip2px(getActivity(), 33));
    }

    private void initPopupWindow(View view) {
        TextView tv_memu1 = (TextView) view.findViewById(R.id.tv_menu1);
        TextView tv_memu2 = (TextView) view.findViewById(R.id.tv_menu2);
        TextView tv_memu3 = (TextView) view.findViewById(R.id.tv_menu3);
        switch (sort_type) {
            case SORT_BY_QUESTION:
                tv_memu1.setText(R.string.sort_by_question);
                tv_memu2.setText(R.string.sort_by_value);
                tv_memu3.setText(R.string.sort_by_fans);
                break;
            case SORT_BY_VALUE:
                tv_memu1.setText(R.string.sort_by_value);
                tv_memu2.setText(R.string.sort_by_question);
                tv_memu3.setText(R.string.sort_by_fans);
                break;
            case SORT_BY_FANS:
                tv_memu1.setText(R.string.sort_by_fans);
                tv_memu2.setText(R.string.sort_by_value);
                tv_memu3.setText(R.string.sort_by_question);
                break;
        }
        tv_memu1.setOnClickListener(onClickListener);
        tv_memu2.setOnClickListener(onClickListener);
        tv_memu3.setOnClickListener(onClickListener);
    }

//    private void hideSearchView() {
//        TranslateAnimation mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mHideAction.setDuration(500);
//        searchView.startAnimation(mHideAction);
//        searchView.setVisibility(View.GONE);
//        isSearchShow = false;
//        ll_shade.setVisibility(View.GONE);
//    }
//
//    private void showSearchView() {
//        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(500);
//        searchView.startAnimation(mShowAction);
//        searchView.setVisibility(View.VISIBLE);
//        isSearchShow = true;
//        ll_shade.setVisibility(View.VISIBLE);
//    }

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
//                            mSearchAdapter.notifyDataSetChanged();
                            EventBus.getDefault().post("update");
                            break;
                        case 1:
                            showShortToast("只能关注20个机器人");
                            break;
                        case 2:
                            showShortToast("已关注");
                            break;
                    }

//                    Utils.hideSoftInput(getActivity(), lv_search);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("关注失败");
//                Utils.hideSoftInput(getActivity(), lv_search);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRobotRankByValue(false);
                    }
                }, 500);
            }
        }
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
    public void onPause() {
        super.onPause();
//        Utils.hideSoftInput(getActivity(), lv_search);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(String message) {
        if (message.equals("update")) {
            getRobotRankByValue(true);
        }
    }
    @Override
    public void onRefresh() {
        switch (sort_type) {
            case SORT_BY_QUESTION:
                getRobotByQuestion(true);
                break;
            case SORT_BY_VALUE:
                getRobotRankByValue(true);
                break;
            case SORT_BY_FANS:
                getRobotRankByFans(true);
                break;
            default:
                break;
        }
    }
}
