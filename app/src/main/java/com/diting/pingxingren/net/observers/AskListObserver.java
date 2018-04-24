package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.AskListModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * 会问列表
 * Created by Administrator on 2018/1/10.
 */

public class AskListObserver extends DefaultObserver<List<AskListModel>> {
    public AskListObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<AskListModel> askListModels) {
        super.onNext(askListModels);
        mResultCallBack.onResult(askListModels);
    }
}
