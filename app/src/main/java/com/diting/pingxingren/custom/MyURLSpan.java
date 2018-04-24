package com.diting.pingxingren.custom;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.diting.pingxingren.activity.WebActivity;

/**
 * Created by asus on 2017/5/23.
 */

public class MyURLSpan extends ClickableSpan {
    private String url;
    private Context context;

    public MyURLSpan(String url,Context context) {
        this.url = url;
        this.context = context;
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

}
