package com.diting.pingxingren.smarteditor.net;

import android.support.annotation.NonNull;

import com.diting.pingxingren.smarteditor.model.ArticleContentModel;
import com.diting.pingxingren.smarteditor.model.ArticleIdModel;
import com.diting.pingxingren.smarteditor.model.ContentModel;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleContentBody;
import com.diting.pingxingren.smarteditor.net.body.SaveArticleTitleBody;
import com.diting.pingxingren.smarteditor.net.observers.ArticleContentObserver;
import com.diting.pingxingren.smarteditor.net.observers.CodeResultObserver;
import com.diting.pingxingren.smarteditor.net.observers.QueryArticleObserver;
import com.diting.pingxingren.smarteditor.net.observers.SaveArticleTitleObserver;
import com.diting.pingxingren.smarteditor.util.Constants;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:50.
 * Description: .
 */

public class ApiManager {

    public static int sUploadSize;
    private static List<ContentModel> sContentModels = new ArrayList<>();
    private static List<ArticleContentModel> sUpdateContentModels = new ArrayList<>();

    /**
     * 保存文章标题.
     *
     * @param title    文章标题.
     * @param robotId  机器人Id.
     * @param iecId    文章分类Id.
     * @param observer 保存文章标题observer.
     */
    public static void saveArticleTitle(String title, String robotId,
                                        String iecId, SaveArticleTitleObserver observer) {
        String createTime = TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM1);
        SaveArticleTitleBody saveArticleTitleBody = new SaveArticleTitleBody();
        saveArticleTitleBody.setTitle(title);
        saveArticleTitleBody.setDeletetype(0);
        saveArticleTitleBody.setRobot_id(robotId);
        saveArticleTitleBody.setIec_id(iecId);
        saveArticleTitleBody.setCreatedtime(createTime);
        saveArticleTitleBody.setUpdatedtime(createTime);

