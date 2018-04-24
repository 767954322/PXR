package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ThirdBindModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 25, 17:31.
 * Description: 是否绑定了第三方.
 */

public class ThirdIsBindObserver extends DefaultObserver<ThirdBindModel>{
    public ThirdIsBindObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ThirdBindModel thirdBindModel) {
        super.onNext(thirdBindModel);
        mResultCallBack.onResult(thirdBindModel);
    }
}
