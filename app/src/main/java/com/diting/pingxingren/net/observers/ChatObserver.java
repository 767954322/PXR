package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ChatModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class ChatObserver extends DefaultObserver<ChatModel> {

    public ChatObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ChatModel chatModel) {
        super.onNext(chatModel);
        mResultCallBack.onResult(chatModel);
    }
}
