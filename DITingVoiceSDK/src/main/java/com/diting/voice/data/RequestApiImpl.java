package com.diting.voice.data;

import com.diting.voice.data.body.ChatRequestBody;
import com.diting.voice.data.body.VoiceCallInfo;
import com.diting.voice.data.model.CommonInfoModel;
import com.diting.voice.data.model.PublicChatInfoModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 10:45.
 * Description: 访问接口实现类.
 */

public class RequestApiImpl {

    private static RequestApiImpl sRequestApi;
    private ApiService mApiService;

    public static RequestApiImpl newInstance() {
        if (sRequestApi == null) {
            synchronized (RequestApiImpl.class) {
                if (sRequestApi == null)
                    sRequestApi = new RequestApiImpl();
            }
        }
        return sRequestApi;
    }

    private RequestApiImpl() {
        Retrofit retrofit = ApiConnection.newInstance().getRetrofit();
        mApiService = retrofit.create(ApiService.class);
    }

    public void getPublicChatInfo(ChatRequestBody requestBody, Observer<PublicChatInfoModel> observer) {
        mApiService.getPublicChatInfo(requestBody).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void saveCallInfo(VoiceCallInfo requestBody, Observer<CommonInfoModel> observer) {
        mApiService.saveCallInfo(requestBody).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
