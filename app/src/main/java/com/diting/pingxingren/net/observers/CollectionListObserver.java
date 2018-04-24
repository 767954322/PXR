package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * 我的收藏列表
 * Created by Administrator on 2018/1/9.
 */

public class CollectionListObserver extends DefaultObserver<List<CollectionModel>> {
    public CollectionListObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<CollectionModel> collectionModels) {
        super.onNext(collectionModels);
        mResultCallBack.onResult(collectionModels);
    }
}
