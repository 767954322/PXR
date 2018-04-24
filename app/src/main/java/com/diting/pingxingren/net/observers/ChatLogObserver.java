package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ChatLogModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 聊天记录  访客管理点击进入
 * Created by Administrator on 2017/12/16.
 */

public class ChatLogObserver extends DefaultObserver<ChatLogModel> {
    public ChatLogObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ChatLogModel chatLogModel) {
        super.onNext(chatLogModel);
        mResultCallBack.onResult(chatLogModel);
    }
}
