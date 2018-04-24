package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class ChildRobotsObserver extends DefaultObserver<List<ChildRobotModel>> {

    public ChildRobotsObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<ChildRobotModel> childRobotModels) {
        super.onNext(childRobotModels);
        mResultCallBack.onResult(childRobotModels);
    }
}
