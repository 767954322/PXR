package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.NewVersionModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class NewVersionObserver extends DefaultObserver<NewVersionModel> {

    public NewVersionObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(NewVersionModel newVersionModel) {
        super.onNext(newVersionModel);
        if (newVersionModel != null)
            mResultCallBack.onResult(newVersionModel);
        else {
            newVersionModel = new NewVersionModel();
            newVersionModel.setDescription("检查失败!");
            mResultCallBack.onError(newVersionModel);
        }
    }
}
