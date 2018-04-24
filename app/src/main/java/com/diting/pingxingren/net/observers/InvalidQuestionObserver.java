package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.InvalidQuestionModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * 无效问题
 * Created by Administrator on 2017/12/16.
 */

public class InvalidQuestionObserver extends DefaultObserver<InvalidQuestionModel> {
    public InvalidQuestionObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(InvalidQuestionModel invalidQuestionModel) {
        super.onNext(invalidQuestionModel);
        mResultCallBack.onResult(invalidQuestionModel);
    }
}
