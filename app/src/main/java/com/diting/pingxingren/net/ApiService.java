package com.diting.pingxingren.net;

import com.diting.pingxingren.model.AskCountModel;
import com.diting.pingxingren.model.AskListModel;
import com.diting.pingxingren.model.AskModel;
import com.diting.pingxingren.model.AskRobotModel;
import com.diting.pingxingren.model.ChatLogModel;
import com.diting.pingxingren.model.ChatModel;
import com.diting.pingxingren.model.ChatUserManageModel;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.model.CommonFeaturesModel;
import com.diting.pingxingren.model.CommonLanguageListModel;
import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.model.CommunicateModel;
import com.diting.pingxingren.model.InvalidQuestionModel;
import com.diting.pingxingren.model.KnowledgeModel;
import com.diting.pingxingren.model.LoginResultModel;
import com.diting.pingxingren.model.MyLuckyModel;
import com.diting.pingxingren.model.NewVersionModel;
import com.diting.pingxingren.model.NewsListModel;
import com.diting.pingxingren.model.PersonalMailModel;
import com.diting.pingxingren.model.PublicChatModel;
import com.diting.pingxingren.model.ResultMessageModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.RobotNameIsExistsModel;
import com.diting.pingxingren.model.SystemMessageModel;
import com.diting.pingxingren.model.ThirdBindModel;
import com.diting.pingxingren.model.ThirdCheckBindModel;
import com.diting.pingxingren.model.UnreadMailModel;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.model.UserInfoModel;
import com.diting.pingxingren.net.body.AddCollectionBody;
import com.diting.pingxingren.net.body.AddKnowledgeBody;
import com.diting.pingxingren.net.body.AddOrCancelFollowBody;
import com.diting.pingxingren.net.body.AddOrDeleteCommonLanguageBody;
import com.diting.pingxingren.net.body.AskChangeBody;
import com.diting.pingxingren.net.body.AskCountBody;
import com.diting.pingxingren.net.body.AskListBody;
import com.diting.pingxingren.net.body.ChatRequestBody;
import com.diting.pingxingren.net.body.CheckBindThirdBody;
import com.diting.pingxingren.net.body.CommonFeaturesBody;
import com.diting.pingxingren.net.body.CommonLanguageBody;
import com.diting.pingxingren.net.body.CompanyInfoBody;
import com.diting.pingxingren.net.body.CreateRobotInfoBody;
import com.diting.pingxingren.net.body.DeleteCollectionBody;
import com.diting.pingxingren.net.body.FansListBody;
import com.diting.pingxingren.net.body.FeedBackBody;
import com.diting.pingxingren.net.body.FollowListBody;
import com.diting.pingxingren.net.body.InvalidQuestionEditBody;
import com.diting.pingxingren.net.body.LocationBody;
import com.diting.pingxingren.net.body.LoginInfoBody;
import com.diting.pingxingren.net.body.LuckyShareBody;
import com.diting.pingxingren.net.body.OnlyNameBody;
import com.diting.pingxingren.net.body.QueryCommonLanguageBody;
import com.diting.pingxingren.net.body.RankListBody;
import com.diting.pingxingren.net.body.RegisterBody;
import com.diting.pingxingren.net.body.RequestByIdBody;
import com.diting.pingxingren.net.body.ResetPassBody;
import com.diting.pingxingren.net.body.RobotInfoBody;
import com.diting.pingxingren.net.body.RobotSpeedListBody;
import com.diting.pingxingren.net.body.SearchCollectionBody;
import com.diting.pingxingren.net.body.SystemIsReadBody;
import com.diting.pingxingren.net.body.ThirdBindBody;
import com.diting.pingxingren.net.body.ThirdIsBindBody;
import com.diting.pingxingren.net.body.UpdateRemarkBody;
import com.diting.voice.data.body.VoiceCallInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 17:39.
 * Api请求服务.
 */

public interface ApiService {

    /**
     * 登录.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.LOGIN)
    Observable<LoginResultModel> login(@Body LoginInfoBody infoBody);

    /**
     * 注册.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.REGISTER)
    Observable<ResultMessageModel> register(@Body RegisterBody registerBody);

    /**
     * 注册验证码.
     */
    @GET(RequestApi.REGISTER_CODE + "{phone}")
    Observable<ResultMessageModel> registerCode(@Path("phone") String phone);

    /**
     * 重置密码.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.RESET_PASSWORD)
    Observable<ResultMessageModel> resetPassword(@Body ResetPassBody resetPassBody);

    /**
     * 修改密码.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CHANGE_PASSWORD)
    Observable<ResultMessageModel> changePassword(@Body ResetPassBody resetPassBody);

    /**
     * 修改密码获取验证码.
     */
    @GET(RequestApi.RESET_AND_CHANGE_PASSWORD_CODE + "{phone}")
    Observable<ResultMessageModel> updatePasswordCode(@Path("phone") String phone);

