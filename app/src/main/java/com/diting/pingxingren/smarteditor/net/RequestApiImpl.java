package com.diting.pingxingren.smarteditor.net;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.ArticleResultModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.model.SaveTitleResultModel;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleContentBody;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleTitleBody;
import com.diting.pingxingren.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:13.
 * Description: .
 */

public class RequestApiImpl {

    private static RequestApiImpl sRequestApi;
    private ApiService mApiService;

    static RequestApiImpl getInstance() {
        if (sRequestApi == null) {
            synchronized (RequestApiImpl.class) {
                if (sRequestApi == null)
                    sRequestApi = new RequestApiImpl();
            }
        }
        return sRequestApi;
    }

    private RequestApiImpl() {
        Retrofit retrofit = ApiConnection.getInstance().getRetrofit();
        mApiService = retrofit.create(ApiService.class);
    }

    void saveArticleTitle(SaveArticleTitleBody saveArticleTitleBody, Observer<SaveTitleResultModel> observer) {
        mApiService.saveArticleTitle(saveArticleTitleBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void saveArticleContent(SaveArticleContentBody articleContentBody, Observer<CodeResultModel> observer) {
        mApiService.saveArticleContent(articleContentBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void updateArticle(RequestBody requestBody, Observer<CodeResultModel> observer) {
        mApiService.update(requestBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void queryArticle(Map<String, String> map, Observer<ArticleModel> observer) {
        mApiService.queryArticle(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void queryArticleById(String id, Observer<ArticleResultModel> observer) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isNotEmpty(id)) try {
            if (StringUtil.isNotEmpty(id))
                jsonObject.put("editor_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), jsonObject.toString());
        mApiService.queryArticleById(requestBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
