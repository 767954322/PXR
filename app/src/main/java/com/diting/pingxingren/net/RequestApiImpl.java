package com.diting.pingxingren.net;

import com.diting.pingxingren.model.ChatModel;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.CommonFeaturesModel;
import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.model.KnowledgeModel;
import com.diting.pingxingren.model.LoginResultModel;
import com.diting.pingxingren.model.PublicChatModel;
import com.diting.pingxingren.model.ResultMessageModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.UnreadMailModel;
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
import com.diting.pingxingren.net.observers.AddCollectionObserver;
import com.diting.pingxingren.net.observers.AskChangeSaveObserver;
import com.diting.pingxingren.net.observers.AskCountObserver;
import com.diting.pingxingren.net.observers.AskListObserver;
import com.diting.pingxingren.net.observers.AskListPageObserver;
import com.diting.pingxingren.net.observers.CallRecordObserver;
import com.diting.pingxingren.net.observers.ChatLogObserver;
import com.diting.pingxingren.net.observers.ChatUserManageObserver;
import com.diting.pingxingren.net.observers.CheckRobotNameObserver;
import com.diting.pingxingren.net.observers.CollectionListObserver;
import com.diting.pingxingren.net.observers.CommonLanguageListObserver;
import com.diting.pingxingren.net.observers.DeleteCollectionObserver;
import com.diting.pingxingren.net.observers.FollowListObserver;
import com.diting.pingxingren.net.observers.InvalidQuestionObserver;
import com.diting.pingxingren.net.observers.MyLuckyObserver;
import com.diting.pingxingren.net.observers.NewVersionObserver;
import com.diting.pingxingren.net.observers.NewsListObserver;
import com.diting.pingxingren.net.observers.PersonalMessageObserver;
import com.diting.pingxingren.net.observers.RobotInfoByPhoneObserver;
import com.diting.pingxingren.net.observers.SystemMessageObserver;
import com.diting.pingxingren.net.observers.ThirdCheckBindObserver;
import com.diting.pingxingren.net.observers.ThirdIsBindObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/5, 18:04.
 * 访问接口实现类.
 */

class RequestApiImpl {

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

