package com.diting.pingxingren.net;

import android.support.annotation.NonNull;

import com.diting.pingxingren.app.MyApplication;
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
import com.diting.pingxingren.net.cookie.PersistentCookieStore;
import com.diting.pingxingren.net.observers.AddCollectionObserver;
import com.diting.pingxingren.net.observers.AskChangeSaveObserver;
import com.diting.pingxingren.net.observers.AskCountObserver;
import com.diting.pingxingren.net.observers.AskListObserver;
import com.diting.pingxingren.net.observers.AskListPageObserver;
import com.diting.pingxingren.net.observers.CallRecordObserver;
import com.diting.pingxingren.net.observers.ChatLogObserver;
import com.diting.pingxingren.net.observers.ChatObserver;
import com.diting.pingxingren.net.observers.ChatUserManageObserver;
import com.diting.pingxingren.net.observers.CheckRobotNameObserver;
import com.diting.pingxingren.net.observers.ChildRobotsObserver;
import com.diting.pingxingren.net.observers.CollectionListObserver;
import com.diting.pingxingren.net.observers.CommonFeaturesObserver;
import com.diting.pingxingren.net.observers.CommonLanguageListObserver;
import com.diting.pingxingren.net.observers.CommonLanguageObserver;
import com.diting.pingxingren.net.observers.CommonLanguagesObserver;
import com.diting.pingxingren.net.observers.DefaultObserver;
import com.diting.pingxingren.net.observers.DeleteCollectionObserver;
import com.diting.pingxingren.net.observers.FollowListObserver;
import com.diting.pingxingren.net.observers.InvalidQuestionObserver;
import com.diting.pingxingren.net.observers.KnowledgeObserver;
import com.diting.pingxingren.net.observers.LoginObserver;
import com.diting.pingxingren.net.observers.MyLuckyObserver;
import com.diting.pingxingren.net.observers.NewVersionObserver;
import com.diting.pingxingren.net.observers.NewsListObserver;
import com.diting.pingxingren.net.observers.PersonalMessageObserver;
import com.diting.pingxingren.net.observers.PublicChatObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.RobotInfoByPhoneObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.net.observers.SystemMessageObserver;
import com.diting.pingxingren.net.observers.ThirdCheckBindObserver;
import com.diting.pingxingren.net.observers.ThirdIsBindObserver;
import com.diting.pingxingren.net.observers.UnreadMailObserver;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.net.observers.UserInfoObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.WXPayUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:48.
 * Description: .
 */

public class ApiManager {

    /**
     * 登录.
     */
    public static void login(String userName, String password, LoginObserver loginObserver) {
        LoginInfoBody loginInfoBody = new LoginInfoBody(userName, password);
        RequestApiImpl.getInstance().login(loginInfoBody, loginObserver);
    }

    /**
     * 注册.
     */
    public static void register(String userName, String password, String code, ResultMessageObserver observer) {
        RegisterBody registerBody = new RegisterBody();
        registerBody.setUserName(userName);
        registerBody.setPassword(password);
        registerBody.setMobile(userName);
        registerBody.setVerifyCode(code);
        RequestApiImpl.getInstance().register(registerBody, observer);
    }

    /**
     * 获取注册验证码.
     */
    public static void registerCode(String phone, ResultMessageObserver observer) {
        RequestApiImpl.getInstance().registerCode(phone, observer);
    }

    /**
     * 重置密码.
     */
    public static void resetOrChangePassword(String userName, String password,
                                             String verifyCode, ResultMessageObserver observer, boolean isReset) {
        ResetPassBody resetPassBody = new ResetPassBody();
        resetPassBody.setMobile(userName);
        resetPassBody.setPassword(password);
        resetPassBody.setVerifyCode(verifyCode);

        if (isReset)
            RequestApiImpl.getInstance().resetPassword(resetPassBody, observer);
        else
            RequestApiImpl.getInstance().changePassword(resetPassBody, observer);
    }

    /**
     * 修改密码验证码.
     */
    public static void updatePasswordCode(String phone, ResultMessageObserver observer) {
        RequestApiImpl.getInstance().updatePasswordCode(phone, observer);
    }

