package com.diting.pingxingren.smarteditor.net;

import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.net.RequestApi;
import com.diting.pingxingren.smarteditor.net.cookie.CookiesManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:13.
 * Description: .
 */

public class ApiConnection {

    private static ApiConnection sApiConnection;
    private volatile Retrofit mRetrofit;

    public static ApiConnection getInstance() {
        if (sApiConnection == null) {
            synchronized (ApiConnection.class) {
                if (sApiConnection == null)
                    sApiConnection = new ApiConnection();
            }
        }
        return sApiConnection;
    }

    Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (Retrofit.class) {
                if (mRetrofit == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .cookieJar(new CookiesManager(MyApplication.getInstance()))
                            .addInterceptor(loggingInterceptor)
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
}
