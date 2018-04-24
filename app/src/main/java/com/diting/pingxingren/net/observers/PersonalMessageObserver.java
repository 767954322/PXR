package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.model.PersonalMailModel;
import com.diting.pingxingren.net.ResultCallBack;

/**
 * Created by Administrator on 2017/12/15.
 */

public class PersonalMessageObserver extends DefaultObserver<PersonalMailModel> {

    public PersonalMessageObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(PersonalMailModel personalMailModel) {
        super.onNext(personalMailModel);
        mResultCallBack.onResult(personalMailModel);
    }
}