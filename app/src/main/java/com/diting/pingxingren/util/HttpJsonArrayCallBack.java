package com.diting.pingxingren.util;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by asus on 2017/1/3.
 */

public interface HttpJsonArrayCallBack {

    void onSuccess(JSONArray response);

    void onFailed(VolleyError error);
}
