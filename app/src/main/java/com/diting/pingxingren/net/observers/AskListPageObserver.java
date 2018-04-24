package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.AskModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Created by Administrator on 2018/1/15.
 */

public class AskListPageObserver extends DefaultObserver<AskModel> {
    public AskListPageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(AskModel askModel) {
        super.onNext(askModel);
        mResultCallBack.onResult(askModel);
    }

}
