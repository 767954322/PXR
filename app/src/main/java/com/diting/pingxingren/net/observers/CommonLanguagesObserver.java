package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class CommonLanguagesObserver extends DefaultObserver<List<CommonLanguageModel>> {

    public CommonLanguagesObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<CommonLanguageModel> commonLanguageModels) {
        super.onNext(commonLanguageModels);
        mResultCallBack.onResult(commonLanguageModels);
    }
}
