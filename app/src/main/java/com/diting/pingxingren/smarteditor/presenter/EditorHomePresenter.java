package com.diting.pingxingren.smarteditor.presenter;

import com.diting.pingxingren.smarteditor.model.ArticleModel;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 24, 18:32.
 * Description: .
 */

public interface EditorHomePresenter extends BasePresenter {

    /**
     * 获取文章.
     *
     * @param pageNo 页数.
     * @param time 时间.
     * @param typeId 类型ID.
     * @param star 星标.
     */
    void getArticles(int pageNo, String time, String typeId, String star);

    /**
     * 修改文章标题.
     *
     * @param itemsBean 文章.
     * @param updateCode 修改内容code. 0: 修改标题 1: 修改星标 2: 修改删除状态 3: 修改分类.
     */
    void updateArticle(ArticleModel.ItemsBean itemsBean, int updateCode);

    /**
     * 移动文章分类.
     *
     * @param itemsBean 文章.
     * @param category 文章分类.
     */
    void mobileArticleCategory(ArticleModel.ItemsBean itemsBean, String category);

    /**
     * 菜单操作.
     *
     * @param operatingPosition 1: 查看全部文章. 2: 查看工作文章. 3: 查看生活文章. 4: 查看星标文章. 5: 回收站.
     * @param isLeft 是否是左菜单.
     */
    void menuOperating(int operatingPosition, boolean isLeft);

    /**
     * 文章操作.
     *
     * @param operatingPosition 0: 重命名. 1: 移动分类.
     */
    void articleOperating(int operatingPosition);
}
