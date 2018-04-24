package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.LoginResultModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class LoginObserver extends DefaultObserver<LoginResultModel> {

    public LoginObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(LoginResultModel loginResultModel) {
        super.onNext(loginResultModel);
        String resultMessage = loginResultModel.getMessage();
        if (resultMessage.equals("登录成功！"))
            mResultCallBack.onResult(loginResultModel);
        else
            mResultCallBack.onError(resultMessage);
    }
}
