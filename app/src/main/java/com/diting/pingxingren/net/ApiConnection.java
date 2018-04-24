package com.diting.pingxingren.net;

import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.net.cookie.CookiesManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:35.
 * Description: .
 */

public class ApiConnection {

    private static ApiConnection sApiConnection;
    private volatile Retrofit mRetrofit;
    private volatile Retrofit mRetrofitByWX;
    private volatile Retrofit mRetrofitByUpload;

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

    Retrofit getRetrofitByUpload() {
        if (mRetrofitByUpload == null) {
            synchronized (Retrofit.class) {
                if (mRetrofitByUpload == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(RequestApi.TIME_OUT, TimeUnit.MINUTES)
                            .writeTimeout(RequestApi.TIME_OUT, TimeUnit.MINUTES)
                            .readTimeout(RequestApi.TIME_OUT, TimeUnit.MINUTES)
                            .cookieJar(new CookiesManager(MyApplication.getInstance()))
                            .addInterceptor(loggingInterceptor)
                            .build();

                    mRetrofitByUpload = new Retrofit.Builder()
                            .baseUrl(RequestApi.BASE_HOST)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofitByUpload;
    }

    /**
     * 微信统一下单使用, 请勿改动!
     */
    Retrofit getRetrofitByWX() {
        if (mRetrofitByWX == null) {
            synchronized (Retrofit.class) {
                if (mRetrofitByWX == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(RequestApi.TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)
                            .build();

                    mRetrofitByWX = new Retrofit.Builder()
                            .baseUrl(RequestApi.BASE_WX_HOST) // 微信统一下单, 不要修改
                            .client(okHttpClient)
                            .build();
                }
            }
        }

        return mRetrofitByWX;
    }
}
