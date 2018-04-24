package com.diting.pingxingren.smarteditor.view;

import com.diting.pingxingren.smarteditor.model.ArticleModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 25, 10:34.
 * Description: .
 */

public interface EditorHomeView extends BaseView {

    /**
     * 修改文章列表集合.
     *
     * @param itemsBeans 文章集合.
     * @param setNewData 是否设置新数据.
     */
    void updateArticlesView(List<ArticleModel.ItemsBean> itemsBeans, boolean setNewData);

    /**
     * 加载更多完毕.
     */
    void loadEnd(boolean end);

    /**
     * 保存配置信息.
     *
     * @param value  配置信息的value.
     */
    void savePreferences(String value);

    /**
     * 修改菜单标题.
     *
     * @param menuTitle 菜单标题.
     */
    void updateLeftMenu(String menuTitle);

    /**
     * 回收站.
     */
    void startToRecycleBin();

    /**
     * 修改文章状态.
     *
     * @param codeResultModel 修改结果model.
     */
    void showUpdateState(CodeResultModel codeResultModel);

    /**
     * 显示修改标题对话框.
     */
    void showUpdateTitleDialog();

    /**
     * 显示移动分类对话框.
     */
    void showMobileCategoryDialog();

    /**
     * 显示星标对话框.
     */
    void showUpdateStarDialog();

    /**
     * 显示删除文章对话框.
     */
    void showDeleteAlertDialog();

    /**
     * 显示吐司.
     *
     * @param toastText 吐司内容.
     * @param isLong 是否长显示.
     */
    void showToast(String toastText, boolean isLong);

    /**
     * 显示Loading界面.
     */
    void showLoadingView();

    /**
     * 隐藏Loading界面.
     */
    void dismissLoadingView();
}
