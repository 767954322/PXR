package com.diting.pingxingren.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.volley.VolleyError;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.SearchRobotAdapter;
import com.diting.pingxingren.adapter.ValueRankAdapter;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.ClearEditText;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.databinding.ActivitySearchRobotBinding;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.HttpJsonArrayCallBack;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 14, 18:09.
 * Description: 搜索机器人.
 */

public class SearchRobotActivity extends BaseActivity implements ValueRankAdapter.AttentionListener, ClearEditText.ClearListener {

    private ActivitySearchRobotBinding mBinding;
    private ArrayList<RobotConcern> searchRobotList;
    private SearchRobotAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_robot);

        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        searchRobotList = new ArrayList<>();
        mSearchAdapter = new SearchRobotAdapter();
        mSearchAdapter.setRobotConcerns(searchRobotList);
        mSearchAdapter.setItemClick(true);
        mSearchAdapter.setListener(this);
        mSearchAdapter.setRemark(false);
        mBinding.robotRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.robotRecyclerView.setAdapter(mSearchAdapter);
        mBinding.robotRecyclerView.setVisibility(View.GONE);
        mBinding.llNoRobot.setVisibility(View.GONE);
    }

    @Override
    protected void initEvents() {
        /*mBinding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchRobot(s.toString());
            }
        });*/
        mBinding.search.setClearListener(this);
        mBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRobot(mBinding.search.getText().toString());
            }
        });
    }

    private void searchRobot(String keywords) {
        if (Utils.isEmpty(keywords) || keywords.trim().length() == 0) {
            showShortToast("请输入你要查找的机器人名称");
            return;
        }

        mBinding.search.clearFocus();
        Utils.hideSoftInput(this, mBinding.search);
        searchRobotList.clear();
        Diting.cancelAll();
        showLoadingDialog("查找中...");
        Diting.searchRobot(keywords, new HttpJsonArrayCallBack() {
            @Override
            public void onSuccess(JSONArray response) {

                dismissLoadingDialog();
                List<RobotConcern> list = Utils.parseRank(response);
                if (list != null && list.size() > 0) {
                    searchRobotList.addAll(list);
                    mBinding.robotRecyclerView.setVisibility(View.VISIBLE);
                    mSearchAdapter.setRobotConcerns(searchRobotList);
                    mBinding.llNoRobot.setVisibility(View.GONE);
                } else {
                    mBinding.robotRecyclerView.setVisibility(View.GONE);
                    mBinding.llNoRobot.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                ToastUtils.showShortToast(error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                            mSearchAdapter.notifyDataSetChanged();
                            EventBus.getDefault().post("update");
                            break;
                        case 1:
                            showShortToast("只能关注20个机器人");
                            break;
                        case 2:
                            showShortToast("已关注");
                            break;
                    }

                    Utils.hideSoftInput(SearchRobotActivity.this, mBinding.search);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                dismissLoadingDialog();
                showShortToast("关注失败");
                Utils.hideSoftInput(SearchRobotActivity.this, mBinding.search);
            }
        });
    }

    @Override
    public void canConcern(RobotConcern robotConcern) {
        showDialog(robotConcern);
    }
    private void showDialog(final RobotConcern robotConcern){
        final MyDialog myDialog = new MyDialog(this);
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
                        mSearchAdapter.notifyDataSetChanged();
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
    public void updateRemark(RobotConcern robotConcern, View view) {
    }

    @Override
    public void getDetail(RobotConcern robotConcern) {
//        startActivityForResult(RobotDetailActivity.getCallingIntent(this, robotConcern), Constants.REQUEST_CODE);
    }

    @Override
    public void onItemClick(RobotConcern robotConcern) {
        startActivity(NewChatActivity.callingIntent(this, robotConcern));
    }

    @Override
    public void onClear() {
        mBinding.robotRecyclerView.stopScroll();
        mBinding.search.setText("");
        searchRobotList.clear();
        mBinding.robotRecyclerView.setVisibility(View.GONE);
        mBinding.llNoRobot.setVisibility(View.VISIBLE);
    }
}