    void login(LoginInfoBody infoBody,
               Observer<LoginResultModel> observer) {
        mApiService.login(infoBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void register(RegisterBody registerBody, Observer<ResultMessageModel> observer) {
        mApiService.register(registerBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void registerCode(String phone, Observer<ResultMessageModel> observer) {
        mApiService.registerCode(phone).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void resetPassword(ResetPassBody resetPassBody, Observer<ResultMessageModel> observer) {
        mApiService.resetPassword(resetPassBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void changePassword(ResetPassBody resetPassBody, Observer<ResultMessageModel> observer) {
        mApiService.changePassword(resetPassBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void updatePasswordCode(String phone, Observer<ResultMessageModel> observer) {
        mApiService.updatePasswordCode(phone).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getChildRobot(Observer<List<ChildRobotModel>> observer) {
        mApiService.getChildRobots().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    Observable<ResponseBody> chooseChildRobot(String uniqueId) {
        return mApiService.chooseChildRobot(uniqueId).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void createRobot(CreateRobotInfoBody robotInfoBody, Observer<ResultMessageModel> observer) {
        mApiService.createRobot(robotInfoBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void chat(ChatRequestBody chatRequestBody, Observer<ChatModel> observer) {
        mApiService.chatInfo(chatRequestBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void publicChat(ChatRequestBody chatRequestBody, Observer<PublicChatModel> observer) {
        mApiService.publicChatInfo(chatRequestBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getUserInfo(Observer<UserInfoModel> observer) {
        mApiService.userInfo().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getRobotInfo(Observer<RobotInfoModel> observer) {
        mApiService.robotInfo().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getRobotInfoByUniqueId(String uniqueId, Observer<RobotInfoModel> observer) {
        mApiService.getRobotInfoByUniqueId(uniqueId).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getKnowledge(int page, Observer<KnowledgeModel> observer) {
        mApiService.getKnowledge(page).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void saveKnowledge(AddKnowledgeBody knowledgeBody, Observer<ResultMessageModel> observer) {
        mApiService.addKnowledge(knowledgeBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void editKnowledge(AddKnowledgeBody knowledgeBody, Observer<ResultMessageModel> observer) {
        mApiService.editKnowledge(knowledgeBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void deleteKnowledge(String knowledgeId, Observer<ResultMessageModel> observer) {
        mApiService.deleteKnowledge(knowledgeId).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getCommonLanguage(QueryCommonLanguageBody queryCommonLanguageBody, Observer<List<CommonLanguageModel>> observer) {
        mApiService.getCommonLanguage(queryCommonLanguageBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void saveCommonLanguage(CommonLanguageBody commonLanguageBody, Observer<CommonLanguageModel> observer) {
        mApiService.saveCommonLanguage(commonLanguageBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void updateCommonLanguage(CommonLanguageBody commonLanguageBody, Observer<CommonLanguageModel> observer) {
        mApiService.updateCommonLanguage(commonLanguageBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void deleteCommonLanguage(CommonLanguageBody commonLanguageBody, Observer<CommonLanguageModel> observer) {
        mApiService.deleteCommonLanguage(commonLanguageBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getUnreadMailCount(Observer<UnreadMailModel> observer) {
        mApiService.getUnreadMailCount().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void saveCompanyInfo(CompanyInfoBody companyInfoBody, Observer<ResultMessageModel> observer) {
        mApiService.saveCompanyInfo(companyInfoBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void saveRobotInfo(RobotInfoBody robotInfoBody, Observer<ResultMessageModel> observer) {
        mApiService.saveRobotInfo(robotInfoBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getCommonFeatures(Observer<List<CommonFeaturesModel>> observer) {
        mApiService.getCommonFeatures().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    Observable<ResponseBody> getOpenCommonFeatures() {
        return mApiService.getOpenCommonFeatures().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void saveCommonFeatures(CommonFeaturesBody featuresBody, Observer<ResultMessageModel> observer) {
        mApiService.saveCommonFeatures(featuresBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getFollowList(FollowListBody followListBody, FollowListObserver followListObserver) {
        mApiService.getFollowList(followListBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(followListObserver);
    }

    void getFansList(FansListBody fansListBody, FollowListObserver followListObserver) {
        mApiService.getFansList(fansListBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(followListObserver);
    }

    Observable<ResponseBody> addFollow(AddOrCancelFollowBody addOrCancelFollowBody) {
        return mApiService.addFollow(addOrCancelFollowBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> cancelFollow(AddOrCancelFollowBody addOrCancelFollowBody) {
        return mApiService.cancelFollow(addOrCancelFollowBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> upDateRemark(UpdateRemarkBody updateRemarkBody) {
        return mApiService.updateRemark(updateRemarkBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getRankList(RankListBody rankListBody, FollowListObserver followListObserver) {
        mApiService.getRankList(rankListBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(followListObserver);
    }

    void getChatUserManageList(int pageNo, ChatUserManageObserver chatUserManageObserver) {
        mApiService.getChatUserManage(pageNo).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(chatUserManageObserver);
    }

    void getRobotInfoByPhone(String phoneNum, RobotInfoByPhoneObserver observer) {
        mApiService.getRobotInfoByPhone(phoneNum).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getSystemMessageList(int pageNum, SystemMessageObserver systemMessageObserver) {
        mApiService.getSystemMessageList(pageNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(systemMessageObserver);
    }

    Observable<ResponseBody> systemIsRead(SystemIsReadBody systemIsReadBody) {
        return mApiService.systemIsRead(systemIsReadBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getPersonalMessageList(String receive_name, int pageNum, PersonalMessageObserver personalMessageObserver) {
        mApiService.getPersonalMessageList(receive_name, pageNum).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(personalMessageObserver);
    }

    Observable<ResponseBody> personalMailIsRead(RequestByIdBody requestByIdBody) {
        return mApiService.personalMailIsRead(requestByIdBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> personalMileDelete(int id) {
        return mApiService.personalMileDelete(id)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void uploadLocation(LocationBody locationBody, Observer<ResultMessageModel> observer) {
        mApiService.uploadLocation(locationBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void uploadImage(File imageFile, UploadImageObserver observer) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        mApiService.uploadImage(part).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getCallRecordList(CallRecordObserver callRecordObserver) {
        mApiService.getCallRecordList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callRecordObserver);
    }

    Observable<ResponseBody> submitFeedBack(FeedBackBody feedBackBody) {
        return mApiService.submitFeedback(feedBackBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getInvalidQuestionList(int pageNum, InvalidQuestionObserver observer) {
        mApiService.getInvalidQuestionList(pageNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observable<ResponseBody> invalidQuestionDelete(int id) {
        return mApiService.invalidQuestionDelete(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> invalidQuestionEdit(InvalidQuestionEditBody invalidQuestionEditBody) {
        return mApiService.invalidQuestionEdit(invalidQuestionEditBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> setPhonePermission(int state) {
        return mApiService.setPhonePermission(state)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getChatLogList(int pageNo, String uuid, ChatLogObserver observer) {
        mApiService.getChatLogList(pageNo, uuid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void getRobotSpeedList(RobotSpeedListBody robotSpeedListBody, FollowListObserver followListObserver) {
        mApiService.getRobotSpeedList(robotSpeedListBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(followListObserver);
    }

    Observable<ResponseBody> getIsAddFollow(AddOrCancelFollowBody addOrCancelFollowBody) {
        return mApiService.getIsAddFollow(addOrCancelFollowBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getLuckyList(MyLuckyObserver observer) {
        mApiService.getLuckyList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    Observable<ResponseBody> luckyShare(LuckyShareBody luckyShareBody) {
        return mApiService.luckyShare(luckyShareBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void addToCollection(AddCollectionBody addCollectionBody, AddCollectionObserver addCollectionObserver) {
        mApiService.addToCollection(addCollectionBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(addCollectionObserver);
    }

    void deleteCollection(DeleteCollectionBody deleteCollectionBody, DeleteCollectionObserver deleteCollectionObserver) {
        mApiService.deleteCollection(deleteCollectionBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(deleteCollectionObserver);
    }

    void getCollectionList(SearchCollectionBody searchCollectionBody, CollectionListObserver collectionListObserver) {
        mApiService.getCollectionList(searchCollectionBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(collectionListObserver);
    }

    void getCommonLanguageList(String uniqueId, CommonLanguageListObserver commonLanguageListObserver) {
        mApiService.getCommonLanguageList(uniqueId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(commonLanguageListObserver);
    }

    Observable<ResponseBody> addOrDeleteCommonLanguage(AddOrDeleteCommonLanguageBody addOrDeleteCommonLanguageBody) {
        return mApiService.addOrDeleteCommonLanguage(addOrDeleteCommonLanguageBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void getAskList(AskListBody askListBody, AskListObserver askListObserver) {
        mApiService.getAskList(askListBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(askListObserver);

    }


    void getAskList(int pageNo, String robot_unicode, AskListPageObserver askListObserver) {
        mApiService.getAskList(pageNo, robot_unicode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(askListObserver);

    }


    void askChangeSave(AskChangeBody askChangeBody, AskChangeSaveObserver askChangeSaveObserver) {
        mApiService.askChangeSave(askChangeBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(askChangeSaveObserver);
    }

    void getAskCount(AskCountBody askCountBody, AskCountObserver askCountObserver) {
        mApiService.getAskCount(askCountBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(askCountObserver);
    }

    void uploadAnnex(File annexFile, UploadImageObserver observer) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), annexFile);
        MultipartBody.Part part =
                MultipartBody.Part.createFormData("file", annexFile.getName(), requestBody);
        mApiService.uploadAnnex(part).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void checkNewVersion(NewVersionObserver observer) {
        mApiService.checkNewVersion(RequestApi.CHECK_VERSION).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    void newsList(NewsListObserver observer) {
        mApiService.getNewsList().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    Observable<ResponseBody> getLuckyCount(String phone) {
        return mApiService.getLuckyCount(phone).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<ResponseBody> bindThirdSendCode(String phone) {
        return mApiService.bindThirdSendCode(phone).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void isBindWeiBo(ThirdIsBindBody thirdIsBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.isBindWeiBo(thirdIsBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);

    }

    void bindWeiBo(ThirdBindBody thirdBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.bindWeiBo(thirdBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);
    }

    void isBindQQ(ThirdIsBindBody thirdIsBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.isBindQQ(thirdIsBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);

    }

    void bindQQ(ThirdBindBody thirdBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.bindQQ(thirdBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);
    }

    void isBindWeiXin(ThirdIsBindBody thirdIsBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.isBindWeiXin(thirdIsBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);

    }

    void bindWeiXin(ThirdBindBody thirdBindBody, ThirdIsBindObserver thirdIsBindObserver) {
        mApiService.bindWeiXin(thirdBindBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdIsBindObserver);
    }

    void checkBindThird(CheckBindThirdBody checkBindThirdBody, ThirdCheckBindObserver thirdCheckBindObserver) {
        mApiService.checkBindThird(checkBindThirdBody).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(thirdCheckBindObserver);
    }

    void checkRobotName(OnlyNameBody body, CheckRobotNameObserver observer) {
        mApiService.checkRobotNameIsExists(body).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
