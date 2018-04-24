package com.diting.pingxingren.smarteditor.presenter;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.net.ApiManager;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.smarteditor.net.observers.CodeResultObserver;
import com.diting.pingxingren.smarteditor.net.observers.QueryArticleObserver;
import com.diting.pingxingren.smarteditor.util.Constants;
import com.diting.pingxingren.smarteditor.util.EditorUtils;
import com.diting.pingxingren.smarteditor.view.EditorHomeView;
import com.diting.pingxingren.util.Utils;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 24, 18:38.
 * Description: .
 */

public class EditorHomePresenterImpl implements EditorHomePresenter {

    private EditorHomeView mView;
    private String mRobotId;
    public int mPageNo = 1;
    public String mTime;
    public String mTypeId;
    public String mStar;
    public boolean mIsNewData = false;

    public EditorHomePresenterImpl(EditorHomeView view, String robotId) {
        mView = view;
        mRobotId = robotId;
    }

    @Override
    public void start() {
        mIsNewData = false;
        mPageNo = 1;
        mTime = "";
        mTypeId = "";
        mStar = "";
        if (mView != null) mView.showLoadingView();
        getArticles(mPageNo, mTime, mTypeId, mStar);
    }

    @Override
    public void destroy() {
        if (mView != null) mView = null;
        if (Utils.isNotEmpty(mRobotId)) mRobotId = null;
    }

    @Override
    public void getArticles(int pageNo, String time, String typeId, String star) {
        mPageNo = pageNo;
        mTime = time;
        mTypeId = typeId;
        mStar = star;
        ApiManager.queryArticle(mPageNo, mRobotId, "", mTime, mTypeId, "0", mStar, new QueryArticleObserver(mResultCallBack));
    }

    @Override
    public void updateArticle(ArticleModel.ItemsBean itemsBean, int updateCode) {
        if (mView != null) mView.showLoadingView();
        switch (updateCode) {
            case Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE:
                ApiManager.updateArticle(String.valueOf(itemsBean.getId()), itemsBean.getTitle(), "",
                        "", "", new CodeResultObserver(
                                Constants.REQUEST_TYPE_UPDATE_ARTICLE_TITLE, mResultCallBack));
                break;
            case Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR:
                ApiManager.updateArticle(String.valueOf(itemsBean.getId()), "", itemsBean.getStar(),
                        "", "", new CodeResultObserver(
                                Constants.REQUEST_TYPE_UPDATE_ARTICLE_STAR, mResultCallBack));
                break;
            case Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY:
                ApiManager.updateArticle(String.valueOf(itemsBean.getId()), "", "",
                        "", itemsBean.getIec_id(), new CodeResultObserver(
                                Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY, mResultCallBack));
                break;
            case Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE:
                ApiManager.updateArticle(String.valueOf(itemsBean.getId()), "", "",
                        itemsBean.getDeletetype(), "", new CodeResultObserver(
                                Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE, mResultCallBack));
                break;
        }
    }

    @Override
    public void mobileArticleCategory(ArticleModel.ItemsBean itemsBean, String category) {
        itemsBean.getEditortype().setClassification(category);
        if (mView != null) mView.showLoadingView();
        ApiManager.updateArticle(String.valueOf(itemsBean.getId()), "", "",
                "", EditorUtils.getArticleCategory(category), new CodeResultObserver(
                        Constants.REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY, mResultCallBack));
    }

    @Override
    public void menuOperating(int operatingPosition, boolean isLeft) {
        if (operatingPosition != 6 && isLeft) {
            mIsNewData = true;
            if (mView != null) mView.showLoadingView();
        }

        switch (operatingPosition) {
            case 0:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "";
                    mStar = "";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("全部");
                } else {
                    if (mView != null) {
                        mView.showLoadingView();
                        mView.savePreferences("star");
                    }
                }
                break;
            case 1:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "我的工作";
                    mStar = "";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("我的工作");
                } else {
                    if (mView != null) {
                        mView.showLoadingView();
                        mView.savePreferences("time");
                    }
                }
                break;
            case 2:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "我的生活";
                    mStar = "";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("我的生活");
                }
                break;
            case 3:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "我的商务";
                    mStar = "";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("我的商务");
                }
                break;
            case 4:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "我的日记";
                    mStar = "";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("我的日记");
                }
                break;
            case 5:
                if (isLeft) {
                    mPageNo = 1;
                    mTime = "";
                    mTypeId = "";
                    mStar = "1";
                    getArticles(mPageNo, mTime, mTypeId, mStar);
                    if (mView != null) mView.updateLeftMenu("星标");
                }
                break;
            case 6:
                if (mView != null) mView.startToRecycleBin();
                break;
        }
    }

    @Override
    public void articleOperating(int operatingPosition) {
        switch (operatingPosition) {
            case 0:
                if (mView != null) mView.showUpdateTitleDialog();
                break;
            case 1:
                if (mView != null) mView.showMobileCategoryDialog();
                break;
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (mView != null) mView.dismissLoadingView();
            if (result instanceof ArticleModel) {
                ArticleModel articleModel = (ArticleModel) result;
                List<ArticleModel.ItemsBean> itemsBeans = articleModel.getItems();
                if (mView != null) mView.updateArticlesView(itemsBeans, mIsNewData && mPageNo == 1);

                if (mPageNo * 15 >= articleModel.getTotal()) {
                    if (mView != null) mView.loadEnd(true);
                }

                mPageNo++;
            } else if (result instanceof CodeResultModel) {
                if (mView != null) mView.showUpdateState((CodeResultModel) result);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            if (mView != null) mView.dismissLoadingView();
            if (o instanceof String) {
                if (mView != null) mView.showToast((String) o, false);
            } else if (o instanceof CodeResultModel) {
                if (mView != null) mView.showUpdateState((CodeResultModel) o);
            }
        }
    };
}
