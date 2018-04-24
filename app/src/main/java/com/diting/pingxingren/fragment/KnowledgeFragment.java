package com.diting.pingxingren.fragment;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.AddKnowledgeActivity;
import com.diting.pingxingren.activity.AskQuestionActivity;
import com.diting.pingxingren.adapter.KnowledgeAdapter;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.BadgeTextView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.databinding.FragmentKnowledgeBinding;
import com.diting.pingxingren.dialog.DialogUtil;
import com.diting.pingxingren.entity.CommonLanguage;
import com.diting.pingxingren.model.AskCountModel;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.KnowledgeItemModel;
import com.diting.pingxingren.model.KnowledgeModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.AskCountObserver;
import com.diting.pingxingren.net.observers.KnowledgeObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.UmengUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/2/22.
 */

public class KnowledgeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, KnowledgeAdapter.ItemClickListener {

    private FragmentKnowledgeBinding mBinding;
    private KnowledgeAdapter adapter;
    private List<KnowledgeItemModel> knowledgeList = new ArrayList<>();
    private int count = 1;
    private CommonLanguage mCommonLanguage;
    private KnowledgeItemModel mKnowledgeItemModel;
    private String mShiro = "0";
    private Dialog mLanguagePromptDialog;
    private BadgeTextView badgeTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_knowledge, container, false);
            initViews();
            initEvents();
            initData();
        }
        return mBinding.getRoot();
    }

    private void initTitleBarView() {
        mBinding.titleBar.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
        mBinding.titleBar.setBtnLeft(R.mipmap.icon_back, null);
        mBinding.titleBar.setTitleText(MySharedPreferences.getRobotName() + "的内容库");
        mBinding.titleBar.setBtnRightText("会问");
        mBinding.titleBar.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_EDITOR_ASK);
                startActivity(AskQuestionActivity.class);
            }
        });
    }

    private void initViews() {
        initTitleBarView();
        initBageTextView();
    }

    private void initEvents() {
        mBinding.llAddKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), UmengUtil.EVENT_CLICK_NUM_OF_EDITOR_ADD_CONTENT);
                startActivity(AddKnowledgeActivity.class);
            }
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new KnowledgeAdapter(R.layout.knowledge_item_new, knowledgeList);
        mBinding.recyclerView.setAdapter(adapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.empty, null);
        TextView textView = view.findViewById(R.id.tv_tip);
        textView.setText(R.string.tv_no_knowledge);
        adapter.setEmptyView(view);
        adapter.setOnLoadMoreListener(loadMoreListener, mBinding.recyclerView);
        adapter.setListener(this);
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        ApiManager.getAskCount(new AskCountObserver(mResultCallBack));
        onRefresh();
    }

    private void initBageTextView() {

        badgeTextView = new BadgeTextView(getActivity());
        badgeTextView.setTargetView(mBinding.titleBar.getBtnRight());
        badgeTextView.setBadgeMargin(0, 2, 5, 0);
//        badgeTextView.setBadgeCount(2);
//        badgeTextView.setText(" ");
    }

    private BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            ApiManager.getKnowledge(count, new KnowledgeObserver(mResultCallBack));
        }
    };

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {

            if (result instanceof KnowledgeModel) {
                KnowledgeModel knowledgeModel = (KnowledgeModel) result;

                if (count == 1) {
                    adapter.setNewData(knowledgeModel.getItems());
                } else {
                    adapter.addData(knowledgeModel.getItems());
                    adapter.loadMoreComplete();
                }

                if (count * 15 >= knowledgeModel.getTotal()) {
                    adapter.loadMoreEnd(true);
                }

                mBinding.recyclerView.smoothScrollToPosition(0);
                count++;
            } else if (result instanceof String) {
                String s = (String) result;
                showShortToast(s);
                if (s.equals("知识删除成功.")) {
                    dismissLoadingDialog();
                    adapter.remove(adapter.getData().indexOf(mKnowledgeItemModel));
//                    CommonLanguageListModel commonLanguageModel=new CommonLanguageListModel();
//                    commonLanguageModel.setId(mKnowledgeItemModel.getId());
//                    commonLanguageModel.setQuestion(mKnowledgeItemModel.getQuestion());
//
//                    MyApplication.sCommonLanguages.remove(commonLanguageModel);
                } else if (s.equals("推荐成功！")) {
                    dismissLoadingDialog();
                    showShortToast("添加成功.");
                    adapter.notifyDataSetChanged();

                }
            } else if (result instanceof CommonLanguageListModel) {
                dismissLoadingDialog();
                showShortToast("添加成功.");
                MyApplication.sCommonLanguages.add((CommonLanguageListModel) result);
                adapter.notifyDataSetChanged();
            } else if (result instanceof AskCountModel) {
                AskCountModel askCountModel = (AskCountModel) result;
                if (askCountModel.getCount() > 0) {
                    badgeTextView.setBadgeCount(askCountModel.getCount());
                } else {
                    badgeTextView.setVisibility(View.GONE);
                }
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void addSuccess(String message) {
        if (message.equals("add_success")) {
            onRefresh();
            ApiManager.getAskCount(new AskCountObserver(mResultCallBack));
        } else if (message.equals("chooseChild")) {
            count = 1;
            mBinding.titleBar.setTitleText(MySharedPreferences.getRobotName() + "的内容库");
            ApiManager.getKnowledge(count, new KnowledgeObserver(mResultCallBack));
        } else if (message.equals("refreshLanguages")) {
            adapter.notifyDataSetChanged();
            onRefresh();
        } else if (message.equals("updateSuccess")) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mBinding.swipeRefreshLayout.setRefreshing(false);
        count = 1;
        ApiManager.getKnowledge(count, new KnowledgeObserver(mResultCallBack));
    }

    private void showDialog(KnowledgeItemModel knowledge) {
        mKnowledgeItemModel = knowledge;
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.setTitle("温馨提示");
        myDialog.setMessage("确定要删除所选项目吗？");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                myDialog.dismiss();
                showLoadingDialog("请求中");
                ApiManager.deleteKnowledge(mKnowledgeItemModel.getId(), new ResultMessageObserver(mResultCallBack));
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
    public void deleteKnowledge(KnowledgeItemModel knowledgeItem) {
        showDialog(knowledgeItem);
    }

    @Override
    public void editKnowledge(KnowledgeItemModel knowledgeItem) {
        startActivity(AddKnowledgeActivity.callingIntent(getActivity(), knowledgeItem));
    }

    @Override
    public void addCommonLanguage(KnowledgeItemModel knowledgeItemModel) {
        showLoadingDialog("正在添加....");
//        ApiManager.saveOrDeleteCommonLanguage(true, knowledgeItemModel.getQuestion(), mShiro,
//                knowledgeItemModel.getId(), new CommonLanguageObserver(mResultCallBack));
        mKnowledgeItemModel = knowledgeItemModel;
        if ("0".equals(mShiro)) {
            ApiManager.addOrDeleteCommonLanguage(knowledgeItemModel.getId(), true, false, AddmResultCallBack);
        } else if ("1".equals(mShiro)) {
            ApiManager.addOrDeleteCommonLanguage(knowledgeItemModel.getId(), true, true, AddmResultCallBack);
        }
    }

    @Override
    public void showLanguagePrompt() {
        if (mLanguagePromptDialog == null) {
            mLanguagePromptDialog = DialogUtil.createDialog(getActivity(),
                    R.layout.layout_language_prompt, R.style.CustomDialog);
            mLanguagePromptDialog.setCancelable(true);
            Window dialogWindow = mLanguagePromptDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = (int) (ScreenUtil.getScreenWidth(getActivity()) * 0.8);
                layoutParams.height = (int) (layoutParams.width / 3.5);
                dialogWindow.setAttributes(layoutParams);
            }
            RadioGroup radioGroup = mLanguagePromptDialog.findViewById(R.id.languageRadioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    mLanguagePromptDialog.dismiss();
                    if (checkedId == R.id.languageEveryone) {
                        mShiro = "0";
                    } else {
                        mShiro = "1";
                    }
                }
            });
        }

        mLanguagePromptDialog.show();
    }


    private ResultCallBack AddmResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                String message = jsonObject.getString("message");
                if (message.equals("推荐成功！")) {
                    dismissLoadingDialog();
                    showShortToast("添加成功.");
                    EventBus.getDefault().post("getCommonLanguage");
                    mKnowledgeItemModel.setAddCommon(true);
                    LogUtils.e("mKnowledgeItemModel"+mKnowledgeItemModel.toString());
                    CommonLanguageListModel commonLanguageModel=new CommonLanguageListModel();
                    commonLanguageModel.setId(mKnowledgeItemModel.getId());
                    commonLanguageModel.setQuestion(mKnowledgeItemModel.getQuestion());
                    MyApplication.sCommonLanguages.add(commonLanguageModel);
                    adapter.setHighFrequencyQuestion(false);
                    adapter.notifyDataSetChanged();
                }

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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
