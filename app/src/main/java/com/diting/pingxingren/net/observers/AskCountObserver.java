package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.AskCountModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Created by Administrator on 2018/1/15.
 */

public class AskCountObserver extends DefaultObserver<AskCountModel> {
    public AskCountObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(AskCountModel askCountModel) {
        super.onNext(askCountModel);
        mResultCallBack.onResult(askCountModel);
    }
}
