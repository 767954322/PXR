package com.diting.pingxingren.smarteditor.net;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 17:37.
 * 请求Api接口.
 */

public interface RequestApi {

    String HEADERS_JSON = "Content-Type: application/json"; // Json请求头.
    String HEADERS_XML = "Content-Type: application/xml"; // Xml请求头.

    /* 智能小编. */
    String EDITOR_SEARCH_HOME = "editor/search-page";
    String EDITOR_SEARCH_ARTICLE = "editortion/search-editortion";
    String EDITOR_SAVE_ARTICLE_TITLE = "editor/save";
    String EDITOR_SAVE_ARTICLE_CONTENT = "editortion/save";
    String EDITOR_UPDATE = "editor/update";

    String EDITOR_ARTICLE_DETAILS = ApiConnection.getInstance().getRetrofit().baseUrl().toString() +
            "m/dtArticle?editorId=";
}