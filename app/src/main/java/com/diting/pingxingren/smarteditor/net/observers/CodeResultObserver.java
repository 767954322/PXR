package com.diting.pingxingren.smarteditor.net.observers;

import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.smarteditor.util.Constants;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 10:57.
 * Description: .
 */

public class CodeResultObserver extends DefaultObserver<CodeResultModel> {

    private int mRequestType = 0;

    public CodeResultObserver(int requestType, ResultCallBack resultCallBack) {
        super(resultCallBack);
        mRequestType = requestType;
    }

    @Override
    public void onNext(CodeResultModel codeResultModel) {
        super.onNext(codeResultModel);
        String code = codeResultModel.getCode();
        String message;
        if (code.equals("1")) {
            if (mRequestType == Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE)
                message = "已删除!";
            else if (mRequestType == Constants.REQUEST_TYPE_SAVE_ARTICLE_CONTENT)
                message = "保存成功!";
            else
                message =  "修改成功!";

        } else {
            if (mRequestType == Constants.REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE)
                message = "删除失败!";
            else if (mRequestType == Constants.REQUEST_TYPE_SAVE_ARTICLE_CONTENT)
                message = "保存失败!";
            else
                message =  "修改失败!";
        }

        codeResultModel.setMessage(message);
        codeResultModel.setRequestType(mRequestType);
        mResultCallBack.onResult(codeResultModel);
    }
}
