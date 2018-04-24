package com.diting.pingxingren.util;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;

/**
 * Created by asus on 2017/2/7.
 */

public interface ImageCallBack {

    void onSuccess(Bitmap response);

    void onFailed(VolleyError error);
}
