package com.diting.pingxingren.smarteditor.net.observers;

import com.diting.pingxingren.smarteditor.model.ArticleResultModel;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:59.
 * Description: .
 */

public class ArticleContentObserver extends DefaultObserver<ArticleResultModel> {

    public ArticleContentObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ArticleResultModel articleResultModel) {
        super.onNext(articleResultModel);
        mResultCallBack.onResult(articleResultModel);
    }
}
