package com.diting.pingxingren.smarteditor.net;

import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.qiniu.Auth;
import com.diting.pingxingren.smarteditor.model.ArticleContentModel;
import com.diting.pingxingren.smarteditor.model.ContentModel;
import com.diting.pingxingren.smarteditor.util.Constants;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 29, 22:15.
 * Description: .
 */

class UploadFileManager {

    private static volatile UploadFileManager sUploadFileManager;
    private static volatile boolean isCancelled = false;
    private static volatile UploadManager sUploadManager;
    private static String sToken;

    public static UploadFileManager newInstance() {
        if (sUploadFileManager == null) {
            synchronized (UploadFileManager.class) {
                if (sUploadFileManager == null)
                    sUploadFileManager = new UploadFileManager();
            }
        }

        return sUploadFileManager;
    }

    private UploadFileManager() {
        String qiNiuToken = MySharedPreferences.getQiNiuToken();
        String time;
        if (StringUtil.isNotEmpty(qiNiuToken)) {
            String[] s = qiNiuToken.split(",");
            sToken = s[0];
            time = s[1];

            if (System.currentTimeMillis() / 1000 > Long.valueOf(time))
                getQiNiuToken();
        } else {
            getQiNiuToken();
        }

        sUploadManager = new UploadManager();
    }

    private void getQiNiuToken() {
        long deadLine = System.currentTimeMillis() / 1000 + Constants.QI_NIU_EXPRIES;
        sToken = Auth.create(Constants.QI_NIU_AK, Constants.QI_NIU_SK)
                .uploadToken(Constants.QI_NIU_BUCKET_NAME);
        MySharedPreferences.setQiNiuToken(sToken + "," + deadLine);

    }

    private static void uploadFile(File file, final UploadAnnexObserver uploadAnnexObserver) {
        // ApiManager.uploadAnnex(file, uploadAnnexObserver);
        String extension = FileUtil.getFileExtension(file);
        if (extension.equals("jpg"))
            extension = "jpeg";
        String fileName = "android_" + TimeUtil.getNowTimeMills() + "." + extension;
        sUploadManager.put(file, fileName, sToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                UploadAnnexModel uploadAnnexModel = new UploadAnnexModel();
                if (info.isOK()) {
                    uploadAnnexModel.setUrl(Constants.QI_NIU_HOST + key);
                } else {
                    uploadAnnexModel.setError(info.error);
                }
                uploadAnnexObserver.onNext(uploadAnnexModel);
            }
        }, new UploadOptions(null, null, false, null, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return isCancelled;
            }
        }));
    }

    static void uploadFile(List<ContentModel> contentModels,
                           ResultCallBack resultCallBack) {
        int uploadSize = contentModels.size();
        uploadFile(0, uploadSize, contentModels, resultCallBack);
    }

    private static void uploadFile(int uploadIndex, final int uploadSize,
                                   final List<ContentModel> contentModels,
                                   final ResultCallBack resultCallBack) {
        final int uploadIndex1 = uploadIndex;
        final int uploadIndex2 = ++uploadIndex;
        resultCallBack.onResult("正在上传第 " + (uploadIndex1 + 1) + "/" +
                com.diting.pingxingren.smarteditor.net.ApiManager.sUploadSize + " 文件...");
        uploadFile(FileUtil.getFileByPath(contentModels.get(uploadIndex1).getContent()),
                new UploadAnnexObserver(new com.diting.pingxingren.net.ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        UploadAnnexModel uploadAnnexModel = (UploadAnnexModel) result;
                        contentModels.get(uploadIndex1).setContent(uploadAnnexModel.getUrl());
                        if (uploadIndex2 < uploadSize) {
                            uploadFile(uploadIndex2, uploadSize, contentModels, resultCallBack);
                        } else {
                            resultCallBack.onResult("上传修改内容!");
                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {
                    }

                    @Override
                    public void onError(Object o) {
                        resultCallBack.onError(o);
                    }
                }));
    }

    static void uploadUpdateFile(List<ArticleContentModel> contentModels,
                                 ResultCallBack resultCallBack) {
        int uploadSize = contentModels.size();
        uploadUpdateFile(0, uploadSize, contentModels, resultCallBack);
    }

    private static void uploadUpdateFile(int uploadIndex, final int uploadSize,
                                         final List<ArticleContentModel> contentModels,
                                         final ResultCallBack resultCallBack) {
        final int uploadIndex1 = uploadIndex;
        final int uploadIndex2 = ++uploadIndex;
        resultCallBack.onResult("正在上传第 " + (uploadIndex1 + 1) + "/" +
                com.diting.pingxingren.smarteditor.net.ApiManager.sUploadSize + " 文件...");
        uploadFile(FileUtil.getFileByPath(contentModels.get(uploadIndex1).getContent()),
                new UploadAnnexObserver(new com.diting.pingxingren.net.ResultCallBack() {
                    @Override
                    public void onResult(Object result) {
                        UploadAnnexModel uploadAnnexModel = (UploadAnnexModel) result;
                        contentModels.get(uploadIndex1).setContent(uploadAnnexModel.getUrl());
                        if (uploadIndex2 < uploadSize) {
                            uploadUpdateFile(uploadIndex2, uploadSize, contentModels, resultCallBack);
                        } else {
                            resultCallBack.onResult("上传完成!");
                        }
                    }

                    @Override
                    public void onResult(List<?> resultList) {
                    }

                    @Override
                    public void onError(Object o) {
                        resultCallBack.onError(o);
                    }
                }));
    }
}