    /**
     * 获取子机器人列表.
     */
    @GET(RequestApi.CHILD_ROBOT_LIST)
    Observable<List<ChildRobotModel>> getChildRobots();

    /**
     * 选择子机器人.
     */
    @GET(RequestApi.CHOOSE_CHILD_ROBOT + "{uniqueId}")
    Observable<ResponseBody> chooseChildRobot(@Path("uniqueId") String uniqueId);

    /**
     * 检查机器人姓名是否存在
     * @param name 机器人姓名
     * @return
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ROBOT_NAME_IS_EXISTS)
    Observable<RobotNameIsExistsModel> checkRobotNameIsExists(@Body OnlyNameBody name);

    /**
     * 创建机器人.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CREATE_ROBOT)
    Observable<ResultMessageModel> createRobot(@Body CreateRobotInfoBody createRobotInfoBody);

    /**
     * 对话.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CHAT_INFO)
    Observable<ChatModel> chatInfo(@Body ChatRequestBody chatRequestBody);

    /**
     * 开发对话.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.PUBLIC_CHAT_INFO)
    Observable<PublicChatModel> publicChatInfo(@Body ChatRequestBody chatRequestBody);

    /**
     * 获取用户信息.
     */
    @GET(RequestApi.USER_INFO)
    Observable<UserInfoModel> userInfo();

    /**
     * 获取用户信息.
     */
    @GET(RequestApi.ROBOT_INFO)
    Observable<RobotInfoModel> robotInfo();

    /**
     * 根据uniqueId获取机器人信息.
     */
    @GET(RequestApi.ROBOT_INFO_BY_UNIQUE_ID + "{uniqueId}")
    Observable<RobotInfoModel> getRobotInfoByUniqueId(@Path("uniqueId") String uniqueId);

    /**
     * 添加知识.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_KNOWLEDGE)
    Observable<ResultMessageModel> addKnowledge(@Body AddKnowledgeBody knowledgeBody);

    /**
     * 添加知识.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.EDIT_KNOWLEDGE)
    Observable<ResultMessageModel> editKnowledge(@Body AddKnowledgeBody knowledgeBody);

    /**
     * 删除知识.
     */
    @POST(RequestApi.DELETE_KNOWLEDGE + "{knowledgeId}")
    Observable<ResultMessageModel> deleteKnowledge(@Path("knowledgeId") String knowledgeId);

    /**
     * 查询知识
     */
    @GET(RequestApi.SEARCH_KNOWLEDGE)
    Observable<KnowledgeModel> getKnowledge(@Query("pageNo") int page);

    /**
     * 获取常用语.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.USER_COMMON_LANGUAGE)
    Observable<List<CommonLanguageModel>> getCommonLanguage(@Body QueryCommonLanguageBody queryCommonLanguageBody);

    /**
     * 保存常用语.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_COMMON_LANGUAGE)
    Observable<CommonLanguageModel> saveCommonLanguage(@Body CommonLanguageBody commonLanguageBody);

    /**
     * 删除常用语.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.DELETE_COMMON_LANGUAGE)
    Observable<CommonLanguageModel> deleteCommonLanguage(@Body CommonLanguageBody commonLanguageBody);

    /**
     * 保存常用语.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.UPDATE_COMMON_LANGUAGE)
    Observable<CommonLanguageModel> updateCommonLanguage(@Body CommonLanguageBody commonLanguageBody);

    /**
     * 保存公司信息.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_COMPANY_INFO)
    Observable<ResultMessageModel> saveCompanyInfo(@Body CompanyInfoBody companyInfoBody);

    /**
     * 保存机器人信息.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_ROBOT_INFO)
    Observable<ResultMessageModel> saveRobotInfo(@Body RobotInfoBody robotInfoBody);

    /**
     * 获取未读消息.
     */
    @GET(RequestApi.UNREAD_MAIL_COUNT)
    Observable<UnreadMailModel> getUnreadMailCount();

    /**
     * 获取公用功能信息.
     */
    @GET(RequestApi.SEARCH_COMMON_FEATURES)
    Observable<List<CommonFeaturesModel>> getCommonFeatures();

    /**
     * 获取开启公用功能信息.
     */
    @GET(RequestApi.SEARCH_OPEN_COMMON_FEATURES)
    Observable<ResponseBody> getOpenCommonFeatures();

