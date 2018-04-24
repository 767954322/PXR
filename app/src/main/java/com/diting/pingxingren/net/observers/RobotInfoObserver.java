package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class RobotInfoObserver extends DefaultObserver<RobotInfoModel> {

    public RobotInfoObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(RobotInfoModel robotInfoModel) {
        super.onNext(robotInfoModel);
        if (robotInfoModel != null)
            mResultCallBack.onResult(robotInfoModel);
        else
            mResultCallBack.onError("获取机器人信息失败!");
    }
}
