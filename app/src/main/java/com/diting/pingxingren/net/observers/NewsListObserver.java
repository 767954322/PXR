package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.NewsListModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

public class NewsListObserver extends DefaultObserver<List<NewsListModel>> {

    public NewsListObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<NewsListModel> newsListModels) {
        super.onNext(newsListModels);
        mResultCallBack.onResult(newsListModels);
    }
}
