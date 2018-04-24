package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.UnreadMailModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class UnreadMailObserver extends DefaultObserver<UnreadMailModel> {

    public UnreadMailObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(UnreadMailModel unreadMailModel) {
        super.onNext(unreadMailModel);
        mResultCallBack.onResult(unreadMailModel);
    }
}
