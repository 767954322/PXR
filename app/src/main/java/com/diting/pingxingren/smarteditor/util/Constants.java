package com.diting.pingxingren.smarteditor.util;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 11, 16:29.
 * Description: .
 */

public interface Constants {

    int REQUEST_TYPE_UPDATE_ARTICLE_TITLE = 0; // 修改文章标题.
    int REQUEST_TYPE_UPDATE_ARTICLE_STAR = 1; // 修改文章星标状态.
    int REQUEST_TYPE_UPDATE_ARTICLE_CATEGORY = 2; // 修改文章分类.
    int REQUEST_TYPE_UPDATE_ARTICLE_DELETE_TYPE = 3; // 修改文章删除状态.
    int REQUEST_TYPE_SAVE_ARTICLE_CONTENT = 4; // 保存文章内容.

    String QI_NIU_HOST = "http://p4r0205k5.bkt.clouddn.com/";
    String QI_NIU_BUCKET_NAME = "android-upload-file";
    String QI_NIU_AK = "uitmSQ6vcOJzagNSf_O1r3Hgc14EIWSLwoaGA8GW";
    String QI_NIU_SK = "f9gzwmMZo73VtvsvhTVAShw87zFjezU2TPWK9XAw";
    int QI_NIU_EXPRIES = 3600;
}
