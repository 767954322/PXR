package com.diting.pingxingren.util;

import com.android.volley.VolleyError;

/**
 * Created by asus on 2017/2/13.
 */

public interface HttpStringCallBack {

    void onSuccess(String response);

    void onFailed(VolleyError error);
}
