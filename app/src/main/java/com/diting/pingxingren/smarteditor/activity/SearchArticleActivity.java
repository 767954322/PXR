package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.custom.ClearEditText;
import com.diting.pingxingren.databinding.ActivitySearchArticleBinding;
import com.diting.pingxingren.smarteditor.adapter.ArticleAdapter;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.dialog.ArticleDialog;
import com.diting.pingxingren.smarteditor.dialog.MobileCategoryDialog;
import com.diting.pingxingren.smarteditor.dialog.UpdateTitleDialog;
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
 * Date: 2018 - 02 - 02, 03:40.
 * Description: .
 */

public class SearchArticleActivity extends BaseActivity implements View.OnClickListener,
        ClearEditText.ClearListener, ClickListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SearchArticleActivity.class);
        return intent;
    }

    private ArticleModel.ItemsBean mItemsBean;
    private int mArticlePosition = -1;
    private ArticleDialog mArticleDialog;
    private UpdateTitleDialog mUpdateTitleDialog;
    private MobileCategoryDialog mMobileCategoryDialog;
    private UsualAlertDialog mStarUsualAlertDialog;
    private UsualAlertDialog mDeleteAlertDialog;

    private ActivitySearchArticleBinding mBinding;
    private int mPageNo = 1;
    private int mDeletePosition = -1;
    private String mQueryTitle;
    private ArticleAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_search_article);
        mBinding.ivBack.setOnClickListener(this);
        mBinding.search.setClearListener(this);
        mBinding.search.addTextChangedListener(mTextWatcher);
        mBinding.articleRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.articleRecycler.addItemDecoration(new RecyclerViewDivider(mContext, RecyclerViewDivider.HORIZONTAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mContext,
                ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mDeletePosition = viewHolder.getAdapterPosition();
                mItemsBean = mAdapter.getItem(mDeletePosition);
                showDeleteAlertDialog();
                mAdapter.notifyItemChanged(mDeletePosition);
            }
        });
        itemTouchHelper.attachToRecyclerView(mBinding.articleRecycler);
    }

    @Override
    protected void initData() {
        super.initData();
        List<ArticleModel.ItemsBean> itemsBeans = new ArrayList<>();
        mAdapter = new ArticleAdapter(itemsBeans, this);
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mBinding.articleRecycler);
        mBinding.articleRecycler.setAdapter(mAdapter);
    }

    public void showUpdateTitleDialog() {
        if (mUpdateTitleDialog == null) {
            mUpdateTitleDialog = new UpdateTitleDialog(mActivity, this);
        }

        mUpdateTitleDialog.setTitle(mItemsBean.getTitle());
        mUpdateTitleDialog.showDialog();
    }

    public void showMobileCategoryDialog() {
        if (mMobileCategoryDialog == null) {
            mMobileCategoryDialog = new MobileCategoryDialog(mActivity);
            mMobileCategoryDialog.setClickListener(this);
        }
        mMobileCategoryDialog.updateItems(mItemsBean.getEditortype().getClassification());
        mMobileCategoryDialog.showDialog();
    }

    public void showUpdateStarDialog() {
        if (mStarUsualAlertDialog == null) {
            mStarUsualAlertDialog = new UsualAlertDialog(mActivity);
            mStarUsualAlertDialog.setTitle(StringUtil.getString(R.string.tips));
            mStarUsualAlertDialog.setContent(StringUtil.getString(R.string.update_star_tips));
            mStarUsualAlertDialog.setCancelable(false);
            mStarUsualAlertDialog.setClickListener(this);
        }

        mStarUsualAlertDialog.showDialog();
    }

    public void showDeleteAlertDialog() {
        if (mDeleteAlertDialog == null) {
            mDeleteAlertDialog = new UsualAlertDialog(mActivity);
            mDeleteAlertDialog.setTitle(StringUtil.getString(R.string.tips));
            mDeleteAlertDialog.setContent(StringUtil.getString(R.string.deleted_article_tips));
            mDeleteAlertDialog.setCancelable(false);
            mDeleteAlertDialog.setClickListener(this);
        }

        mDeleteAlertDialog.showDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
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
                        if (mStarUsualAlertDialog != null && mStarUsualAlertDialog.isShowing()) {
                            mStarUsualAlertDialog.dismissDialog();
                            ApiManager.updateArticle(String.valueOf(mItemsBean.getId()), "", mItemsBean.getStar(),
                                    "", "", new CodeResultObserver(
                                            Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR, mResultCallBack));
                        } else if (mDeleteAlertDialog != null && mDeleteAlertDialog.isShowing()) {
                            mDeleteAlertDialog.dismissDialog();
                            mItemsBean.setDeletetype("1");
                            ApiManager.updateArticle(String.valueOf(mItemsBean.getId()), "", "",
                                    mItemsBean.getDeletetype(), "", new CodeResultObserver(
                                            Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE, mResultCallBack));
                        }
                    } else {
                        mUpdateTitleDialog.dismissDialog();
                        String oldTitle = mItemsBean.getTitle();
                        if (!s.equals(oldTitle)) {
                            mItemsBean.setTitle(s);
                            ApiManager.updateArticle(String.valueOf(mItemsBean.getId()), mItemsBean.getTitle(), "",
                                    "", "", new CodeResultObserver(
                                            Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE, mResultCallBack));
                        }
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
                }
                mArticleDialog.showDialog();
                break;
            case R.id.ivArticleStar:
                mItemsBean = (ArticleModel.ItemsBean) o;
                mArticlePosition = position;
                String star = mItemsBean.getStar();
                if (StringUtil.isNotEmpty(star) && star.equals("1")) {
                    mItemsBean.setStar("0");
                } else {
                    mItemsBean.setStar("1");
                }

                showUpdateStarDialog();
                break;
            case R.id.string:
                mArticleDialog.dismissDialog();
                if (o instanceof String) {
                    String s = (String) o;
                    if (s.equals("重命名")) {
                        showUpdateTitleDialog();
                    } else if (s.equals("移动分类")) {
                        showMobileCategoryDialog();
                    } else {
                        mMobileCategoryDialog.dismissDialog();
                        mobileArticleCategory(mItemsBean, s);
                    }
                }
                break;
        }
    }

    public void mobileArticleCategory(ArticleModel.ItemsBean itemsBean, String category) {
        int categoryType = 1;
        itemsBean.getEditortype().setClassification(category);

        if (category.equals("我的工作")) {
            categoryType = 1;
        } else if (category.equals("我的生活")) {
            categoryType = 2;
        } else if (category.equals("我的商务")) {
            categoryType = 3;
        } else if (category.equals("我的日记")) {
            categoryType = 4;
        }

        showLoading();
        ApiManager.updateArticle(String.valueOf(itemsBean.getId()), "", "",
                "", String.valueOf(categoryType), new CodeResultObserver(
                        Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY, mResultCallBack));
    }

    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            showLoading();
            ApiManager.queryArticle(mPageNo, MySharedPreferences.getUniqueId(), mQueryTitle,
                    "", "", "0", "", new QueryArticleObserver(mResultCallBack));
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                mQueryTitle = s.toString();
                showLoading();
                ApiManager.queryArticle(mPageNo, MySharedPreferences.getUniqueId(), mQueryTitle,
                        "", "", "0", "", new QueryArticleObserver(mResultCallBack));
            } else {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mBinding.tvNotData.setVisibility(View.VISIBLE);
            }
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
                    mBinding.tvNotData.setVisibility(View.GONE);
                    mAdapter.addData(itemsBeans);
                    if (mPageNo * 15 >= articleModel.getTotal()) {
                        mAdapter.loadMoreEnd(true);
                    }
                    mPageNo++;
                }

                if (mAdapter.getData().size() == 0) mBinding.tvNotData.setVisibility(View.VISIBLE);
            } else if (result instanceof CodeResultModel) {
                CodeResultModel codeResultModel = (CodeResultModel) result;
                String message = codeResultModel.getMessage();
                int type = codeResultModel.getRequestType();

                showShortToast(message);
                if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE) {
                    mAdapter.remove(mDeletePosition);
                    if (mAdapter.getData().size() == 0)
                        mBinding.tvNotData.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post("deleteRefreshArticle");
                } else if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE
                        || type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR
                        || type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY) {
                    if (mArticlePosition != -1) {
                        mAdapter.notifyItemChanged(mArticlePosition);
                        mArticlePosition = -1;
                    }
                }
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
                mAdapter.notifyItemChanged(mDeletePosition);
                mDeletePosition = -1;
            }
        }
    };

    @Override
    public void onClear() {
        mBinding.articleRecycler.stopScroll();
        mBinding.search.setText("");
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mBinding.tvNotData.setVisibility(View.VISIBLE);
        mPageNo = 1;
    }
}
