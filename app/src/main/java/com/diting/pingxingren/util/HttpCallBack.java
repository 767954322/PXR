package com.diting.pingxingren.util;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by asus on 2017/1/3.
 */

public interface HttpCallBack {

    void onSuccess(JSONObject response);

    void onFailed(VolleyError error);
}
