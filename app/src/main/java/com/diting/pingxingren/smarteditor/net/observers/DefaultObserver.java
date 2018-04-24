package com.diting.pingxingren.smarteditor.net.observers;

import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:52.
 * Description: .
 */

public class DefaultObserver<T> extends DisposableObserver<T> {

    ResultCallBack mResultCallBack;

    public DefaultObserver(ResultCallBack resultCallBack) {
        mResultCallBack = resultCallBack;
    }

    @Override
    public void onNext(T t) {
        LogUtils.i("onNext");
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.i("onError");
        errorMessageHandling(e, mResultCallBack);
    }

    @Override
    public void onComplete() {
        LogUtils.i("onComplete");
    }

    /**
     * 错误信息处理.
     */
    public static void errorMessageHandling(Throwable e, ResultCallBack resultCallBack) {
        String msg = e.getMessage();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            try {
                ResponseBody responseBody = httpException.response().errorBody();
                if (Utils.isNotEmpty(responseBody)) {
                    String errorBody = responseBody.string();
                    JSONObject jsonObject = new JSONObject(errorBody);
                    String errorMessage = jsonObject.getString("message");
                    resultCallBack.onError(errorMessage);
                } else {
                    resultCallBack.onError(getErrorMessage(msg));
                }
            } catch (IOException e1) {
                resultCallBack.onError(getErrorMessage(e1.getMessage()));
            } catch (JSONException e1) {
                resultCallBack.onError(getErrorMessage(e1.getMessage()));
            }
        } else if (e instanceof IllegalStateException) {
            resultCallBack.onError(getErrorMessage("请求接口失败!"));
        } else {
            resultCallBack.onError(getErrorMessage(msg));
        }
    }

    private static String getErrorMessage(String error) {
        if(!Utils.isEmpty(error)){
            if (error.equals("timeout"))
                return "请求超时";
            else if (error.equals("connect timed out"))
                return "连接服务器超时";
            else if (error.contains("Failed to connect to /"))
                return "连接失败, 请检查网络设置";
            else
                return error;
        }
        return "请求超时";

    }
}
