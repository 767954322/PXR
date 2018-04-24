package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.ChatUserManageModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 访客管理
 * Created by Administrator on 2017/12/14.
 */

public class ChatUserManageObserver extends DefaultObserver<ChatUserManageModel> {
    public ChatUserManageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(ChatUserManageModel chatUserManageModel) {
        super.onNext(chatUserManageModel);
        mResultCallBack.onResult(chatUserManageModel);
    }
}
