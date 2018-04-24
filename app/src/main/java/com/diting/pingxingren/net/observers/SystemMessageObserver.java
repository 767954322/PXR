package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.SystemMessageModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 系统消息
 * Created by Administrator on 2017/12/14.
 */

public class SystemMessageObserver extends DefaultObserver<SystemMessageModel> {
    public SystemMessageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(SystemMessageModel systemMessageModel) {
        super.onNext(systemMessageModel);
        mResultCallBack.onResult(systemMessageModel);
    }
}
