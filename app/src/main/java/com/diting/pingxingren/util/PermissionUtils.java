package com.diting.pingxingren.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.diting.pingxingren.activity.VideoCallActivity;
import com.diting.pingxingren.activity.VoiceCallActivity;

/**
 * Created by asus on 2017/7/27.
 */

public class PermissionUtils {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static final int MY_PERMISSIONS_REQUEST_AUDIO_RECORD = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 4;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 5;

    public static void requestCallPermission(Activity activity, String phone){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
                Utils.showMissingPermissionDialog(activity, "拨号");
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            callPhone(activity,phone);
        }
    }
    public static void callPhone(Activity activity,String phone)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        activity.startActivity(intent);
        if(activity instanceof VoiceCallActivity || activity instanceof VideoCallActivity) {
            activity.finish();
        }
    }



}
