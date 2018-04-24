package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 根据手机号获取机器人详情   访客管理界面
 * Created by Administrator on 2017/12/14.
 */

public class RobotInfoByPhoneObserver extends DefaultObserver<RobotInfoModel> {
    public RobotInfoByPhoneObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(RobotInfoModel chatBundleModel) {
        super.onNext(chatBundleModel);
        mResultCallBack.onResult(chatBundleModel);
    }
}
