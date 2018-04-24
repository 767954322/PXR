package com.diting.pingxingren.net;

import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.model.UploadImageModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 17:39.
 * Api请求服务.
 */

public interface UploadApiService {

    /**
     * 上传图片.
     */
    @Multipart
    @POST(RequestApi.UPLOAD_IMAGE)
    Observable<UploadImageModel> uploadImage(@Part MultipartBody.Part imageFile);

    /**
     * 上传附件.
     */
    @Multipart
    @POST(RequestApi.UPLOAD_ANNEX)
    Observable<UploadAnnexModel> uploadAnnex(@Part MultipartBody.Part annexFile);
}