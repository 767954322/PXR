package com.diting.pingxingren.smarteditor.net;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.ArticleResultModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.model.ContentModel;
import com.diting.pingxingren.smarteditor.model.SaveTitleResultModel;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleContentBody;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleTitleBody;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:14.
 * Description: .
 */

public interface ApiService {

    /**
     * 保存标题.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.EDITOR_SAVE_ARTICLE_TITLE)
    Observable<SaveTitleResultModel> saveArticleTitle(@Body SaveArticleTitleBody saveArticleTitleBody);

    /**
     * 保存内容.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.EDITOR_SAVE_ARTICLE_CONTENT)
    Observable<List<ContentModel>> saveArticleContent(@Body List<ContentModel> content);

    /**
     * 保存内容.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.EDITOR_SAVE_ARTICLE_CONTENT)
    Observable<CodeResultModel> saveArticleContent(@Body SaveArticleContentBody articleContentBody);

    /**
     * 修改文章信息.
     */
    @POST(RequestApi.EDITOR_UPDATE)
    Observable<CodeResultModel> update(@Body RequestBody requestBody);

    /**
     * 查询文章列表.
     */
    @GET(RequestApi.EDITOR_SEARCH_HOME)
    Observable<ArticleModel> queryArticle(@QueryMap Map<String, String> map);

    /**
     * 根据文章Id查询文章.
     */
    @POST(RequestApi.EDITOR_SEARCH_ARTICLE)
    Observable<ArticleResultModel> queryArticleById(@Body RequestBody requestBody);
}
