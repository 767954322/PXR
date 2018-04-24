package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.databinding.ActivityRecycleBinBinding;
import com.diting.pingxingren.smarteditor.adapter.ArticleAdapter;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.dialog.ArticleDialog;
import com.diting.pingxingren.smarteditor.dialog.UsualAlertDialog;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.smarteditor.listener.SwipeToDeleteCallback;
import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.net.ApiManager;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.smarteditor.net.observers.CodeResultObserver;
import com.diting.pingxingren.smarteditor.net.observers.QueryArticleObserver;
import com.diting.pingxingren.smarteditor.util.Constants;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 30, 00:54.
 * Description: .
 */

public class RecycleBinActivity extends BaseActivity implements ClickListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecycleBinActivity.class);
        return intent;
    }

    public static Intent getCallIntent(Context context, Bundle bundle) {
        Intent intent = getCallIntent(context);
        if (bundle != null) intent.putExtras(bundle);
        return intent;
    }

    private ActivityRecycleBinBinding mBinding;
    private int mPageNo = 1;
    private ArticleAdapter mAdapter;
    private int mArticlePosition = -1;
    private int mDeletePosition = -1;
    private boolean mIsDelete = false;

    private ArticleModel.ItemsBean mItemsBean;
    private ArticleDialog mArticleDialog;
    private UsualAlertDialog mDeleteAlertDialog;
    private List<String> mMenuList;

    @Override
    protected void initView() {
        super.initView();
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_recycle_bin);
        mBinding.titleView.initTitle(true, "回收站", "", null);
        mBinding.articleRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.articleRecycler.addItemDecoration(new RecyclerViewDivider(mContext, RecyclerViewDivider.HORIZONTAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mContext,
                ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mDeletePosition = viewHolder.getAdapterPosition();
                mItemsBean = mAdapter.getData().get(mDeletePosition);
                mIsDelete = true;
                showDeleteAlertDialog();
                mAdapter.notifyItemChanged(mDeletePosition);
            }
        });
        itemTouchHelper.attachToRecyclerView(mBinding.articleRecycler);
    }

    @Override
    protected void initData() {
        super.initData();
        mMenuList = new ArrayList<>();
        mMenuList.add("还原文章");
        List<ArticleModel.ItemsBean> itemsBeans = new ArrayList<>();
        mAdapter = new ArticleAdapter(itemsBeans, this);
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mBinding.articleRecycler);
        mBinding.articleRecycler.setAdapter(mAdapter);
        showLoading();
        ApiManager.queryArticle(mPageNo, MySharedPreferences.getUniqueId(), "",
                "", "", "1", "", new QueryArticleObserver(mResultCallBack));
    }

    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            showLoading();
            ApiManager.queryArticle(mPageNo, MySharedPreferences.getUniqueId(), "",
                    "", "", "1", "", new QueryArticleObserver(mResultCallBack));
        }
    };

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            dismissLoading();
            if (result instanceof ArticleModel) {
                ArticleModel articleModel = (ArticleModel) result;
                List<ArticleModel.ItemsBean> itemsBeans = articleModel.getItems();
                if (itemsBeans.size() > 0) {
                    mAdapter.addData(itemsBeans);
                    if (mPageNo * 15 >= articleModel.getTotal()) {
                        mAdapter.loadMoreEnd(true);
                    }
                    mPageNo++;
                }

                if (mAdapter.getData().size() == 0) mBinding.tvNotData.setVisibility(View.VISIBLE);
            } else if (result instanceof CodeResultModel) {
                CodeResultModel codeResultModel = (CodeResultModel) result;
                showShortToast(mIsDelete ? codeResultModel.getMessage() : "以还原!");
                mAdapter.remove(mIsDelete ? mDeletePosition : mArticlePosition);

                if (mIsDelete) {
                    mDeletePosition = -1;
                } else {
                    mArticlePosition = -1;
                    EventBus.getDefault().post("reductionRefreshArticle");
                }

                if (mAdapter.getData().size() == 0)
                    mBinding.tvNotData.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            dismissLoading();
            if (o instanceof String) {
                showShortToast((String) o);
            } else if (o instanceof CodeResultModel) {
                CodeResultModel codeResultModel = (CodeResultModel) o;
                showShortToast(codeResultModel.getMessage());
                mAdapter.notifyItemChanged(mIsDelete ? mDeletePosition : mArticlePosition);
                if (mIsDelete)
                    mDeletePosition = -1;
                else
                    mArticlePosition = -1;
            }
        }
    };

    public void showDeleteAlertDialog() {
        if (mDeleteAlertDialog == null) {
            mDeleteAlertDialog = new UsualAlertDialog(mActivity);
            mDeleteAlertDialog.setTitle(StringUtil.getString(R.string.tips));
            mDeleteAlertDialog.setContent(StringUtil.getString(R.string.recycle_bin_deleted_article_tips));
            mDeleteAlertDialog.setCancelable(false);
            mDeleteAlertDialog.setClickListener(this);
        }

        mDeleteAlertDialog.showDialog();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
        }
    }

    @Override
    public void onClick(View view, Object o) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.articleView:
                mItemsBean = (ArticleModel.ItemsBean) o;
                startActivity(ArticleDetailsActivity.getCallIntent(mActivity, String.valueOf(mItemsBean.getId())));
                break;
            case R.id.bt_enter:
                if (o instanceof String) {
                    String s = (String) o;
                    if (s.equals("OK")) {
                        mDeleteAlertDialog.dismissDialog();
                        mItemsBean.setDeletetype("2");
                        ApiManager.updateArticle(String.valueOf(mItemsBean.getId()), "",
                                "", mItemsBean.getDeletetype(), "", new CodeResultObserver(
                                        Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE, mResultCallBack));
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view, Object o, int position) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.articleView:
                mItemsBean = (ArticleModel.ItemsBean) o;
                mArticlePosition = position;
                if (mArticleDialog == null) {
                    mArticleDialog = new ArticleDialog(mActivity);
                    mArticleDialog.setClickListener(this);
                    mArticleDialog.setNewMenu(mMenuList);
                }
                mArticleDialog.showDialog();
                break;
            case R.id.string:
                mArticleDialog.dismissDialog();
                if (o instanceof String) {
                    String s = (String) o;
                    if (s.equals("还原文章")) {
                        mIsDelete = false;
                        mItemsBean.setDeletetype("0");
                        ApiManager.updateArticle(String.valueOf(mItemsBean.getId()), "",
                                "", mItemsBean.getDeletetype(), "", new CodeResultObserver(
                                        Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE, mResultCallBack));
                    }
                }
                break;
        }
    }
}
