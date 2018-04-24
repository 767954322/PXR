package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.UserInfoModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class UserInfoObserver extends DefaultObserver<UserInfoModel> {

    public UserInfoObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(UserInfoModel userInfoModel) {
        super.onNext(userInfoModel);
        if (userInfoModel != null)
            mResultCallBack.onResult(userInfoModel);
        else
            mResultCallBack.onError("获取信息失败!");
    }
}
