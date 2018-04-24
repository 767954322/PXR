package com.diting.voice.data.observer;

import com.diting.voice.data.ObserverCallback;

import io.reactivex.observers.DisposableObserver;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 13:42.
 * Description: .
 */

public class DefaultObserver<T> extends DisposableObserver<T> {

    ObserverCallback mCallback;

    public DefaultObserver(ObserverCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onNext(T t) {
        mCallback.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        mCallback.onError(e.getMessage());
    }

    @Override
    public void onComplete() {
        mCallback.onComplete();
    }
}

