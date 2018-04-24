package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.databinding.ActivityEditorHomeBinding;
import com.diting.pingxingren.smarteditor.adapter.ArticleAdapter;
import com.diting.pingxingren.smarteditor.adapter.ArticleMonthAdapter;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.dialog.ArticleDialog;
import com.diting.pingxingren.smarteditor.dialog.MobileCategoryDialog;
import com.diting.pingxingren.smarteditor.dialog.UpdateTitleDialog;
import com.diting.pingxingren.smarteditor.dialog.UsualAlertDialog;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.smarteditor.listener.SwipeToDeleteCallback;
import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.presenter.EditorHomePresenterImpl;
import com.diting.pingxingren.smarteditor.sort.StarSort;
import com.diting.pingxingren.smarteditor.sort.TimeSort;
import com.diting.pingxingren.smarteditor.util.Constants;
import com.diting.pingxingren.smarteditor.view.EditorHomeView;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.StringUtil;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 08, 15:52.
 * Description: 智能小编首页.
 */

public class EditorHomeActivity extends BaseActivity implements EditorHomeView, ClickListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, EditorHomeActivity.class);
        return intent;
    }

    private ActivityEditorHomeBinding mBinding;
    private ArticleAdapter mAdapter;
    private List<ArticleModel.ItemsBean> mArticles;
    private EditorHomePresenterImpl mPresenter;
    private TopRightMenu mTopLeftMenu;
    private TopRightMenu mTopRightMenu;
    private ArticleDialog mArticleDialog;
    private UpdateTitleDialog mUpdateTitleDialog;
    private MobileCategoryDialog mMobileCategoryDialog;
    private UsualAlertDialog mStarUsualAlertDialog;
    private UsualAlertDialog mDeleteAlertDialog;
    private ArticleModel.ItemsBean mItemsBean;
    private int mArticlePosition = -1;
    private String mSort;
    private EditorHomeActivityHandler mHandler;
    private Drawable mDrawableUp;
    private Drawable mDrawableDown;
    private int mDeletePosition = -1;

    @Override
    protected void initView() {
        super.initView();
        mSort = MySharedPreferences.getEditorSort();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_editor_home);
        mBinding.setClick(this);
        mBinding.monthRecycler.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBinding.monthRecycler.addItemDecoration(new RecyclerViewDivider(mContext, RecyclerViewDivider.VERTICAL));
        mBinding.articleRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.articleRecycler.addItemDecoration(new RecyclerViewDivider(mContext, RecyclerViewDivider.HORIZONTAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mContext,
                ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mDeletePosition = viewHolder.getAdapterPosition();
                mItemsBean = mAdapter.getItem(mDeletePosition);
                mAdapter.notifyItemChanged(mDeletePosition);
                showDeleteAlertDialog();
            }
        });
        itemTouchHelper.attachToRecyclerView(mBinding.articleRecycler);
        initTopLeftMenu();
        initTopRightMenu();
    }

    @Override
    protected void initData() {
        super.initData();
        mDrawableUp = ContextCompat.getDrawable(mContext, R.drawable.ic_sort_up);
        mDrawableDown = ContextCompat.getDrawable(mContext, R.drawable.ic_sort_down);
        mHandler = new EditorHomeActivityHandler(this);
        EventBus.getDefault().register(mActivity);
        mArticles = new ArrayList<>();
        String[] articleStrings = mResources.getStringArray(R.array.article_months);
        ArticleMonthAdapter monthAdapter = new ArticleMonthAdapter(Arrays.asList(articleStrings), this);
        mBinding.monthRecycler.setAdapter(monthAdapter);
        mAdapter = new ArticleAdapter(mArticles, this);
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mBinding.articleRecycler);
        mBinding.articleRecycler.setAdapter(mAdapter);
        mPresenter = new EditorHomePresenterImpl(this, MySharedPreferences.getUniqueId());
        mPresenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.destroy();
        EventBus.getDefault().unregister(mActivity);
    }

    private void initTopLeftMenu() {
        mTopLeftMenu = new TopRightMenu(mActivity, -1, R.layout.item_popup_menu);
        mTopLeftMenu.setMenuDismissListener(mLeftMenuDismissListener);

        mTopLeftMenu.addMenuItem(new MenuItem("全部"))
                .addMenuItem(new MenuItem("我的工作"))
                .addMenuItem(new MenuItem("我的生活"))
                .addMenuItem(new MenuItem("我的商务"))
                .addMenuItem(new MenuItem("我的日记"))
                .addMenuItem(new MenuItem("星标"))
                .addMenuItem(new MenuItem("回收站"));
        mTopLeftMenu
                .setHeight(ScreenUtil.dip2px(mContext, 320))     //默认高度480
                .showIcon(false)            //显示菜单图标，默认为true
                .showRightIcon(false)       //显示右侧菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .setOnMenuItemClickListener(mOnLeftMenuClickListener);
    }

    private void initTopRightMenu() {
        mTopRightMenu = new TopRightMenu(mActivity, -1, R.layout.item_popup_menu);
        mTopRightMenu.addMenuItem(new MenuItem(0, "置顶星标文章", R.drawable.ic_checkmark))
                .addMenuItem(new MenuItem(0, "已创建时间排序", R.drawable.ic_checkmark));
        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(mContext, 100))     //默认高度480
                .showIcon(false)     //显示菜单图标，默认为true
                .showRightIcon(true)   //显示右侧菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .setOnMenuItemClickListener(mOnRightMenuClickListener);
        mTopRightMenu.setRightShowPosition(mSort.endsWith("star") ? 0 : 1);
    }

    private void showTopMenu(boolean isLeft) {
        if (isLeft)
            mTopLeftMenu.showAsDropDown(mBinding.tvType);
        else
            mTopRightMenu.showAsDropDown(mBinding.btMultifunction, -250, 5);
    }

    private BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mPresenter.getArticles(mPresenter.mPageNo, mPresenter.mTime, mPresenter.mTypeId, mPresenter.mStar);
        }
    };

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.tvType:
                mBinding.tvType.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableUp, null);
                showTopMenu(true);
                break;
            case R.id.btSearch:
                startActivity(SearchArticleActivity.getCallIntent(mContext));
                break;
            case R.id.btMultifunction:
                showTopMenu(false);
                break;
            case R.id.addArticle:
                startActivity(ArticleActivity.getCallIntent(this, true, "", mBinding.tvType.getText().toString()));
                break;
        }
    }

    @Override
    public void onClick(View view, Object o) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.viewMonth:
                showLoadingView();
                updateLeftMenu("全部");
                mPresenter.mIsNewData = true;
                mPresenter.getArticles(1, (String) o, "", "");
                break;
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
                            if (mPresenter != null)
                                mPresenter.updateArticle(mItemsBean, Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR);
                        } else if (mDeleteAlertDialog != null && mDeleteAlertDialog.isShowing()) {
                            mDeleteAlertDialog.dismissDialog();
                            mItemsBean.setDeletetype("1");
                            if (mPresenter != null)
                                mPresenter.updateArticle(mItemsBean, Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE);
                        }
                    } else {
                        mUpdateTitleDialog.dismissDialog();
                        String oldTitle = mItemsBean.getTitle();
                        if (!s.equals(oldTitle)) {
                            mItemsBean.setTitle(s);
                            if (mPresenter != null)
                                mPresenter.updateArticle(mItemsBean, Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE);
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
                        mPresenter.articleOperating(0);
                    } else if (s.equals("移动分类")) {
                        mPresenter.articleOperating(1);
                    } else {
                        mMobileCategoryDialog.dismissDialog();
                        mPresenter.mobileArticleCategory(mItemsBean, s);
                    }
                }
                break;
        }
    }

    private TopRightMenu.MenuDismissListener mLeftMenuDismissListener = new TopRightMenu.MenuDismissListener() {
        @Override
        public void onDismiss() {
            mBinding.tvType.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableDown, null);
        }
    };

    private TopRightMenu.OnMenuItemClickListener mOnLeftMenuClickListener = new TopRightMenu.OnMenuItemClickListener() {
        @Override
        public void onMenuItemClick(int position) {
            mPresenter.menuOperating(position, true);
        }
    };

    private TopRightMenu.OnMenuItemClickListener mOnRightMenuClickListener = new TopRightMenu.OnMenuItemClickListener() {
        @Override
        public void onMenuItemClick(int position) {
            mPresenter.menuOperating(position, false);
        }
    };

    @Override
    public void updateArticlesView(List<ArticleModel.ItemsBean> itemsBeans, boolean setNewData) {
        if (!setNewData) {
            mAdapter.addData(itemsBeans);
            mArticles = mAdapter.getData();
            Collections.sort(mArticles, mSort.equals("star") ? new StarSort() : new TimeSort());
            mAdapter.setNewData(mArticles);
        } else {
            Collections.sort(itemsBeans, mSort.equals("star") ? new StarSort() : new TimeSort());
            mAdapter.setNewData(itemsBeans);
        }

        mBinding.tvNotData.setVisibility(mAdapter.getData().size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void loadEnd(boolean end) {
        mAdapter.loadMoreEnd(end);
    }

    @Override
    public void savePreferences(String value) {
        dismissLoadingView();
        mSort = value;
        mTopRightMenu.setRightShowPosition(mSort.endsWith("star") ? 0 : 1);
        MySharedPreferences.setEditorSort(mSort);
        mArticles = mAdapter.getData();
        Collections.sort(mArticles, mSort.equals("star") ? new StarSort() : new TimeSort());
        mAdapter.setNewData(mArticles);
        mBinding.articleRecycler.smoothScrollToPosition(0);
    }

    @Override
    public void updateLeftMenu(String menuTitle) {
        if (StringUtil.isNotEmpty(menuTitle)
                && !mBinding.tvType.getText().equals(menuTitle)) mBinding.tvType.setText(menuTitle);
    }

    @Override
    public void startToRecycleBin() {
        startActivity(RecycleBinActivity.getCallIntent(mActivity));
    }

    @Override
    public void showUpdateState(CodeResultModel codeResultModel) {
        String code = codeResultModel.getCode();
        String message = codeResultModel.getMessage();
        int type = codeResultModel.getRequestType();

        if (code.equals("1")) {
            showToast(message, false);
            if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE) {
                mAdapter.remove(mDeletePosition);
                if (mAdapter.getData().size() == 0)
                    mBinding.tvNotData.setVisibility(View.VISIBLE);
            } else if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE
                    || type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR
                    || type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY) {
                if (mArticlePosition != -1) {
                    mAdapter.notifyItemChanged(mArticlePosition);
                }

                if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR && mSort.equals("star")) {
                    mArticles = mAdapter.getData();
                    Collections.sort(mArticles, new StarSort());
                    mAdapter.setNewData(mArticles);
                } else if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY) {
                    mAdapter.remove(mArticlePosition);
                }

                mArticlePosition = -1;
            }
        } else {
            showToast(message, false);
            if (type == Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE) {
                mAdapter.notifyItemChanged(mDeletePosition);
                mDeletePosition = -1;
            }
        }
    }

    @Override
    public void showUpdateTitleDialog() {
        if (mUpdateTitleDialog == null) {
            mUpdateTitleDialog = new UpdateTitleDialog(mActivity, this);
        }

        mUpdateTitleDialog.setTitle(mItemsBean.getTitle());
        mUpdateTitleDialog.showDialog();
    }

    @Override
    public void showMobileCategoryDialog() {
        if (mMobileCategoryDialog == null) {
            mMobileCategoryDialog = new MobileCategoryDialog(mActivity);
            mMobileCategoryDialog.setClickListener(this);
        }
        mMobileCategoryDialog.updateItems(mItemsBean.getEditortype().getClassification());
        mMobileCategoryDialog.showDialog();
    }

    @Override
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

    @Override
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
    public void showToast(String toastText, boolean isLong) {
        if (isLong)
            showLongToast(toastText);
        else
            showShortToast(toastText);
    }

    @Override
    public void showLoadingView() {
        mHandler.sendEmptyMessage(HANDLER_WHAT_SHOW_LOADING);
    }

    @Override
    public void dismissLoadingView() {
        mHandler.sendEmptyMessage(HANDLER_WHAT_DISMISS_LOADING);
    }

    private static final int HANDLER_WHAT_SHOW_LOADING = 0x001;
    private static final int HANDLER_WHAT_DISMISS_LOADING = 0x002;

    private static class EditorHomeActivityHandler extends Handler {
        private WeakReference<EditorHomeActivity> mActivityWeakReference;

        public EditorHomeActivityHandler(EditorHomeActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditorHomeActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLER_WHAT_SHOW_LOADING:
                        activity.showLoading();
                        break;
                    case HANDLER_WHAT_DISMISS_LOADING:
                        activity.dismissLoading();
                        break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("RefreshArticle") || message.equals("reductionRefreshArticle") || message.equals("deleteRefreshArticle")) {
            mPresenter.mIsNewData = true;
            if (message.equals("RefreshArticle"))
                MySharedPreferences.clearNotEditContent();
            mPresenter.getArticles(1, mPresenter.mTime, mPresenter.mTypeId, mPresenter.mStar);
        }
    }
}
