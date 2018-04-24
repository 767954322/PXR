package com.diting.pingxingren.net.observers;

import com.diting.pingxingren.net.ResultCallBack;
import com.diting.voice.data.body.VoiceCallInfo;

import java.util.List;

/**
 * 通话记录
 * Created by Administrator on 2017/12/15.
 */

public class CallRecordObserver extends DefaultObserver<List<VoiceCallInfo>> {
    public CallRecordObserver(ResultCallBack resultCallBack) {
        super(resultCallBack);
    }

    @Override
    public void onNext(List<VoiceCallInfo> voiceCallInfos) {
        super.onNext(voiceCallInfos);
        mResultCallBack.onResult(voiceCallInfos);
    }
}
