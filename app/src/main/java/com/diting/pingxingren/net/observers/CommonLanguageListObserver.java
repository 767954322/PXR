package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.List;

/**
 * 获取常用语列表    与PC端同步
 * Created by Administrator on 2018/1/9.
 */

public class CommonLanguageListObserver extends DefaultObserver<List<CommonLanguageListModel>> {
    public CommonLanguageListObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<CommonLanguageListModel> commonLanguageListModels) {
        super.onNext(commonLanguageListModels);
        mResultCallBack.onResult(commonLanguageListModels);
    }
}
