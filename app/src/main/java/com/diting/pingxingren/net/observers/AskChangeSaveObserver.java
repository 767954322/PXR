package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.AskRobotModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * AskRobotModel
 * Created by Administrator on 2018/1/11.
 */

public class AskChangeSaveObserver extends DefaultObserver<AskRobotModel> {

    @Override
    protected void onStart() {
        super.onStart();
    }

    public AskChangeSaveObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(AskRobotModel askRobotModel) {
        super.onNext(askRobotModel);
        mResultCallBack.onResult(askRobotModel);
    }
}