        RequestApiImpl.getInstance().saveArticleTitle(saveArticleTitleBody, observer);
    }

    /**
     * 保存文章内容.
     *
     * @param saveContentModels   添加内容集合.
     * @param updateContentModels 修改内容集合.
     * @param deleteIds           删除内容id集合.
     * @param resultCallBack1     结果回调.
     */
    public static void saveArticleContent(final List<ContentModel> saveContentModels,
                                          final List<ArticleContentModel> updateContentModels,
                                          final List<ArticleIdModel> deleteIds,
                                          final ResultCallBack resultCallBack1) {
        UploadFileManager.newInstance();

        int contentType;
        final SaveArticleContentBody saveArticleContentBody = new SaveArticleContentBody();
        sContentModels.clear();
        for (final ContentModel contentModel : saveContentModels) {
            contentType = contentModel.getContenttype();
            if (contentType != 1 && contentType != 4) {
                sContentModels.add(contentModel);
            }
        }

        for (final ArticleContentModel articleContentModel : updateContentModels) {
            contentType = articleContentModel.getContenttype();
            if (contentType != 1 && contentType != 4
                    && !articleContentModel.getContent().startsWith("http")) {
                sUpdateContentModels.add(articleContentModel);
            }
        }

        sUploadSize = sContentModels.size() + sUpdateContentModels.size();

        ResultCallBack resultCallBack2 = new ResultCallBack() {
            @Override
            public void onResult(Object result) {
                if (result instanceof String) {
                    String s = (String) result;
                    if (s.equals("上传修改内容!")) { // 上传完添加附件后走这里.
                        if (sUpdateContentModels.size() > 0) { // 当修改附件不为空时, 上传修改附件.
                            UploadFileManager.uploadUpdateFile(sUpdateContentModels, this);
                        } else { // 当修改附件为空时, 保存文章内容
                            resultCallBack1.onResult("正在保存文章内容...");

                            // 将上传后附件后得到的网络地址设置覆盖本地地址.
                            for (ContentModel articleContentModel1 : saveContentModels) {
                                for (ContentModel articleContentModel2 : sContentModels) {
                                    if (articleContentModel1.getContentindex() == articleContentModel2.getContentindex()) {
                                        articleContentModel1.setContent(articleContentModel2.getContent());
                                    }
                                }
                            }

                            // 保存文章内容
                            saveArticleContentBody.setSave(saveContentModels);
                            saveArticleContentBody.setDelete(deleteIds);
                            saveArticleContentBody.setUpdate(updateContentModels);
                            RequestApiImpl.getInstance().saveArticleContent(saveArticleContentBody,
                                    new CodeResultObserver(Constants.REQUEST_TYPE_SAVE_ARTICLE_CONTENT, resultCallBack1));
                        }
                    } else if (!s.equals("上传完成!")) { // 当返回结果不是成功时, 吐司.
                        resultCallBack1.onResult(s);
                    } else { // 当返回结果是成功时, 保存文章内容.
                        resultCallBack1.onResult("正在保存文章内容...");
                        // 将上传后附件后得到的网络地址设置覆盖本地地址.
                        for (ContentModel articleContentModel1 : saveContentModels) {
                            for (ContentModel articleContentModel2 : sContentModels) {
                                if (articleContentModel1.getContentindex() == articleContentModel2.getContentindex()) {
                                    articleContentModel1.setContent(articleContentModel2.getContent());
                                }
                            }
                        }

                        // 将上传后附件后得到的网络地址设置覆盖本地地址.
                        for (ArticleContentModel articleContentModel1 : updateContentModels) {
                            for (ArticleContentModel articleContentModel2 : sUpdateContentModels) {
                                if (articleContentModel1.getContentindex() == articleContentModel2.getContentindex()) {
                                    articleContentModel1.setContent(articleContentModel2.getContent());
                                }
                            }
                        }

                        // 保存文章内容.
                        saveArticleContentBody.setSave(saveContentModels);
                        saveArticleContentBody.setDelete(deleteIds);
                        saveArticleContentBody.setUpdate(updateContentModels);
                        RequestApiImpl.getInstance().saveArticleContent(saveArticleContentBody,
                                new CodeResultObserver(Constants.REQUEST_TYPE_SAVE_ARTICLE_CONTENT, resultCallBack1));
                    }
                }
            }

            @Override
            public void onResult(List<?> resultList) {
            }

            @Override
            public void onError(Object o) {
                resultCallBack1.onError(o);
            }
        };

        if (sContentModels.size() == 0) { // 判断是否需要上传新附件, 0：不需要 1: 需要.
            if (sUpdateContentModels.size() == 0) { // 判断是否需要上传修改后附件, 0：不需要 1: 需要.
                saveArticleContentBody.setSave(saveContentModels);
                saveArticleContentBody.setDelete(deleteIds);
                saveArticleContentBody.setUpdate(updateContentModels);

                // 都不需要上传附件时保存内容.
                RequestApiImpl.getInstance().saveArticleContent(saveArticleContentBody,
                        new CodeResultObserver(Constants.REQUEST_TYPE_SAVE_ARTICLE_CONTENT, resultCallBack1));
            } else {
                // 上传修改的附件.
                UploadFileManager.uploadUpdateFile(sUpdateContentModels, resultCallBack2);
            }
        } else {
            // 上传添加的附件.
            UploadFileManager.uploadFile(sContentModels, resultCallBack2);
        }
    }

    /**
     * 修改文章内容.
     *
     * @param articleId    文章Id.
     * @param articleTitle 文章标题.
     * @param star         星标.
     * @param deleteType   文章删除状态.
     * @param iecId        文章标签.
     * @param observer     修改文章observer.
     */
    public static void updateArticle(@NonNull String articleId, String articleTitle, String star,
                                     String deleteType, String iecId, CodeResultObserver observer) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isNotEmpty(articleId)) try {
            if (StringUtil.isNotEmpty(articleId)) jsonObject.put("id", articleId);
            if (StringUtil.isNotEmpty(articleTitle)) jsonObject.put("title", articleTitle);
            if (StringUtil.isNotEmpty(star)) jsonObject.put("star", star);
            if (StringUtil.isNotEmpty(deleteType)) jsonObject.put("deletetype", deleteType);
            if (StringUtil.isNotEmpty(iecId)) jsonObject.put("iec_id", iecId);
            jsonObject.put("robot_id", MySharedPreferences.getUniqueId());
            jsonObject.put("updatedtime", TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), jsonObject.toString());
        RequestApiImpl.getInstance().updateArticle(requestBody, observer);
    }

    /**
     * 查询文章列表.
     *
     * @param pageNo   页数.
     * @param robotId  机器人Id.
     * @param time     时间.
     * @param typeId   文章Id.
     * @param star     星标.
     * @param observer 文章observer.
     */
    public static void queryArticle(int pageNo, String robotId, String title,
                                    String time, String typeId, String deleteType,
                                    String star, QueryArticleObserver observer) {
        Map<String, String> map = new HashMap<>();
        if (pageNo != -1 && pageNo > 0) map.put("pageNo", String.valueOf(pageNo));
        if (Utils.isNotEmpty(robotId)) map.put("robot_id", robotId);
        if (Utils.isNotEmpty(title)) map.put("title", title);
        if (Utils.isNotEmpty(time)) map.put("updatedtime", time);
        if (Utils.isNotEmpty(typeId)) map.put("iec_id", typeId);
        if (Utils.isNotEmpty(star)) map.put("star", star);
        if (Utils.isNotEmpty(deleteType)) map.put("deletetype", deleteType);

        RequestApiImpl.getInstance().queryArticle(map, observer);
    }

    /**
     * 根据文章id查看文章内容.
     *
     * @param articleId 文章id.
     * @param observer  文章observer.
     */
    public static void queryArticleById(int articleId, ArticleContentObserver observer) {
        RequestApiImpl.getInstance().queryArticleById(String.valueOf(articleId), observer);
    }
}
