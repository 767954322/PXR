package com.diting.pingxingren.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.InvalidQuestionAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.InvalidQuestionItemModel;
import com.diting.pingxingren.model.InvalidQuestionModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.InvalidQuestionObserver;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 未知问题
 * Created by asus on 2017/3/13.
 */

public class InvalidQuestionActivity extends BaseActivity implements InvalidQuestionAdapter.UnknowledgeListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {
    private TitleBarView titleBarView;
    private InvalidQuestionAdapter adapter;
    private List<InvalidQuestionItemModel> invalidQuestionList = new ArrayList<InvalidQuestionItemModel>();
    private LinearLayout ll_submenu;
    private int moreCount = 1;

    private View view;
    private  TextView textView;
    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_knowledge);
        initViews();
        initEvents();
        initData();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        onRefresh();
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("未知问题");
        titleBarView.setBtnRightText("万能答案");
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(UniversalAnswerActivity.class);
            }
        });
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
        ll_submenu =   findViewById(R.id.ll_submenu);
        ll_submenu.setVisibility(View.GONE);
        mSwipeRefreshWidget = findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new InvalidQuestionAdapter(R.layout.unknown_knowledge_item_new, invalidQuestionList);

          view = LayoutInflater.from(this).inflate(R.layout.empty, null);
          textView =   view.findViewById(R.id.tv_tip);
        if (Utils.isEmpty(MySharedPreferences.getUniversalAnswer1()) && Utils.isEmpty(MySharedPreferences.getUniversalAnswer2()) && Utils.isEmpty(MySharedPreferences.getUniversalAnswer3())) {
            textView.setText(R.string.unknown_tip_answer);
        } else {
            textView.setText(R.string.unknown_tip);
        }

        adapter.setEmptyView(view);

        mRecyclerView.setAdapter(adapter);


    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        adapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
        adapter.setListener(this);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        adapter.setOnItemClickListener(this);
    }
    //    public void getInvalidQuestion(final int pageNo) {
//
//        Diting.getInvalidQuestion(pageNo, new HttpCallBack() {
//            @Override
//            public void onSuccess(JSONObject response) {
//
//                try {
//                    if (pageNo * 15 >= response.getInt("total")) {
//                        adapter.loadMoreEnd(true);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                List<InvalidQuestion> list = Utils.parseInvalidQuestion(response);
//                moreCount++;
//                if (list == null) {
//                    return;
//                }
//                if (pageNo == 1) {
//                    adapter.setNewData(list);
//                } else {
//                    adapter.addData(list);
//                    adapter.loadMoreComplete();
//                }
//
//
//            }
//
//            @Override
//            public void onFailed(VolleyError error) {
//                if (pageNo == 1) {
//                    showShortToast("请求失败！");
//                } else {
//                    adapter.loadMoreFail();
//                }
//            }
//        });
//    }

    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getInvalidQuestionList(moreCount, new InvalidQuestionObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            if (result instanceof InvalidQuestionModel) {
                InvalidQuestionModel invalidQuestionModel = (InvalidQuestionModel) result;

                if (moreCount == 1) {
                    adapter.setNewData(invalidQuestionModel.getItems());
                } else {
                    adapter.addData(invalidQuestionModel.getItems());
                    adapter.loadMoreComplete();
                }

                if (moreCount * 15 >= invalidQuestionModel.getTotal()) {
                    adapter.loadMoreEnd(true);
                }

                moreCount++;
            }

        }

        @Override
        public void onResult(List<?> resultList) {

        }

        @Override
        public void onError(Object o) {

            if (o instanceof String)
                showShortToast((String) o);
        }
    };

    @Override
    public void onRefresh() {
        mSwipeRefreshWidget.setRefreshing(false);
        moreCount = 1;
        ApiManager.getInvalidQuestionList(moreCount, new InvalidQuestionObserver(mResultCallBack));
//        getInvalidQuestion(moreCount);
    }


    private void showDialog(final InvalidQuestionItemModel invalidQuestion) {

        final MyDialog myDialog = new MyDialog(this);
        myDialog.setTitle("温馨提示");
        myDialog.setMessage("确定要删除所选项目吗？");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
                showLoadingDialog("请求中");
                ApiManager.invalidQuestionDelete(invalidQuestion.getId(), new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            dismissLoadingDialog();
                            showShortToast(jsonObject.getString("message"));
//                            if (jsonObject.getString("message").equals("未知问题删除成功！")) {
                            dismissLoadingDialog();
                            showShortToast("删除成功");
//                                invalidQuestionList.remove(invalidQuestion);
                            adapter.remove(adapter.getData().indexOf(invalidQuestion));
                            adapter.notifyDataSetChanged();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {
                        dismissLoadingDialog();
                        if (o instanceof String)
                            showShortToast((String) o);

                    }
                });
//                Diting.deleteInvalidQuestion(invalidQuestion.getId() + "", new HttpCallBack() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//
//
////                        "message": "无效问题批量删除成功！"
//
//
//
//                        dismissLoadingDialog();
//                        showShortToast("删除成功");
//                        invalidQuestionList.remove(invalidQuestion);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//                        dismissLoadingDialog();
//                        showShortToast("删除失败");
//                    }
//                });
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
    public void editSuccess(String message) {
        if (message.equals("edit_success")) {
            onRefresh();
        }else if(message.equals("answer")){
            if (Utils.isEmpty(MySharedPreferences.getUniversalAnswer1()) && Utils.isEmpty(MySharedPreferences.getUniversalAnswer2()) && Utils.isEmpty(MySharedPreferences.getUniversalAnswer3())) {
                textView.setText(R.string.unknown_tip_answer);
            } else {
                textView.setText(R.string.unknown_tip);
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void editQuestion(String question) {
        Intent intent = new Intent(this, EditInvalidQuestionActivity.class);
        intent.putExtra("question", question);
        startActivity(intent);
    }

    @Override
    public void deleteKnowledge(InvalidQuestionItemModel invalidQuestion) {
        showDialog(invalidQuestion);
    }

    @Override
    public void editKnowledge(InvalidQuestionItemModel invalidQuestion) {
        editQuestion(invalidQuestion.getQuestion());
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, EditInvalidQuestionActivity.class);
        InvalidQuestionItemModel invalidQuestion = (InvalidQuestionItemModel) adapter.getItem(position);
        intent.putExtra("question", invalidQuestion.getQuestion());
        startActivity(intent);
    }
}
