package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class CommonLanguageObserver extends DefaultObserver<CommonLanguageModel> {

    public CommonLanguageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(CommonLanguageModel commonLanguageModel) {
        super.onNext(commonLanguageModel);
        if (commonLanguageModel.getId() != -1)
            mResultCallBack.onResult(commonLanguageModel);
        else
            mResultCallBack.onError("保存常用语失败.");
    }
}
