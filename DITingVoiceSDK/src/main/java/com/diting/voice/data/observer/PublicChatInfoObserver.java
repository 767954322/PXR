package com.diting.voice.data.observer;

import com.diting.voice.data.ObserverCallback;
import com.diting.voice.data.model.PublicChatInfoModel;
import com.diting.voice.utils.Utils;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 13:43.
 * Description: .
 */

public class PublicChatInfoObserver extends DefaultObserver<PublicChatInfoModel> {

    public PublicChatInfoObserver(ObserverCallback callback) {
        super(callback);
    }

    @Override
    public void onNext(PublicChatInfoModel chatInfoModel) {
        String answer = chatInfoModel.getAnswer();
        if (Utils.isNotEmpty(answer)) {
            mCallback.onNext(chatInfoModel);
        } else {
            mCallback.onError("出现错误!");
        }
    }
}
