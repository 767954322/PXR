package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.MailDetailActivity;
import com.diting.pingxingren.adapter.PersonalMailAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.model.PersonalMailItemModel;
import com.diting.pingxingren.model.PersonalMailModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.PersonalMessageObserver;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 私信
 * Created by asus on 2017/1/6.
 */

public class MailRobotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PersonalMailAdapter.PerMailDetailListener {

    private PersonalMailAdapter adapter;
    private List<PersonalMailItemModel> mailList = new ArrayList<>();

    private int moreCount = 1;


    //刷新布局
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View emptyView;
    private TextView tv_prompt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mail, null);
        initViews(view);
        initEvents();
        initDate();
        return view;
    }

    private void initDate() {
        onRefresh();
    }

    private void initViews(View view) {
        mSwipeRefreshWidget = view.findViewById(R.id.swipe_refresh_widget_sysmail);
        mRecyclerView = view.findViewById(R.id.recycle_view_sysmail);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PersonalMailAdapter(R.layout.mail_item, mailList);
        emptyView = getLayoutInflater().inflate(R.layout.empty, (ViewGroup) mRecyclerView.getParent(), false);
        tv_prompt = emptyView.findViewById(R.id.tv_tip);
        tv_prompt.setText(R.string.no_personal_date);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);

    }

    private void initEvents() {
        adapter.setOnLoadMoreListener(loadMoreListener, mRecyclerView);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        adapter.setMailDetailListener(this);


//        mailList = new ArrayList<Mail>();
//        adapter = new MailAdapter(getActivity(),R.layout.mail_item,mailList,MailAdapter.TYPE_ROBOT);
//        lv_mail.setAdapter(adapter);
//        lv_mail.setInterface(this);
//        lv_mail.setReflashInterface(this);
//        lv_mail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                editMailRobot(mailList.get(position - 1));
//            }
//        });
//        lv_mail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                showDialog(mailList.get(position - 1));
//                return true;
//            }
//        });
//        adapter.setOnClickListener(new MailAdapter.IOnClickListener() {
//            @Override
//            public void onClick(Mail mail) {
//                showDialog(mail);
//            }
//        });
    }

    private void showDialog(final PersonalMailItemModel mail) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("提示");
        myDialog.setMessage("确定要删除此条消息吗？");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                showLoadingDialog("请求中");


                ApiManager.personalMileDelete(mail.getId(), new ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        dismissLoadingDialog();
                        showShortToast("删除成功");
//                        mailList.remove(mail);
                        adapter.remove(adapter.getData().indexOf(mail));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onResult(List<?> resultList) {

                    }

                    @Override
                    public void onError(Object o) {
                        dismissLoadingDialog();
                        ToastUtils.showShortToastSafe("删除失败!");
                    }
                });

//
//                Diting.deleteRobotMail(mail.getMailId(), new HttpCallBack() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        dismissLoadingDialog();
//                        showShortToast("删除成功");
//                        mailList.remove(mail);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onFailed(VolleyError error) {
//                        dismissLoadingDialog();
//                        showShortToast("删除失败，请稍后再试");
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

    private void gotoDetail(PersonalMailItemModel mail) {

//        EventBus.getDefault().postSticky(mail);
        Intent intent = new Intent(getActivity(), MailDetailActivity.class);
        intent.putExtra("from", "MailRobotFragment");
        intent.putExtra("Personal", mail);
        startActivity(intent);
    }

    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
           ApiManager.getPersonalMessageList(MySharedPreferences.getRobotName(), moreCount, new PersonalMessageObserver(mResultCallBack));
        }
    };
    private ResultCallBack mResultCallBack = new ResultCallBack() {

        @Override
        public void onResult(Object result) {
            if (result instanceof PersonalMailModel) {
                PersonalMailModel personalMailModel = (PersonalMailModel) result;

                if (moreCount == 1) {
                    adapter.setNewData(personalMailModel.getItems());
                } else {
                    adapter.addData(personalMailModel.getItems());
                    adapter.loadMoreComplete();
                }

                if (moreCount * 15 >= personalMailModel.getTotal()) {
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
            dismissLoadingDialog();
            ToastUtils.showShortToastSafe("请求失败!");
        }
    };

    private void editMailRobot(final PersonalMailItemModel mail) {

        ApiManager.personalMailIsRead(mail.getId(), new ResultCallBack() {
            @Override
            public void onResult(Object result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result.toString());
//                    dismissLoadingDialog();
//                    LogUtils.e("=======+" + "保存成功"+jsonObject.toString());
//                    if (jsonObject.getString("message").equals("保存成功！")) {
//                        mail.setRead(true);
                        EventBus.getDefault().post("readed");
                        if (--MyApplication.unreadMessageNum <= 0) {
                            EventBus.getDefault().post("NoRobotMail");
                        }
                        if (!Utils.hasUnreadMail()) {
                            EventBus.getDefault().post("hideRedPoint");
                        }

                        mail.setIdRead(true);
                        adapter.setShowPoint(true);
                        adapter.notifyDataSetChanged();


//                    }
                    gotoDetail(mail);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onResult(List<?> resultList) {

            }

            @Override
            public void onError(Object o) {
                if (o instanceof String)
                    showShortToast((String) o);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshWidget.setRefreshing(false);
        moreCount = 1;
        ApiManager.getPersonalMessageList(MySharedPreferences.getRobotName(), moreCount, new PersonalMessageObserver(mResultCallBack));
    }

    @Override
    public void gotoPerMailDetail(PersonalMailItemModel personalMailItemModel) {
        editMailRobot(personalMailItemModel);
    }

    @Override
    public void deletePerMail(PersonalMailItemModel personalMailItemModel) {
        showDialog(personalMailItemModel);
    }
}
