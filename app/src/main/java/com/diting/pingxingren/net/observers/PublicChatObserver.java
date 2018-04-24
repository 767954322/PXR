package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.PublicChatModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class PublicChatObserver extends DefaultObserver<PublicChatModel> {

    public PublicChatObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(PublicChatModel publicChatModel) {
        super.onNext(publicChatModel);
        mResultCallBack.onResult(publicChatModel);
    }
}
