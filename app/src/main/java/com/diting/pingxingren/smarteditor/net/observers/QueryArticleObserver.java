package com.diting.pingxingren.smarteditor.net.observers;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:59.
 * Description: .
 */

public class QueryArticleObserver extends DefaultObserver<ArticleModel> {
    public QueryArticleObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ArticleModel articleModel) {
        super.onNext(articleModel);
        mResultCallBack.onResult(articleModel);
    }
}
