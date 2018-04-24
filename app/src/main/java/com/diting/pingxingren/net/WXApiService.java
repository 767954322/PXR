package com.diting.pingxingren.net;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 17:39.
 * Api请求服务.
 */

public interface WXApiService {

    /**
     * 统一下单.
     */
    @POST
    Call<ResponseBody> unifiedOrder(@Url String url, @Body RequestBody requestBody);
}