package com.diting.pingxingren.net;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:51.
 * Description: .
 */

public interface ResultCallBack {

    void onResult(Object result);

    void onResult(List<?> resultList);

    void onError(Object o);
}
