package com.diting.pingxingren.util;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.voice.data.body.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.diting.pingxingren.util.Const.VERSION_URL;

/**
 * Created by asus on 2017/1/3.
 */

public class Diting {
    private static RequestQueue mQueue = MyApplication.requestQueue;
    private static ImageLoader imageLoader;

    //登录
    public static void login(String username, String password, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", username);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequestWithSession(Const.URL_LOGIN, json, callBack);
    }

    //注册
    public static void register(String username, String password, String verifyCode, String openId, String unionId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", username);
            json.put("password", password);
            json.put("mobile", username);
            json.put("verifyCode", verifyCode);
            if (openId != null) {
                json.put("openId", openId);
            }
            if (unionId != null) {
                json.put("unionId", unionId);
            }
            json.put("source", "5");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequestWithSession(Const.URL_REGISTER, json, callBack);
    }

    //注册发送短信验证码
    public static void sendMessage(String username, HttpCallBack callBack) {
        jsonRequest(Const.URL_SEND_MESSAGE + username, null, callBack);
    }

    //修改密码
    public static void changePassword(String username, String password, String verifyCode, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile", username);
            json.put("newPassword", password);
            json.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_CHANGE_PASSWORD, json, callBack);
    }

    ///找回密码
    public static void resetPassword(String username, String password, String verifyCode, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile", username);
            json.put("newPassword", password);
            json.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_RESET_PASSWORD, json, callBack);
    }

    //找回密码发送短信验证码
    public static void sendResetMessage(String username, HttpCallBack callBack) {
        jsonRequest(Const.URL_SEND_RESET_MESSAGE + username, null, callBack);
    }

    //意见反馈
    public static void feedback(String content, String contact, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("suggestion", content);
            if (!TextUtils.isEmpty(contact)) {
                json.put("contactInformation", contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_FEEDBACK, json, callBack);
    }

    //设置机器人信息
    public static void setRobotInfo(String companyName, String robotName, String profile, String welcomes,
                                    Boolean enable, String industryIds, String voicePassword, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", robotName);
            if (!Utils.isEmpty(profile)) {
                json.put("profile", profile);
            }
            if (!Utils.isEmpty(welcomes)) {
                json.put("welcomes", welcomes);
            }
            if (enable != null) {
                json.put("enable", enable);
            }
            if (industryIds != null) {
                json.put("industryIds", industryIds);
            }
            if (!Utils.isEmpty(voicePassword)) {
                json.put("voicePassword", voicePassword);
            }
            json.put("companyName", companyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ROBOT_SETTING, json, callBack);
    }

    //设置机器人信息
    public static void setRobotInfo(String companyName, String robotName, String profile,
                                    Boolean enable, String sex, String industry,
                                    String industryLevel, String price,
                                    String birthday, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", robotName);
            if (!Utils.isEmpty(profile)) {
                json.put("profile", profile);
            }

            if (enable != null) {
                json.put("enable", enable);
            }
            if (!Utils.isEmpty(industry))
                json.put("hangye", industry);
            if (!Utils.isEmpty(industryLevel))
                json.put("hangyedengji", industryLevel);
            if (!Utils.isEmpty(price))
                json.put("zidingyi", price);
            if (!Utils.isEmpty(birthday))
                json.put("shengri", birthday);

            json.put("companyName", companyName);
            json.put("sex", sex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ROBOT_SETTING, json, callBack);
    }

    //设置公司信息
    public static void setCompanyInfo(String companyName, Integer industry, String headPortrait, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            if (companyName != null) {
                json.put("name", companyName);
            }
            if (industry != null) {
                json.put("industry", industry);
            }
            if (!Utils.isEmpty(headPortrait)) {
                json.put("headPortrait", headPortrait);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_COMPANY_SETTING, json, callBack);
    }

    //设置公司信息
    public static void setCompanyInfo(String companyName, String headPortrait, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            if (companyName != null) {
                json.put("name", companyName);
            }
            if (!Utils.isEmpty(headPortrait)) {
                json.put("headPortrait", headPortrait);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_COMPANY_SETTING, json, callBack);
    }

    //设置小游戏信息
    public static void setGameInfo(String textsup, String textsdown, String num, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            if (textsup != null) {
                json.put("textsup", textsup);
            }
            if (textsdown != null) {
                json.put("textsdown", textsdown);
            }
            json.put("num", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_SAVE_GAME_INFO, json, callBack);
    }

    //查询关注行业
    public static void searchConcernIndustry(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_SEARCH_CONCERN_INDUSTRY, callBack);
    }
    //查询关注行业  速配界面

    public static void searchHangye(String hangye, HttpJsonArrayCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("hangye", hangye);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArrayRequest(Const.URL_SEARCH_HANGYE, json, callBack);

    }

    //添加问题
    public static void addQuestion(String question, String answer, String img_url, String scene, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("answer", answer);
            json.put("actionOption", "action_0");
            if (img_url != null) {
                json.put("img_url", img_url);
            }
            if (scene != null) {
                json.put("scene", scene);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ADD_QUESTION, json, callBack);
    }

    //添加问题
    public static void addQuestion(String question, String answer, String annexPath, int annexType, String scene, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("answer", answer);
            json.put("actionOption", "action_0");
            switch (annexType) {
                case AnnexUtil.PICTURE:
                    json.put("img_url", annexPath);
                    json.put("video_url", null);
                    json.put("audio_url", null);
                    json.put("file_url", null);
                    break;
                case AnnexUtil.VIDEO:
                    json.put("video_url", annexPath);
                    json.put("img_url", null);
                    json.put("audio_url", null);
                    json.put("file_url", null);
                    break;
                case AnnexUtil.AUDIO:
                    json.put("audio_url", annexPath);
                    json.put("video_url", null);
                    json.put("img_url", null);
                    json.put("file_url", null);
                    break;
                case AnnexUtil.FILE:
                    json.put("file_url", annexPath);
                    json.put("video_url", null);
                    json.put("audio_url", null);
                    json.put("img_url", null);
                    break;
                case -1:
                default:
                    json.put("file_url", null);
                    json.put("video_url", null);
                    json.put("audio_url", null);
                    json.put("img_url", null);
                    break;
            }
            if (scene != null) {
                json.put("scene", scene);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ADD_QUESTION, json, callBack);
    }

    //修改问题
    public static void editQuestion(String question, String answer, String annexPath, int annexType, String scene, int id, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("answer", answer);
            json.put("actionOption", "action_0");
            json.put("id", id);
            if (!Utils.isEmpty(annexPath)) {
                switch (annexType) {
                    case AnnexUtil.PICTURE:
                        json.put("img_url", annexPath);
                        json.put("video_url", null);
                        json.put("audio_url", null);
                        json.put("file_url", null);
                        break;
                    case AnnexUtil.VIDEO:
                        json.put("video_url", annexPath);
                        json.put("img_url", null);
                        json.put("audio_url", null);
                        json.put("file_url", null);
                        break;
                    case AnnexUtil.AUDIO:
                        json.put("audio_url", annexPath);
                        json.put("video_url", null);
                        json.put("img_url", null);
                        json.put("file_url", null);
                        break;
                    case AnnexUtil.FILE:
                        json.put("file_url", annexPath);
                        json.put("video_url", null);
                        json.put("audio_url", null);
                        json.put("img_url", null);
                        break;
                }
            }
            if (scene != null) {
                json.put("scene", scene);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_EDIT_QUESTION, json, callBack);
    }

    //修改问题
    public static void editQuestion(String question, String answer, String img_url, String scene, int id, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("answer", answer);
            json.put("actionOption", "action_0");
            json.put("id", id);
            if (img_url != null) {
                json.put("img_url", img_url);
            }
            if (scene != null) {
                json.put("scene", scene);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_EDIT_QUESTION, json, callBack);
    }

    //删除知识（单条）
    public static void deleteKnowledge(Integer knowledgeId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", knowledgeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_DELETE_KNOWLEDGE + knowledgeId, json, callBack);
    }

    //删除知识（批量）
    public static void deleteKnowledgeList(String ids, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("ids", ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_DELETE_KNOWLEDGE_LIST, json, callBack);
    }

    //删除站内消息
    public static void deleteRobotMail(int id, HttpCallBack callBack) {
        jsonRequest(Const.URL_DELETE_MAIL + id, new JSONObject(), callBack);
    }


    //无效问题
    public static void getInvalidQuestion(int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_INVALID_QUESTION + "?pageNo=" + pageNo, null, callBack);
    }

    //删除无效问题
    public static void deleteInvalidQuestion(String ids, HttpCallBack callBack) {
        jsonRequest(Const.URL_DELETE_INVALID_QUESTION + "?id_str=id=" + ids, null, callBack);
    }

    //编辑无效问题
    public static void editInvalidQuestion(String question, String answer, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_EDIT_INVALID_QUESTION, json, callBack);
    }


    //用户管理
    public static void getChatUsers(int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_GET_CHAT_USER + "?pageNo=" + pageNo, null, callBack);
    }

    //聊天记录
    public static void getChatLog(int pageNo, String uuid, HttpCallBack callBack) {
        jsonRequest(Const.URL_GET_CHAT_LOG + "?pageNo=" + pageNo + "&uuid=" + uuid, null, callBack);
    }

    //根据手机号获取机器人信息
    public static void getRobotByUserName(String username, HttpCallBack callback) {
        jsonRequest(Const.URL_GET_ROBOT_INFO_BY_USERNAME + username, null, callback);
    }

    //上传通讯录
    public static void uploadContacts(List<Contact> contactList, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < contactList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userName", contactList.get(i).getPhone());
                jsonObject.put("realName", contactList.get(i).getName());
                jsonArray.put(jsonObject);
            }
            json.put("accountList", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_UPLOAD_CONTACT, json, callBack);
    }

    //通话记录查询
    public static void getCallLog(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_GET_CALL_LOG, callBack);
    }


    //关注列表查询
    public static void searchConcern(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_SEARCH_CONCERN, callBack);
    }

    /**
     * 王浩兰
     * 关注列表  17.12.11日
     *
     * @param own_phone 登录用户uniqueid
     */
    public static void ConcernList(String own_phone, HttpJsonArrayCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("own_phone", own_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArrayRequest(Const.URL_FINDGUANZHU_NEW, json, callBack);
    }


    public static void updateRemark(String own_phone, String remarks, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("own_phone", own_phone);
            json.put("remarks", remarks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_UPDATE_REMARK, json, callBack);
    }

    public static void updateLocation(String lat, String lng, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("lat", lat);
            json.put("lng", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_UPDATE_LOCATION, json, callBack);
    }


    //粉丝列表
    public static void getFansList(int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_FANS_LIST + "?pageNo=" + pageNo, null, callBack);
    }

    //附近列表
    public static void searchNearBy(String lat, String lng, int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_SEARCH_NEARBY + "?lat=" + lat + "&lng=" + lng + "&pageNo=" + pageNo, null, callBack);
    }

    //取消关注
    public static void deleteConcern(String oth_phone, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("oth_phone", oth_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_DELETE_CONCERN, json, callBack);
    }

    //按问答量排行列表
    public static void rankByQuestion(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_RANK_BY_QUESTION, callBack);
    }

    //按价值排行列表
    public static void rankByValue(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_RANK_BY_VALUE, callBack);
    }

    //按粉丝排行列表
    public static void rankByFans(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_RANK_BY_FANS, callBack);
    }

    //添加关注
    public static void addConcern(String oth_phone, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("oth_phone", oth_phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ADD_CONCERN, json, callBack);
    }

    //站内信查询
    public static void searchMailSystem(int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_SEARCH_MAIL_SYSTEM + "?pageNo=" + pageNo, null, callBack);
    }

    //信息条数查询
    public static void searchMailCount(HttpCallBack callBack) {
        jsonRequest(Const.URL_SEARCH_MAIL_COUNT, null, callBack);
    }

    //私信查询
    public static void searchMailRobot(int pageNo, HttpCallBack callBack) {
        String robotName = null;
        try {
            robotName = URLEncoder.encode(MySharedPreferences.getRobotName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_SEARCH_MAIL_ROBOT + "?receive_name=" + robotName + "&pageNo=" + pageNo, null, callBack);
    }

    //系统消息标记已读
    public static void editMailSystem(Integer mailId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("ids", mailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_EDIT_MAIL_SYSTEM, json, callBack);
    }

    //私信消息标记已读
    public static void editMailRobot(Integer mailId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_EDIT_MAIL_ROBOT, json, callBack);
    }

    //知识库查询
    public static void searchKnowledge(int pageNo, HttpCallBack callBack) {
        jsonRequest(Const.URL_SEARCH_KNOWLEDGE + "?pageNo=" + pageNo, null, callBack);
    }

    //应用列表查询
    public static void searchExternalOptionsList(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_SEARCH_EXTERNALOPTIONSLIST, callBack);
    }

    //应用设置
    public static void setExternalOptionsList(List<Integer> openIds, List<Integer> closedIds, HttpCallBack callBack) {
        String jsonstr = "{\"openIds\":" + openIds + "," +
                "\"closedIds\":" + closedIds + "}";
        JSONObject json = null;
        try {
            json = new JSONObject(jsonstr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_SET_EXTERNALOPTIONSLIST, json, callBack);
    }


    //启用应用查询
    public static void searchEnableExternalOptions(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_SEARCH_ENABLE_EXTERNALOPTION, callBack);
    }

    //问答接口
    public static void getAnswer(String uuid, String question, boolean isVoice, String lat, String lng, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
            json.put("uuid", uuid);
            json.put("isVoice", isVoice ? 1 : 0);
            json.put("source", "app");
            if (lat != null && lng != null) {
                json.put("lat", lat);
                json.put("lng", lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_ANSWER, json, callBack);
    }

    //远程问答接口
    public static void getRemoteAnswer(String username, String uuid, String question, boolean isVoice, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("question", question);
            json.put("uuid", uuid);
            json.put("isVoice", isVoice ? 1 : 0);
            json.put("source", "app");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_REMOTE_ANSWER, json, callBack);
    }

    //采采
    public static void caiCai(String question, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("question", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Const.URL_CAICAI, json, callBack);
    }

    //上传图片
    public static void uploadImage(File image, HttpStringCallBack callBack) {
        uploadRequest(Const.URL_UPLOAD, image, callBack);
    }

    //上传附件
    public static void uploadAnnex(File image, HttpStringCallBack callBack) {
        uploadRequest(Const.URL_UPLOAD_ANNEX, image, callBack);
    }

    //上传crash日志
    public static void uploadCrashLog(File log, HttpStringCallBack callBack) {
        uploadRequest(Const.URL_UPLOAD_CRASH_LOG, log, callBack);
    }

    //微信绑定
    public static void wechatBind(String username, String password, String openId, String unionId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", username);
            json.put("password", password);
            json.put("openId", openId);
            json.put("unionId", unionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequestWithSession(Const.URL_WECHAT_BIND, json, callBack);
    }

    //微信登录
    public static void wechatLogin(String openId, String unionId, HttpCallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("openId", openId);
            json.put("unionId", unionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequestWithSession(Const.URL_WECHAT_LOGIN, json, callBack);
    }

    //获取机器人信息
    public static void getRobotInfo(HttpCallBack callBack) {
        jsonRequest(Const.URL_ROBOT_INFO, null, callBack);
    }

    //获取所有者信息
    public static void getCompanyInfo(HttpCallBack callBack) {
        jsonRequest(Const.URL_COMPANY_INFO, null, callBack);
    }

    //获取个人信息
    public static void getMyInfo(HttpCallBack callBack) {
        jsonRequest(Const.URL_GET_INFO, null, callBack);
    }

    //机器人手机号权限设置
    public static void setPhonePermission(int type, HttpCallBack callBack) {
        jsonRequest(Const.URL_PHONE_PERMISSION_SETTING + type, null, callBack);
    }

    //根据unique_id获取机器人信息Rseat
    public static void getRobotByUniqueId(String uniqueId, HttpCallBack callBack) {
        jsonRequest(Const.URL_GET_ROBOT_INFO + uniqueId, null, callBack);
    }

    //机器人查询
    public static void searchRobot(String keyword, HttpJsonArrayCallBack callBack) {
        String url = null;
        try {
            url = Const.URL_SEARCH_ROBOT + URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jsonArrayRequest(url, callBack);
    }

    //附近美食测试
    public static void searchFood(HttpCallBack callBack) {
        String url = null;
        try {
            url = "http://60.205.59.176:8260/intel_food/intel_food?question=" + URLEncoder.encode("附近美食", "utf-8") + "&lat=40.03512285301014&lng=116.50928697484426";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jsonRequest(url, null, callBack);
    }

    //余额查询
    public static void searchBalance(HttpCallBack callBack) {
        jsonRequest(Const.URL_SEARCH_BALANCE, null, callBack);
    }

    //取消机器人查询
    public static void cancelAll() {
        mQueue.cancelAll("tag1");
    }

    //加载网络图片
    public static void loadImageFromNetwork(String url, ImageCallBack callBack) {
        imageRequest(url + "/thumbnail/200x300", callBack);
    }

    //微信通过code获取access_token
    public static void getAccessToken(String url, HttpCallBack callBack) {
        jsonRequest(url, null, callBack);
    }

    /**
     * 从服务器获得更新信息
     */
    public static void getUpdateMsg(final HttpCallBack callBack) {
        MyJsonRequest jsonObjectRequest = new MyJsonRequest(VERSION_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        });
        jsonObjectRequest.setTag("tag1");
        mQueue.add(jsonObjectRequest);
    }

    private static void jsonRequestWithSession(String url, JSONObject json, final HttpCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url
                , json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        }) {
            //重写parseNetworkResponse方法，返回的数据中 Set-Cookie:***************;
            //其中**************为session id
            @Override
            protected Response<JSONObject> parseNetworkResponse(
                    NetworkResponse response) {
                Response<JSONObject> superResponse = super
                        .parseNetworkResponse(response);
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
//                Const.localCookie = rawCookies.substring(0, rawCookies.indexOf(";"));
                MySharedPreferences.saveCookie(rawCookies.substring(0, rawCookies.indexOf(";")));
                return superResponse;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        mQueue.add(jsonObjectRequest);
    }

    private static void jsonRequest(String url, JSONObject json, final HttpCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Cookie",Const.localCookie);
                headers.put("Cookie", MySharedPreferences.getCookie());
                return headers;
            }
        };
        jsonObjectRequest.setTag("tag1");
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 王浩兰
     *
     * @param url
     * @param json
     * @param callBack
     */
    private static void jsonArrayRequest(String url, JSONObject json, final HttpJsonArrayCallBack callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Cookie",Const.localCookie);
                headers.put("Cookie", MySharedPreferences.getCookie());
                return headers;
            }
        };
        jsonArrayRequest.setTag("tag1");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        mQueue.add(jsonArrayRequest);
    }

    private static void jsonArrayRequest(String url, final HttpJsonArrayCallBack callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Cookie",Const.localCookie);
                headers.put("Cookie", MySharedPreferences.getCookie());
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        jsonArrayRequest.setTag("tag1");
        mQueue.add(jsonArrayRequest);
    }

    private static void jsonArrayRequest(String url, final Map<String, String> params, final HttpJsonArrayCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    callBack.onSuccess(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Cookie",Const.localCookie);
                headers.put("Cookie", MySharedPreferences.getCookie());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }

    private static void imageRequest(String url, final ImageCallBack callBack) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        callBack.onSuccess(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFailed(error);
            }
        });
        imageRequest.setTag("tag1");
        mQueue.add(imageRequest);
    }

    private static void uploadRequest(String url, File image, final HttpStringCallBack callBack) {
        List<Part> partList = new ArrayList<Part>();
        try {
            partList.add(new FilePart("file", image));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartRequest profileUpdateRequest = new MultipartRequest(url, partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //处理成功返回信息
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                callBack.onFailed(error);
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        profileUpdateRequest.setRetryPolicy(retryPolicy);
        //将请求加入队列
        profileUpdateRequest.setTag("tag1");
        mQueue.add(profileUpdateRequest);
    }


    //自定义请求 用于版本更新
    private static class MyJsonRequest extends JsonObjectRequest {
        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                response.headers.put("HTTP.CONTENT_TYPE", "utf-8");
                String jsonString = new String(response.data, "utf-8");
                return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }

        /**
         * @param url
         * @param jsonRequest
         * @param listener
         * @param errorListener
         */
        public MyJsonRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
            super(url, jsonRequest, listener, errorListener);
        }

    }

    public static void loadImgByVolley(String string, ImageView ivImage) {
//        try {
//            string = new String(string.getBytes("gbk"),"utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        imageLoader = new ImageLoader(mQueue, LruImageCache.getInstance());
        ImageListener listener = ImageLoader.getImageListener(ivImage,
                R.mipmap.icon_loading, R.mipmap.icon_load_failed);
        imageLoader.get(string, listener);
    }

    public static void getCommonLanguage(HttpJsonArrayCallBack callBack) {
        jsonArrayRequest(Const.URL_SEARCH_COMMON_LANGUAGE, callBack);
    }

    public static void saveCommonLanguage(String question, HttpCallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ques", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonRequest(Const.URL_SAVE_COMMON_LANGUAGE, jsonObject, callBack);
    }

    public static void deleteCommonLanguage(String question, HttpCallBack callBack) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ques", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonRequest(Const.URL_DELETE_COMMON_LANGUAGE, jsonObject, callBack);
    }
}