    /**
     * 保存通用功能.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_COMMON_FEATURES)
    Observable<ResultMessageModel> saveCommonFeatures(@Body CommonFeaturesBody featuresBody);

    /**
     * 提交反馈信息.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SUBMIT_FEEDBACK)
    Observable<ResponseBody> submitFeedback(@Body FeedBackBody feedBackBody);

    /**
     * 获取关注列表数据.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.FOLLOW_LIST)
    Observable<List<CommunicateModel>> getFollowList(@Body FollowListBody followListBody);

    /**
     * 获取粉丝列表.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.FANS_LIST)
    Observable<List<CommunicateModel>> getFansList(@Body FansListBody fansListBody);

    /**
     * 添加关注.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ADD_FOLLOW)
    Observable<ResponseBody> addFollow(@Body AddOrCancelFollowBody addOrCancelFollowBody);

    /**
     * 取消关注.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CANCEL_FOLLOW)
    Observable<ResponseBody> cancelFollow(@Body AddOrCancelFollowBody addOrCancelFollowBody);

    /**
     * 添加备注.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.UPDATE_REMARK)
    Observable<ResponseBody> updateRemark(@Body UpdateRemarkBody updateRemarkBody);

    /**
     * 获价值配排行列表.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.RANK_LIST)
    Observable<List<CommunicateModel>> getRankList(@Body RankListBody rankListBody);

    /**
     * 访客管理列表.
     */
    @GET(RequestApi.CHAT_USER__MANAGE)
    Observable<ChatUserManageModel> getChatUserManage(@Query("pageNo") int page);

    /**
     * 根据手机号获取机器人详情.
     */
    @GET(RequestApi.GET_ROBOT_INFO_BY_USERNAME + "{phoneNum}")
    Observable<RobotInfoModel> getRobotInfoByPhone(@Path("phoneNum") String username);

    /**
     * 系统消息查询.
     */
    @GET(RequestApi.SYSTEM_MESSAGE)
    Observable<SystemMessageModel> getSystemMessageList(@Query("pageNo") int page);

    /**
     * 系统消息状态.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SYSTEM_MAIL_IS_READ)
    Observable<ResponseBody> systemIsRead(@Body SystemIsReadBody systemIsReadBody);

    /**
     * 私信消息查询.
     */
    @GET(RequestApi.PERSONAL_MAIL)
    Observable<PersonalMailModel> getPersonalMessageList(@Query("receive_name") String receive_name, @Query("pageNo") int pageNum);

