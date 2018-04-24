package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.Utils;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class UploadImageObserver extends DefaultObserver<UploadImageModel> {

    public UploadImageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(UploadImageModel uploadImageModel) {
        super.onNext(uploadImageModel);
        String errorMsg = uploadImageModel.getError();
        if (Utils.isNotEmpty(errorMsg))
            mResultCallBack.onError(errorMsg);
        else
            mResultCallBack.onResult(uploadImageModel);
    }
}
