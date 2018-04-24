package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.MyLuckyModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * 我的活动
 * Created by Administrator on 2018/1/8.
 */

public class MyLuckyObserver extends DefaultObserver<List<MyLuckyModel>> {
    public MyLuckyObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<MyLuckyModel> myLuckyModels) {
        super.onNext(myLuckyModels);
        mResultCallBack.onResult(myLuckyModels);
    }
}

