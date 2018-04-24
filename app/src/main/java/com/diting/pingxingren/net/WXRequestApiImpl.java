package com.diting.pingxingren.net;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 19, 17:06.
 * Description: .
 */

public class WXRequestApiImpl {

    private static WXRequestApiImpl sRequestApi;
    private WXApiService mApiService;

    static WXRequestApiImpl getInstance() {
        if (sRequestApi == null) {
            synchronized (WXRequestApiImpl.class) {
                if (sRequestApi == null)
                    sRequestApi = new WXRequestApiImpl();
            }
        }
        return sRequestApi;
    }

    private WXRequestApiImpl() {
        Retrofit retrofit = ApiConnection.getInstance().getRetrofitByWX();
        mApiService = retrofit.create(WXApiService.class);
    }

    Call<ResponseBody> unifiedOrder(RequestBody requestBody) {
        return mApiService.unifiedOrder(RequestApi.WX_UNIFIED_ORDER, requestBody);
    }
}
