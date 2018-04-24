package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.KnowledgeModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:58.
 * Description: .
 */

public class KnowledgeObserver extends DefaultObserver<KnowledgeModel> {

    public KnowledgeObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(KnowledgeModel knowledgeModel) {
        super.onNext(knowledgeModel);
        mResultCallBack.onResult(knowledgeModel);
    }
}
