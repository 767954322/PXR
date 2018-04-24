package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.entity.ExternalOptions;
import com.diting.pingxingren.model.CommonFeaturesModel;
import com.diting.pingxingren.net.ResultCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class CommonFeaturesObserver extends DefaultObserver<List<CommonFeaturesModel>> {

    public CommonFeaturesObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<CommonFeaturesModel> commonFeaturesModels) {
        super.onNext(commonFeaturesModels);
        int size = commonFeaturesModels.size();
        ExternalOptions externalOptions;
        ArrayList<ExternalOptions> externalOptionsList = new ArrayList<>();

        if (size > 0) {
            for (CommonFeaturesModel commonFeaturesModel : commonFeaturesModels) {
                externalOptions = new ExternalOptions();
                externalOptions.setAppName(commonFeaturesModel.getName());
                externalOptions.setChoose(false);
                externalOptions.setId(commonFeaturesModel.getId());
                externalOptionsList.add(externalOptions);
            }
            mResultCallBack.onResult(externalOptionsList);
        } else {
            mResultCallBack.onError("获取信息失败!");
        }
    }
}
