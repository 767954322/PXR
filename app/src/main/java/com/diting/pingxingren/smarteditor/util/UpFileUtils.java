package com.diting.pingxingren.smarteditor.util;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by 2018 on 2018/2/26.
 * 文件上传工具
 */

public class UpFileUtils {

    //是否取消上传
    private static volatile boolean isCancelled = false;
    private static UploadManager uploadManager = new UploadManager();

    public static String token = "bergaeasdf";

    public void upLoadFile(File file, String fileName, String token) {
        if (file.exists()) {
            uploadManager.put(file, fileName, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    Log.e("tag", "key=" + key + "info=" + info.path + ";response=" + response.toString());
                }
            }, new UploadOptions(null, null, false, null, new UpCancellationSignal() {
                @Override
                public boolean isCancelled() {
                    return isCancelled;
                }
            }));
        } else {
            Log.e("tag", "文件不存在");
        }
    }

    public static void cancelUpLoad() {
        isCancelled = true;
    }

}
