package com.diting.voice.data;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 11:18.
 * Description: 访问接口回调函数.
 */

public interface ObserverCallback {

    void onComplete();

    void onNext(Object o);

    void onNext(List<?> objects);

    void onError(String msg);
}
