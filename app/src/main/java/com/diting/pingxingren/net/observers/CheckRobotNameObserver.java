package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.RobotNameIsExistsModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Created by 2018 on 2018/1/31.
 * 检查机器人姓名是否存在
 */

public class CheckRobotNameObserver extends DefaultObserver<RobotNameIsExistsModel> {

    public CheckRobotNameObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(RobotNameIsExistsModel robotNameIsExistsModel) {
        mResultCallBack.onResult(robotNameIsExistsModel);
        super.onNext(robotNameIsExistsModel);
    }
}
