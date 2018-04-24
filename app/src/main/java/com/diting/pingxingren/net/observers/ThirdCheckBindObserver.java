package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ThirdCheckBindModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 27, 11:41.
 * Description: .
 */

public class ThirdCheckBindObserver  extends DefaultObserver<ThirdCheckBindModel>{
    public ThirdCheckBindObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ThirdCheckBindModel thirdCheckBindModel) {
        super.onNext(thirdCheckBindModel);
        mResultCallBack.onResult(thirdCheckBindModel);
    }
}
