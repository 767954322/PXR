package com.diting.voice.data;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/10, 17:51.
 * Description: ApiConnection.
 */

public class ApiConnection {

    private static ApiConnection sApiConnection;
    private volatile Retrofit mRetrofit;

    public static ApiConnection newInstance() {
        if (sApiConnection == null) {
            synchronized (ApiConnection.class) {
                if (sApiConnection == null)
                    sApiConnection = new ApiConnection();
            }
        }
        return sApiConnection;
    }

    private ApiConnection() {
        createRetrofit();
    }

    private Retrofit createRetrofit() {
        if (mRetrofit == null) {
            synchronized (Retrofit.class) {
                if (mRetrofit == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .build();

                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(RequestApi.BASE_HOST)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }

        return mRetrofit;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