    /**
     * 获取子机器人列表.
     */
    public static void getChildRobots(ChildRobotsObserver observer) {
        RequestApiImpl.getInstance().getChildRobot(observer);
    }

    /**
     * 选择子机器人.
     */
    public static void chooseChildRobot(String uniqueId, final ResultCallBack resultCallBack) {
        if (Utils.isEmpty(uniqueId))
            resultCallBack.onError(false);

        RequestApiImpl.getInstance().chooseChildRobot(uniqueId).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    if (Utils.isNotEmpty(result))
                        resultCallBack.onResult(true);
                    else
                        resultCallBack.onError(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                resultCallBack.onError(false);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public static void createRobot(String robotName, String robotImg, String profile, String industry,
                                   String industryLevel, String birthday, String sex, boolean enable,
                                   String outTradeNo, ResultCallBack resultCallBack) {
//        if (Utils.isEmpty(outTradeNo)) {
//            resultCallBack.onError("创建机器人失败");
//            return;
//        }

        CreateRobotInfoBody createRobotInfoBody = new CreateRobotInfoBody();
        createRobotInfoBody.setName(robotName);
        createRobotInfoBody.setRobotHeadImg(robotImg);
        createRobotInfoBody.setProfile(profile);
        createRobotInfoBody.setHangye(industry);
        createRobotInfoBody.setHangyedengji("");
        createRobotInfoBody.setShengri(birthday);
        createRobotInfoBody.setSex(Integer.valueOf(sex));
        createRobotInfoBody.setEnable(enable);
        createRobotInfoBody.setOutTradeNo(outTradeNo);

        RequestApiImpl.getInstance().createRobot(createRobotInfoBody, new ResultMessageObserver(resultCallBack));
    }

    /**
     * 对话.
     */
    public static void chat(String question, String uuid, String userName,
                            String lat, String lng, boolean isVoice, String invalidAnswer1,
                            String invalidAnswer2, String invalidAnswer3, String mUniqueId,
                            boolean isPublic, ChatObserver observer, PublicChatObserver publicChatObserver) {
        ChatRequestBody chatRequestBody = new ChatRequestBody();
        chatRequestBody.setQuestion(question);
        chatRequestBody.setUuid(uuid);
        chatRequestBody.setLat(lat);
        chatRequestBody.setLng(lng);
        chatRequestBody.setSource("app");
        chatRequestBody.setUsername(userName);
        chatRequestBody.setAnswer1(invalidAnswer1);
        chatRequestBody.setAnswer2(invalidAnswer2);
        chatRequestBody.setAnswer3(invalidAnswer3);
        chatRequestBody.setUnique_id(mUniqueId);
        chatRequestBody.setIsVoice(isVoice ? 1 : 0);
        if (!isPublic) {
            RequestApiImpl.getInstance().chat(chatRequestBody, observer);
        } else {
//            chatRequestBody.setRobotHeadImg(MySharedPreferences.getRobotHeadPortrait());
            RequestApiImpl.getInstance().publicChat(chatRequestBody, publicChatObserver);
        }
    }

    /**
     * 获取用户信息.
     */
    public static void getUserInfo(UserInfoObserver userInfoObserver) {
        RequestApiImpl.getInstance().getUserInfo(userInfoObserver);
    }

    /**
     * 获取机器人信息.
     */
    public static void getRobotInfo(RobotInfoObserver observer) {
        RequestApiImpl.getInstance().getRobotInfo(observer);
    }

    /**
     * 根据uniqueId获取机器人信息.
     */
    public static void getRobotInfoByUniqueId(String uniqueId, RobotInfoObserver observer) {
        RequestApiImpl.getInstance().getRobotInfoByUniqueId(uniqueId, observer);
    }

    /**
     * 保存公司信息.
     */
    public static void saveCompanyInfo(String companyName, String headPortrait, ResultMessageObserver observer) {
        CompanyInfoBody companyInfoBody = new CompanyInfoBody();
        companyInfoBody.setName(companyName);
        companyInfoBody.setHeadPortrait(headPortrait);
        RequestApiImpl.getInstance().saveCompanyInfo(companyInfoBody, observer);
    }

    /**
     * 保存机器人信息.
     */
    public static void saveRobotInfo(String robotHeadImg, String companyName, String robotName,
                                     String profile, Boolean enable, int sex, String industry,
                                     String industryLevel, String price, String birthday,
                                     String invalidAnswer1, String invalidAnswer2, String invalidAnswer3,
                                     ResultMessageObserver observer) {
        RobotInfoBody robotInfoBody = new RobotInfoBody();
        robotInfoBody.setUniqueId(MySharedPreferences.getUniqueId());
        robotInfoBody.setRobotHeadImg(robotHeadImg);
        robotInfoBody.setCompanyName(companyName);
        robotInfoBody.setName(robotName);
        robotInfoBody.setEnable(enable);
        robotInfoBody.setHangye(industry);
        robotInfoBody.setHangyedengji(industryLevel);
        robotInfoBody.setShengri(birthday);
        robotInfoBody.setZidingyi(price);
        robotInfoBody.setSex(sex);
        robotInfoBody.setProfile(profile);
        robotInfoBody.setInvalidAnswer1(invalidAnswer1);
        robotInfoBody.setInvalidAnswer2(invalidAnswer2);
        robotInfoBody.setInvalidAnswer3(invalidAnswer3);

        RequestApiImpl.getInstance().saveRobotInfo(robotInfoBody, observer);
    }

    /**
     * 获取知识.
     */
    public static void getKnowledge(int page, KnowledgeObserver observer) {
        RequestApiImpl.getInstance().getKnowledge(page, observer);
    }

    /**
     * 检查机器人是否存在
     */
    public static void checkRobotName(String name, CheckRobotNameObserver observer) {
        OnlyNameBody body = new OnlyNameBody();
        body.setName(name);
        RequestApiImpl.getInstance().checkRobotName(body, observer);
    }

    /**
     * 添加或者编辑知识.
     */
    public static void addOrEditKnowledge(boolean isAdd, int knowledgeID, String question,
                                          String answer, String annexPath,
                                          String scene, int annexType, ResultMessageObserver observer) {
        AddKnowledgeBody knowledgeBody = new AddKnowledgeBody();
        knowledgeBody.setId(knowledgeID);
        knowledgeBody.setQuestion(question);
        knowledgeBody.setAnswer(answer);
        knowledgeBody.setActionOption("action_0");
        knowledgeBody.setScene(scene);
        knowledgeBody.setImg_url(annexType == AnnexUtil.PICTURE ? annexPath : "");
        knowledgeBody.setFile_url(annexType == AnnexUtil.FILE ? annexPath : "");
        knowledgeBody.setAudio_url(annexType == AnnexUtil.AUDIO ? annexPath : "");
        knowledgeBody.setVideo_url(annexType == AnnexUtil.VIDEO ? annexPath : "");
        knowledgeBody.setHyperlink_url(annexType == AnnexUtil.HYPERLINK ? annexPath : "");
        if (isAdd)
            RequestApiImpl.getInstance().saveKnowledge(knowledgeBody, observer);
        else
            RequestApiImpl.getInstance().editKnowledge(knowledgeBody, observer);
    }

    /**
     * 删除知识.
     */
    public static void deleteKnowledge(int knowledgeId, ResultMessageObserver observer) {
        RequestApiImpl.getInstance().deleteKnowledge(String.valueOf(knowledgeId), observer);
    }

    /**
     * 获取通用功能信息.
     */
    public static void getCommonFeatures(CommonFeaturesObserver observer) {
        RequestApiImpl.getInstance().getCommonFeatures(observer);
    }

    /**
     * 获取通用功能信息.
     */
    public static void getOpenCommonFeatures(final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().getOpenCommonFeatures().subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    try {
                        String result = responseBody.string();
                        JSONTokener jsonTokener = new JSONTokener(result);
                        JSONArray jsonArray = new JSONArray(jsonTokener);
                        int resultLength = jsonArray.length();
                        if (resultLength > 0) {
                            List<Integer> ids = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ids.add((Integer) jsonArray.get(i));
                            }

                            resultCallBack.onResult(ids);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 保存通用功能信息.
     */
    public static void saveCommonFeatures(List<Integer> openIds, List<Integer> closedIds,
                                          ResultMessageObserver observer) {
        CommonFeaturesBody featuresBody = new CommonFeaturesBody();
        featuresBody.setOpenIds(openIds);
        featuresBody.setClosedIds(closedIds);

        RequestApiImpl.getInstance().saveCommonFeatures(featuresBody, observer);
    }

    /**
     * 获取常用语.
     */
    public static void getCommonLanguage(String robotName, String shiro, CommonLanguagesObserver observer) {
        QueryCommonLanguageBody requestByUserIdBody = new QueryCommonLanguageBody();
        requestByUserIdBody.setUser_id(Utils.isNotEmpty(robotName) ? robotName : MySharedPreferences.getRobotName());
        requestByUserIdBody.setShiro(shiro);
        RequestApiImpl.getInstance().getCommonLanguage(requestByUserIdBody, observer);
    }

    /**
     * 保存或者删除常用语.
     */
    public static void saveOrDeleteCommonLanguage(boolean isSave, String ques, String shiro, int quesId, CommonLanguageObserver observer) {
        CommonLanguageBody commonLanguageBody = new CommonLanguageBody();
        commonLanguageBody.setQues(ques);
        commonLanguageBody.setUser_id(MySharedPreferences.getRobotName());
        commonLanguageBody.setShiro(shiro);
        commonLanguageBody.setQues_id(String.valueOf(quesId));

        if (isSave)
            RequestApiImpl.getInstance().saveCommonLanguage(commonLanguageBody, observer);
        else
            RequestApiImpl.getInstance().deleteCommonLanguage(commonLanguageBody, observer);
    }

    /**
     * 修改常用语.
     */
    public static void updateCommonLanguage(boolean isSave, String ques, int quesId, CommonLanguageObserver observer) {
        CommonLanguageBody commonLanguageBody = new CommonLanguageBody();
        commonLanguageBody.setQues(ques);
        commonLanguageBody.setUser_id("");
        commonLanguageBody.setShiro("");
        commonLanguageBody.setQues_id(String.valueOf(quesId));

        RequestApiImpl.getInstance().updateCommonLanguage(commonLanguageBody, observer);
    }

    /**
     * 获取未读消息.
     */
    public static void getUnreadMailCount(UnreadMailObserver observer) {
        RequestApiImpl.getInstance().getUnreadMailCount(observer);
    }

    /**
     * 上传位置信息.
     */
    public static void uploadLocation(String lat, String lng, ResultMessageObserver observer) {
        LocationBody locationBody = new LocationBody();
        locationBody.setLat(lat);
        locationBody.setLng(lng);
        RequestApiImpl.getInstance().uploadLocation(locationBody, observer);
    }

    /**
     * 上传图片.
     */
    public static void uploadImage(File imageFile, UploadImageObserver observer) {
        UploadApiImpl.getInstance().uploadImage(imageFile, observer);
    }

    /**
     * 上传附件.
     */
    public static void uploadAnnex(File annexFile, UploadAnnexObserver observer) {
        UploadApiImpl.getInstance().uploadAnnex(annexFile, observer);
    }

    /**
     * 检查更新.
     */
    public static void checkNewVersion(NewVersionObserver observer) {
        RequestApiImpl.getInstance().checkNewVersion(observer);
    }

    /**
     * 清除Cookie.
     */
    public static void clearCookie() {
        PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getInstance());
        cookieStore.removeAll();
    }

    /**
     * 获取关注的列表.
     */
    public static void getFollowList(FollowListObserver observer) {
        FollowListBody followListBody = new FollowListBody();
        followListBody.setOwn_phone(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getFollowList(followListBody, observer);
    }

    /**
     * 获取粉丝列表.
     */
    public static void getFansList(FollowListObserver observer) {
        FansListBody fansListBody = new FansListBody();
        fansListBody.setOth_phone(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getFansList(fansListBody, observer);
    }

    /**
     * 添加关注.
     */
    public static void addFollow(String oth_phone, final ResultCallBack resultCallBack) {
        AddOrCancelFollowBody addOrCancelFollowBody = new AddOrCancelFollowBody();
        addOrCancelFollowBody.setOwn_phone(MySharedPreferences.getUniqueId());
        addOrCancelFollowBody.setOth_phone(oth_phone);
        RequestApiImpl.getInstance().addFollow(addOrCancelFollowBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 取消关注.
     */
    public static void cancelFollow(String oth_phone, final ResultCallBack resultCallBack) {
        AddOrCancelFollowBody addOrCancelFollowBody = new AddOrCancelFollowBody();
        addOrCancelFollowBody.setOwn_phone(MySharedPreferences.getUniqueId());
        addOrCancelFollowBody.setOth_phone(oth_phone);
        RequestApiImpl.getInstance().cancelFollow(addOrCancelFollowBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 关注列表跳转过去的修改备注.
     */
    public static void updateRemarkFormFollow(String oth_phone, String remarks,
                                              final ResultCallBack resultCallBack) {
        UpdateRemarkBody updateRemarkBody = new UpdateRemarkBody();
        updateRemarkBody.setOwn_phone(MySharedPreferences.getUniqueId());
        updateRemarkBody.setOth_phone(oth_phone);
        updateRemarkBody.setRemarks(remarks);
        RequestApiImpl.getInstance().upDateRemark(updateRemarkBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 修改备注从粉丝列表跳过去修改备注.
     */
    public static void upDateRemarkformFans(String own_phone, final ResultCallBack resultCallBack) {
        UpdateRemarkBody updateRemarkBody = new UpdateRemarkBody();
        updateRemarkBody.setOth_phone(MySharedPreferences.getUniqueId());
        updateRemarkBody.setOwn_phone(own_phone);
        RequestApiImpl.getInstance().upDateRemark(updateRemarkBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 价值排行列表.
     */
    public static void getRankList(FollowListObserver observer) {
        RankListBody rankListBody = new RankListBody();
        rankListBody.setOwn_phone(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getRankList(rankListBody, observer);
    }

    /**
     * 根据手机号获取机器人详情.
     */
    public static void getRobotInfoByPhone(String phonrNum, RobotInfoByPhoneObserver robotInfoByPhoneObserver) {
        RequestApiImpl.getInstance().getRobotInfoByPhone(phonrNum, robotInfoByPhoneObserver);
    }

    /**
     * 访客管理.
     */
    public static void getChatUserManageList(int pageNo, ChatUserManageObserver observer) {
        RequestApiImpl.getInstance().getChatUserManageList(pageNo, observer);

    }

    /**
     * 获取系统消息.
     */
    public static void getSystemMessageList(int pageNum, SystemMessageObserver systemMessageObserver) {
        RequestApiImpl.getInstance().getSystemMessageList(pageNum, systemMessageObserver);
    }

    /**
     * 系统消息状态.
     */
    public static void systemIsRead(int ids, final ResultCallBack resultCallBack) {
        SystemIsReadBody systemIsReadBody = new SystemIsReadBody();
        systemIsReadBody.setIds(ids);
        RequestApiImpl.getInstance().systemIsRead(systemIsReadBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 获取私信列表.
     */
    public static void getPersonalMessageList(String receive_name, int pageNum, PersonalMessageObserver personalMessageObserver) {
        RequestApiImpl.getInstance().getPersonalMessageList(receive_name, pageNum, personalMessageObserver);
    }

    /**
     * 私信状态修改.
     */
    public static void personalMailIsRead(int id, final ResultCallBack resultCallBack) {
        RequestByIdBody requestByIdBody = new RequestByIdBody();
        requestByIdBody.setId(id);
        RequestApiImpl.getInstance().personalMailIsRead(requestByIdBody).subscribe(new DisposableObserver<ResponseBody>() {

            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 删除私信消息.
     */
    public static void personalMileDelete(int id, final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().personalMileDelete(id).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 通话记录.
     */
    public static void getCallRecordList(CallRecordObserver callRecordObserver) {
        RequestApiImpl.getInstance().getCallRecordList(callRecordObserver);
    }

    /**
     * 意见反馈.
     */
    public static void submitFeedback(String feedback_content, final ResultCallBack resultCallBack) {
        FeedBackBody feedBackBody = new FeedBackBody();
        feedBackBody.setContactInformation(feedback_content);
        feedBackBody.setSuggestion(feedback_content);
        RequestApiImpl.getInstance().submitFeedBack(feedBackBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 无效问题列表.
     */
    public static void getInvalidQuestionList(int pageNum, InvalidQuestionObserver observer) {
        RequestApiImpl.getInstance().getInvalidQuestionList(pageNum, observer);
    }

    /**
     * 新闻列表
     */
    public static void getNewsList(NewsListObserver observer) {
        RequestApiImpl.getInstance().newsList(observer);
    }

    /**
     * 删除未知问题
     */
    public static void invalidQuestionDelete(int id, final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().invalidQuestionDelete(id).subscribe(new DisposableObserver<ResponseBody>() {
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 编辑未知问题.
     */
    public static void invalidQuestionEdit(String question, String answer, final ResultCallBack resultCallBack) {
        InvalidQuestionEditBody invalidQuestionEditBody = new InvalidQuestionEditBody();
        invalidQuestionEditBody.setQuestion(question);
        invalidQuestionEditBody.setAnswer(answer);
        RequestApiImpl.getInstance().invalidQuestionEdit(invalidQuestionEditBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 机器人手机号权限设置.
     */
    public static void setPhonePermission(int state, final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().setPhonePermission(state).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 聊天记录  访客管理点击进入.
     */
    public static void getChatLogList(int pageNo, String uuid, ChatLogObserver chatLogObserver) {
        RequestApiImpl.getInstance().getChatLogList(pageNo, uuid, chatLogObserver);
    }

    /**
     * 微信统一下单.
     */
    public static void wxUnifiedOrder(String unifiedOrderBody, final ResultCallBack resultCallBack) {
        MediaType mediaType = MediaType.parse("application/xml");
        final RequestBody requestBody = RequestBody.create(mediaType, unifiedOrderBody);
        Call<ResponseBody> call = WXRequestApiImpl.getInstance().unifiedOrder(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Map<String, String> map = WXPayUtils.getInstance().decodeXml(result);
                    String returnCode = map.get("return_code");
                    String returnMsg = map.get("return_msg");
                    if (returnCode.equals("SUCCESS")) {
                        String resultCode = map.get("result_code");
                        if (resultCode.equals("SUCCESS")) {
                            String prepayId = map.get("prepay_id");
                            WXPayUtils.getInstance().pay(prepayId);
                            resultCallBack.onResult(resultCode);
                        } else {
                            String errCode = map.get("err_code");
                            String errCodeDes = map.get("err_code_des");
                            resultCallBack.onError(errCode + ", " + errCodeDes);
                        }
                    } else {
                        resultCallBack.onError(returnMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                DefaultObserver.errorMessageHandling(t, resultCallBack);
            }
        });
    }

    /**
     * 速配列表
     */
    public static void getRobotSpeedList(String hangye, FollowListObserver followListObserver) {
        RobotSpeedListBody robotSpeedListBody = new RobotSpeedListBody();
        robotSpeedListBody.setHangye(hangye);
        robotSpeedListBody.setUniqueId(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getRobotSpeedList(robotSpeedListBody, followListObserver);
    }

    /**
     * 获取是否关注了此机器人的状态
     */
    public static void getIsAddFollow(String own_phone, final ResultCallBack resultCallBack) {
        AddOrCancelFollowBody addOrCancelFollowBody = new AddOrCancelFollowBody();
        addOrCancelFollowBody.setOwn_phone(MySharedPreferences.getUniqueId());
        addOrCancelFollowBody.setOth_phone(own_phone);
        RequestApiImpl.getInstance().getIsAddFollow(addOrCancelFollowBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 获取活动列表
     */
    public static void getLuckyList(MyLuckyObserver observer) {
        RequestApiImpl.getInstance().getLuckyList(observer);
    }

    /*
    *活动分享后请求
     */
    public static void luckyShare(int id, final ResultCallBack resultCallBack) {
        LuckyShareBody luckyShareBody = new LuckyShareBody();
        luckyShareBody.setMobile(MySharedPreferences.getUsername());
        luckyShareBody.setId(id);
        RequestApiImpl.getInstance().luckyShare(luckyShareBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 添加收藏
     */
    public static void addToCollection(String url, String text, int sort, String chatrobotname, String chatuniqueid, AddCollectionObserver addCollectionObserver) {
        AddCollectionBody addCollectionBody = new AddCollectionBody();
        addCollectionBody.setUrl(url);
        addCollectionBody.setText(text);
        addCollectionBody.setSort(sort);
        addCollectionBody.setTimer(TimeUtil.getNowTimeMills());
        addCollectionBody.setOnlineuniqueid(MySharedPreferences.getUniqueId());
        addCollectionBody.setOnlinerobotname(MySharedPreferences.getCompanyName());
        addCollectionBody.setChatuniqueid(chatuniqueid);
        addCollectionBody.setChatrobotname(chatrobotname);
        RequestApiImpl.getInstance().addToCollection(addCollectionBody, addCollectionObserver);
    }

    /**
     * 删除收藏
     */
    public static void deleteCollection(String id, DeleteCollectionObserver deleteCollectionObserver) {
        DeleteCollectionBody deleteCollectionBody = new DeleteCollectionBody();
        deleteCollectionBody.setId(id);
        RequestApiImpl.getInstance().deleteCollection(deleteCollectionBody, deleteCollectionObserver);
    }

    /**
     * 收藏列表
     */
    public static void getCollectionList(String sort, CollectionListObserver collectionListObserver) {
        SearchCollectionBody searchCollectionBody = new SearchCollectionBody();
        searchCollectionBody.setOnlineuniqueid(MySharedPreferences.getUniqueId());
        if (!Utils.isEmpty(sort)) {
            searchCollectionBody.setSort(sort);
        }
        RequestApiImpl.getInstance().getCollectionList(searchCollectionBody, collectionListObserver);
    }

    /**
     * 常用语列表  与pc端同步
     */
    public static void getCommonLanguageList(String uniqueId, CommonLanguageListObserver commonLanguageListObserver) {
        RequestApiImpl.getInstance().getCommonLanguageList(uniqueId, commonLanguageListObserver);
    }

    /**
     * 添加或删除常用语
     */
    public static void addOrDeleteCommonLanguage(int id, boolean highFrequencyQuestion, boolean ownVisible, final ResultCallBack resultCallBack) {
        AddOrDeleteCommonLanguageBody addOrDeleteCommonLanguageBody = new AddOrDeleteCommonLanguageBody();
        addOrDeleteCommonLanguageBody.setId(id);
        addOrDeleteCommonLanguageBody.setHighFrequencyQuestion(highFrequencyQuestion);
        if (highFrequencyQuestion) {
            addOrDeleteCommonLanguageBody.setOwnVisible(ownVisible);
        }


        RequestApiImpl.getInstance().addOrDeleteCommonLanguage(addOrDeleteCommonLanguageBody).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 获取会问列表数据
     */
    public static void getAskList(AskListObserver askListObserver) {
        AskListBody askListBody = new AskListBody();
        askListBody.setRobot_unicode(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getAskList(askListBody, askListObserver);

    }


    /**
     * 获取会问列表   带有分页
     */
    public static void getAskList(int pageNo, String robot_unicode, AskListPageObserver askListObserver) {
        RequestApiImpl.getInstance().getAskList(pageNo, robot_unicode, askListObserver);
    }

    /**
     * 会问改变之后的
     */
    public static void askChangeSave(String ask_id, AskChangeSaveObserver askChangeSaveObserver) {
        AskChangeBody askChangeBody = new AskChangeBody();
        askChangeBody.setAsk_id(ask_id);
        askChangeBody.setRobot_unicode(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().askChangeSave(askChangeBody, askChangeSaveObserver);
    }

    /**
     * 获取会问的数量
     */
    public static void getAskCount(AskCountObserver askCountObserver) {
        AskCountBody askCountBody = new AskCountBody();
        askCountBody.setRobot_unicode(MySharedPreferences.getUniqueId());
        RequestApiImpl.getInstance().getAskCount(askCountBody, askCountObserver);

    }

    /**
     * 获取活动的小红点
     */
    public static void getLuckyCount(final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().getLuckyCount(MySharedPreferences.getUsername()).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }


    /**
     * 绑定第三方时发送验证码
     */
    public static void bindThirdSendCode(String phone, final ResultCallBack resultCallBack) {
        RequestApiImpl.getInstance().bindThirdSendCode(phone).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String result = null;
                try {
                    result = responseBody.string();
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject jsonObject = new JSONObject(jsonTokener);
                    resultCallBack.onResult(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DefaultObserver.errorMessageHandling(e, resultCallBack);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 判断是否绑定了微博
     */
    public static void isBindWeiBo(String accessToken, String uid, ThirdIsBindObserver thirdIsBindObserver) {

        ThirdIsBindBody thirdIsBindBody = new ThirdIsBindBody();
        thirdIsBindBody.setAccessToken(accessToken);
        thirdIsBindBody.setUid(uid);
        RequestApiImpl.getInstance().isBindWeiBo(thirdIsBindBody, thirdIsBindObserver);
    }

    /**
     * 绑定微博
     */
    public static void bindWeiBo(String userName,
                                 String verifyCode,
                                 String uid,
                                 String accessToken, ThirdIsBindObserver thirdIsBindObserver) {
        ThirdBindBody thirdBindBody = new ThirdBindBody();
        thirdBindBody.setUserName(userName);
        thirdBindBody.setVerifyCode(verifyCode);
        thirdBindBody.setUid(uid);
        thirdBindBody.setAccessToken(accessToken);
        RequestApiImpl.getInstance().bindWeiBo(thirdBindBody, thirdIsBindObserver);
    }

    /**
     * 判断是否绑定了QQ
     */
    public static void isBindQQ(String accessToken, String uid, ThirdIsBindObserver thirdIsBindObserver) {
        ThirdIsBindBody thirdIsBindBody = new ThirdIsBindBody();
        thirdIsBindBody.setAccessToken(accessToken);
        thirdIsBindBody.setUid(uid);
        RequestApiImpl.getInstance().isBindQQ(thirdIsBindBody, thirdIsBindObserver);
    }

    /**
     * 绑定QQ
     */
    public static void bindQQ(String userName,
                              String verifyCode,
                              String uid,
                              String accessToken, ThirdIsBindObserver thirdIsBindObserver) {

        ThirdBindBody thirdBindBody = new ThirdBindBody();
        thirdBindBody.setUserName(userName);
        thirdBindBody.setVerifyCode(verifyCode);
        thirdBindBody.setUid(uid);
        thirdBindBody.setAccessToken(accessToken);
        RequestApiImpl.getInstance().bindQQ(thirdBindBody, thirdIsBindObserver);
    }

    /**
     * 判断是否绑定了微信
     */
    public static void isBindWeiXin(String accessToken, String uid, ThirdIsBindObserver thirdIsBindObserver) {
        ThirdIsBindBody thirdIsBindBody = new ThirdIsBindBody();
        thirdIsBindBody.setAccessToken(accessToken);
        thirdIsBindBody.setUid(uid);
        RequestApiImpl.getInstance().isBindWeiXin(thirdIsBindBody, thirdIsBindObserver);
    }

    /**
     * 绑定微信
     */
    public static void bindWeiXin(String userName,
                                  String verifyCode,
                                  String uid,
                                  String accessToken, ThirdIsBindObserver thirdIsBindObserver) {
        ThirdBindBody thirdBindBody = new ThirdBindBody();
        thirdBindBody.setUserName(userName);
        thirdBindBody.setVerifyCode(verifyCode);
        thirdBindBody.setUid(uid);
        thirdBindBody.setAccessToken(accessToken);
        RequestApiImpl.getInstance().bindWeiXin(thirdBindBody, thirdIsBindObserver);
    }

    public static void checkBindThird(String userName, ThirdCheckBindObserver thirdCheckBindObserver) {
        CheckBindThirdBody checkBindThirdBody = new CheckBindThirdBody();
        checkBindThirdBody.setUserName(userName);
        RequestApiImpl.getInstance().checkBindThird(checkBindThirdBody, thirdCheckBindObserver);

    }
}
