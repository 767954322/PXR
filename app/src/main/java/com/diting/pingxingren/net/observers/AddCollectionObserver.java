package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 添加收藏
 * Created by Administrator on 2018/1/9.
 */

public class AddCollectionObserver extends DefaultObserver<CollectionModel> {
    public AddCollectionObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(CollectionModel collectionModel) {
        super.onNext(collectionModel);
        mResultCallBack.onResult(collectionModel);
    }
}