    /**
     * 私信状态修改.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.PERSONAL_MAIL_IS_READ)
    Observable<ResponseBody> personalMailIsRead(@Body RequestByIdBody requestByIdBody);

    /**
     * 删除私信消息.
     */
//    @GET(RequestApi.PERSONAL_MAIL_DELETE + "{id}")
//    Observable<ResponseBody> personalMileDelete(@Path("id") int id);
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.PERSONAL_MAIL_DELETE + "{id}")
    Observable<ResponseBody> personalMileDelete(@Path("id") int id);

    /**
     * 通话记录查询.
     */
    @GET(RequestApi.CALL_RECORD_LIST)
    Observable<List<VoiceCallInfo>> getCallRecordList();

    /**
     * 上传位置信息.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.UPLOAD_LOCATION)
    Observable<ResultMessageModel> uploadLocation(@Body LocationBody locationBody);

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
    Observable<UploadImageModel> uploadAnnex(@Part MultipartBody.Part annexFile);

    /**
     * 检查更新.
     */
    @GET
    Observable<NewVersionModel> checkNewVersion(@Url String url);

    /**
     * 无效问题查询.
     */
    @GET(RequestApi.INVALID_QUESTION)
    Observable<InvalidQuestionModel> getInvalidQuestionList(@Query("pageNo") int pageNo);

    /**
     * 无效问题删除.
     */
    @GET(RequestApi.INVALID_QUESTION_DELETE)
    Observable<ResponseBody> invalidQuestionDelete(@Query("id_str") int id);

    /**
     * 未知问题编辑.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.INVALID_QUESTION_EDIT)
    Observable<ResponseBody> invalidQuestionEdit(@Body InvalidQuestionEditBody invalidQuestionEditBody);

    /**
     * 机器人手机号权限设置.
     */
    @GET(RequestApi.PHONE_PERMISSION_SETTING + "{state}")
    Observable<ResponseBody> setPhonePermission(@Path("state") int state);

    /**
     * 聊天记录  访客管理点击进入.
     * ?pageNo=" + pageNo + "&uuid=" + uuid
     */
    @GET(RequestApi.GET_CHAT_RECORD)
    Observable<ChatLogModel> getChatLogList(@Query("pageNo") int pageNo, @Query("uuid") String uuid);

    /**
     * 活动列表
     */
    @GET(RequestApi.GET_LUCKY_LIST)
    Observable<List<MyLuckyModel>> getLuckyList();

    /**
     * 活动分享成功手返回
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.LUCKY_SHATE)
    Observable<ResponseBody> luckyShare(@Body LuckyShareBody luckyShareBody);

    /**
     * 速配列表.
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ROBOT_SPEED_LIST)
    Observable<List<CommunicateModel>> getRobotSpeedList(@Body RobotSpeedListBody robotSpeedListBody);

    /**
     * 添加收藏
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ADD_TO_COLLECTION)
    Observable<CollectionModel> addToCollection(@Body AddCollectionBody addCollectionBody);

    /**
     * 删除收藏
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.DELETE_COLLECTION)
    Observable<CollectionModel> deleteCollection(@Body DeleteCollectionBody deleteCollectionBody);

    /**
     * 我的收藏列表
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.GET_COLLECTION_LIST)
    Observable<List<CollectionModel>> getCollectionList(@Body SearchCollectionBody searchCollectionBody);

    /**
     * 检查是否关注了该机器人
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CHECK_IS_ADD_FOLLOW)
    Observable<ResponseBody> getIsAddFollow(@Body AddOrCancelFollowBody addOrCancelFollowBody);

    /**
     * 获取常用语   和pc端同步
     */
    @GET(RequestApi.GET_COMMON_LANGUAGE_LIST + "{uniqueId}")
    Observable<List<CommonLanguageListModel>> getCommonLanguageList(@Path("uniqueId") String uniqueId);

    /**
     * 添加常用语
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ADD_OR_DELETE_COMMON_LANGUAGE)
    Observable<ResponseBody> addOrDeleteCommonLanguage(@Body AddOrDeleteCommonLanguageBody addOrDeleteCommonLanguageBody);

    /**
     * 获取会问的列表
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.GET_ASK_LIST)
    Observable<List<AskListModel>> getAskList(@Body AskListBody askListBody);

    /**
     * 获取会问的列表   带有分页
     */
    @GET(RequestApi.GET_ASK_LIST_GET)
    Observable<AskModel> getAskList(@Query("pageNo") int pageNo, @Query("robot_unicode") String robot_unicode);

    /**
     * 会问的问题回答后
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.SAVE_CHANGE_ASK)
    Observable<AskRobotModel> askChangeSave(@Body AskChangeBody askChangeBody);


    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ASK_COOUNT)
    Observable<AskCountModel> getAskCount(@Body AskCountBody askCountBody);

    /**
     * 新闻列表
     */
    @GET(RequestApi.URL_NEWS_LIST)
    Observable<List<NewsListModel>> getNewsList();

    /**
     * 我的活动是否显示小红点
     */
    @GET(RequestApi.GET_LUCKY_COUNT + "{phone}")
    Observable<ResponseBody> getLuckyCount(@Path("phone") String phone);

    /**
     * 绑定第三方时发送验证码
     */
    @GET(RequestApi.THIRD_BIND_CODE + "{phone}")
    Observable<ResponseBody> bindThirdSendCode(@Path("phone") String phone);

    /**
     * 判断是否绑定了微博
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ISBIND_WEIBO)
    Observable<ThirdBindModel> isBindWeiBo(@Body ThirdIsBindBody thirdIsBindBody);

    /**
     * 绑定微博
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.BIND_WEIBO)
    Observable<ThirdBindModel> bindWeiBo(@Body ThirdBindBody thirdBindBody);

    /**
     * 判断是否绑定了QQ
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ISBIND_QQ)
    Observable<ThirdBindModel> isBindQQ(@Body ThirdIsBindBody thirdIsBindBody);

    /**
     * 绑定QQ
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.BIND_QQ)
    Observable<ThirdBindModel> bindQQ(@Body ThirdBindBody thirdBindBody);

    /**
     * 判断是否绑定了微信
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.ISBIND_WEIXIN)
    Observable<ThirdBindModel> isBindWeiXin(@Body ThirdIsBindBody thirdIsBindBody);

    /**
     * 绑定微信
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.BIND_WEIXIN)
    Observable<ThirdBindModel> bindWeiXin(@Body ThirdBindBody thirdBindBody);

    /**
     * 判断绑定第三方的状态
     */
    @Headers(RequestApi.HEADERS_JSON)
    @POST(RequestApi.CHECK_THIRD_BIND)
    Observable<ThirdCheckBindModel> checkBindThird(@Body CheckBindThirdBody checkBindThirdBody);

}