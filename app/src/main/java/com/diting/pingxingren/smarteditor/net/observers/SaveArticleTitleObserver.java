package com.diting.pingxingren.smarteditor.net.observers;

import com.diting.pingxingren.smarteditor.model.SaveTitleResultModel;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.util.StringUtil;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:59.
 * Description: .
 */

public class SaveArticleTitleObserver extends DefaultObserver<SaveTitleResultModel> {
    public SaveArticleTitleObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(SaveTitleResultModel saveTitleResultModel) {
        super.onNext(saveTitleResultModel);
        String robotId = saveTitleResultModel.getRobot_id();
        String iecId = saveTitleResultModel.getIec_id();
        if (StringUtil.isNotEmpty(robotId) && StringUtil.isNotEmpty(iecId))
            mResultCallBack.onResult(saveTitleResultModel);
        else
            mResultCallBack.onError("保存失败请重试.");
    }
}
