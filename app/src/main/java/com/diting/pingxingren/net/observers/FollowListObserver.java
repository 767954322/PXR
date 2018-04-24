package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**关注列表
 * Created by Administrator on 2017/12/12.
 */

public class FollowListObserver extends DefaultObserver<List<CommunicateModel>> {
    public FollowListObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<CommunicateModel> communicateModels) {
        super.onNext(communicateModels);
        mResultCallBack.onResult(communicateModels);
    }
}
