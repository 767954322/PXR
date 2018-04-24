package com.diting.pingxingren.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/15.
 */

public class TextLengthUtil implements InputFilter {

    private int mMaxLength;
    private String toastStr;
    private Context mContext1;

    public TextLengthUtil(Context mContext, int max, String toast) {
        mContext1 = mContext;
        mMaxLength = max - 1;
        toastStr = toast;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {

            Toast.makeText(mContext1, toastStr, Toast.LENGTH_LONG).show();
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}