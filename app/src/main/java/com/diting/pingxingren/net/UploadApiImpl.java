package com.diting.pingxingren.net;

import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.util.FileUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 19, 17:06.
 * Description: .
 */

public class UploadApiImpl {

    private static UploadApiImpl sRequestApi;
    private UploadApiService mApiService;

    public static UploadApiImpl getInstance() {
        if (sRequestApi == null) {
            synchronized (UploadApiImpl.class) {
                if (sRequestApi == null)
                    sRequestApi = new UploadApiImpl();
            }
        }
        return sRequestApi;
    }

    private UploadApiImpl() {
        Retrofit retrofit = ApiConnection.getInstance().getRetrofitByUpload();
        mApiService = retrofit.create(UploadApiService.class);
    }

    void uploadImage(File imageFile, UploadImageObserver observer) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        mApiService.uploadImage(part).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void uploadAnnex(File annexFile, UploadAnnexObserver observer) {
        String mimeType = FileUtil.getMIMEType(annexFile);
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), annexFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", annexFile.getName(), requestBody);
        mApiService.uploadAnnex(part).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
