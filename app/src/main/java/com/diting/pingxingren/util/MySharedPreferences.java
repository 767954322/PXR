package com.diting.pingxingren.util;

import android.content.SharedPreferences;

import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.smarteditor.model.EditListModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by asus on 2017/1/3.
 */

public class MySharedPreferences {
    private static SharedPreferences userPreferences = MyApplication.userPreferences;

    public static void saveLogin(boolean login) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("login", login);
        editor.apply();
    }

    public static boolean getLogin() {
        return userPreferences.getBoolean("login", false);
    }

    //此处保存的用户名与密码应该加密
    public static void saveUser(String username, String password) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public static void saveSafeUserInfo(String username, String password) {
        SharedPreferences.Editor editor = userPreferences.edit();
        String name;
        String pass;
        boolean isSafe;
        try {
            name = Base64Util.encodeString(username);
            pass = Base64Util.encodeString(password);
            isSafe = true;
        } catch (UnsupportedEncodingException e) {
            name = username;
            pass = password;
            isSafe = false;
        }

        editor.putString("username", name);
        editor.putString("password", pass);
        editor.putBoolean("userInfoSafe", isSafe);
        editor.apply();
    }

    public static String getUsername() {
        return userPreferences.getBoolean("userInfoSafe", false) ?
                Base64Util.decodeString(userPreferences.getString("username", "")) :
                userPreferences.getString("username", "");
    }

    public static void isFirstOpen(boolean boo) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("isFirstOpen", boo);
        editor.apply();
    }

    //是否第一次打开应用
    public static Boolean isFirstOpen() {
        return userPreferences.getBoolean("isFirstOpen", true);
    }

    public static String getPassword() {
        return userPreferences.getBoolean("userInfoSafe", false) ?
                Base64Util.decodeString(userPreferences.getString("password", "")) :
                userPreferences.getString("password", "");
    }

    public static void saveCookie(String cookie) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("cookie", cookie);
        editor.apply();
    }

    public static String getCookie() {
        return userPreferences.getString("cookie", "");
    }

    public static void clearCookie() {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.remove("cookie");
        editor.remove("headPortrait");
        editor.remove("robotName");
        editor.remove("defaultRobotName");
        editor.remove("companyName");
        editor.remove("industry");
        editor.remove("robotHeadPortrait");
        editor.remove("hangyedengji");
        editor.remove("invalidAnswer1");
        editor.remove("invalidAnswer2");
        editor.remove("invalidAnswer3");
        editor.remove("robotVal");
        editor.apply();
    }

    public static void saveHeadPortrait(String headPortrait) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("headPortrait", headPortrait);
        editor.apply();
    }

    public static String getHeadPortrait() {
        return userPreferences.getString("headPortrait", "");
    }

    public static void saveRobotHeadPortrait(String headPortrait) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("robotHeadPortrait", headPortrait);
        editor.apply();
    }

    public static String getRobotHeadPortrait() {
        return userPreferences.getString("robotHeadPortrait", "");
    }

    public static void saveSuggestionState(boolean state) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("showSuggestion", state);
        editor.apply();
    }

    public static boolean getSuggestionState() {
        return userPreferences.getBoolean("showSuggestion", true);
    }

    public static String getUniqueId() {
        return userPreferences.getString("uniqueId", "");
    }

    public static void saveUniqueId(String uniqueId) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("uniqueId", uniqueId);
        editor.apply();
    }

    public static String getProfile() {
        return userPreferences.getString("profile", "");
    }

    public static void saveProfile(String profile) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("profile", profile);
        editor.apply();
    }

    public static int getPhoneSwitch() {
        return userPreferences.getInt("phoneSwitch", 1);
    }

    public static void savePhoneSwitch(int phoneSwitch) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putInt("phoneSwitch", phoneSwitch);
        editor.apply();
    }

    public static boolean getContactsPermission() {
        return userPreferences.getBoolean("contacts", false);
    }

    public static void saveContactsPermission(boolean isPermission) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("contacts", isPermission);
        editor.apply();
    }

    public static String getRobotName() {
        return userPreferences.getString("robotName", "");
    }

    public static void saveRobotName(String robotName) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("robotName", robotName);
        editor.apply();
    }

    public static String getDefaultRobotName() {
        return userPreferences.getString("defaultRobotName", "");
    }

    public static void saveDefaultRobotName(String robotName) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("defaultRobotName", robotName);
        editor.apply();
    }

    public static boolean isDefaultRobot() {
        return userPreferences.getBoolean("defaultRobot", true);
    }

    public static void saveDefaultRobot(String robotName) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("defaultRobot", robotName.equals(getDefaultRobotName()));
        editor.apply();
    }

    public static void saveLoginTime(String loginTime) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("loginTime", loginTime);
        editor.apply();
    }

    public static String getLoginTime() {
        return userPreferences.getString("loginTime", "");
    }

    public static void saveVoicePeople(String peopleCode) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("peopleCode", peopleCode);
        editor.apply();
    }

    public static String getVoicePeople() {
        return userPreferences.getString("peopleCode", "");
    }

    public static void saveLeftHeadImage(String LeftHeadImage) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("LeftHeadImage", LeftHeadImage);
        editor.apply();
    }

    public static String getLeftHeadImage() {
        return userPreferences.getString("LeftHeadImage", "");
    }

    public static void saveLat(String lat) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("lat", lat);
        editor.apply();
    }

    public static String getLat() {
        return userPreferences.getString("lat", "");
    }

    public static void saveLng(String lng) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("lng", lng);
        editor.apply();
    }

    public static String getLng() {
        return userPreferences.getString("lng", "");
    }

    public static void saveSkinRes(int skinRes) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putInt("skin", skinRes);
        editor.apply();
    }

    public static int getSkinRes() {
        return userPreferences.getInt("skin", -1);
    }

    public static void saveFontSize(float fontSize) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putFloat("fontSize", fontSize);
        editor.apply();
    }

    public static float getFontSize() {
        return userPreferences.getFloat("fontSize", 20);
    }

    public static void saveCity(String city) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("city", city);
        editor.apply();
    }

    public static String getCity() {
        return userPreferences.getString("city", "北京");
    }

    public static void saveAddr(String addr) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("addr", addr);
        editor.apply();
    }

    public static String getAddr() {
        return userPreferences.getString("addr", null);
    }


    public static void saveIndustry(String Industry) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("industry", Industry);
        editor.apply();
    }

    public static String getIndustry() {
        return userPreferences.getString("industry", null);
    }

    public static String getCompanyName() {
        return userPreferences.getString("companyName", null);
    }

    public static void saveCompanyName(String companyName) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("companyName", companyName);
        editor.apply();
    }

    public static int getFansCount() {
        return userPreferences.getInt("fansCount", 0);
    }

    public static void saveFansCount(int fansCount) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putInt("fansCount", fansCount);
        editor.apply();
    }


    public static boolean getEnable() {
        return userPreferences.getBoolean("enable", false);
    }

    public static void saveEnable(boolean enable) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("enable", enable);
        editor.apply();
    }


    public static int getUserSex() {
        return userPreferences.getInt("sex", 1);

    }

    public static void saveSex(int sex) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putInt("sex", sex);
        editor.apply();
    }

    public static String getShengri() {
        return userPreferences.getString("shengri", null);
    }

    public static void setShengri(String shengri) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("shengri", shengri);
        editor.apply();
    }


    public static void saveHangYeLevel(String hangyedengji) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("hangyedengji", hangyedengji);
        editor.apply();
    }

    public static String getHangYeLevel() {
        return userPreferences.getString("hangyedengji", null);
    }

    public static String getPrice() {
        return userPreferences.getString("price", null);


    }

    public static void savePrice(String price) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("price", price);
        editor.apply();
    }


    public static String getUniversalAnswer1() {
        return userPreferences.getString("invalidAnswer1", null);
    }

    public static void saveUniversalAnswer1(String invalidAnswer1) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("invalidAnswer1", invalidAnswer1);
        editor.apply();
    }

    public static String getUniversalAnswer2() {
        return userPreferences.getString("invalidAnswer2", null);
    }

    public static void saveUniversalAnswer2(String invalidAnswer2) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("invalidAnswer2", invalidAnswer2);
        editor.apply();
    }

    public static String getUniversalAnswer3() {
        return userPreferences.getString("invalidAnswer3", null);
    }

    public static void saveUniversalAnswer3(String invalidAnswer3) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("invalidAnswer3", invalidAnswer3);
        editor.apply();
    }

    public static void saveRobotVal(String roborVal) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("robotVal", roborVal);
        editor.apply();
    }

    public static String getRobotVal() {
        String value = userPreferences.getString("robotVal", null);
        if (!Utils.isEmpty(value)) {
            if (value.contains("."))
                value = value.substring(0, value.indexOf("."));
            return value;
        }
        return "" + 50;

    }

    public static void saveThirdState(boolean thirdState){
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("thirdState", thirdState);
        editor.apply();
    }


    public static boolean getThirdState() {
        return userPreferences.getBoolean("thirdState", false);
    }


//    String value = String.valueOf(robotValue);
//        if (value.contains("."))
//    value = value.substring(0, value.indexOf("."));
//        holder.setText(R.id.tvValue, value);
//


    //保存屏幕显示模式(1:日间模式;2:夜间模式)
    public static void saveNewsShowMode(int showMode) {
        SharedPreferences.Editor editor = userPreferences.edit();
        switch (showMode) {
            case Constants.MODE_DAY:
                editor.putInt("showMode", Constants.MODE_NIGHT);
                break;
            case Constants.MODE_NIGHT:
                editor.putInt("showMode", Constants.MODE_DAY);
                break;
        }
        editor.apply();
    }

    //获取屏幕显示模式
    public static int getNewsShowMode() {
        return userPreferences.getInt("showMode", Constants.MODE_DAY);
    }

    //保存每日新闻页面字体大小,默认100为正常
    public static void saveNewsFont(int size) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putInt("newsFontSize", size);
        editor.apply();
    }

    //获取每日新闻页面字体大小
    public static int getNewsFont() {
        return userPreferences.getInt("newsFontSize", 1);
    }

    //将语音记事本未完成的集合转换成json后保存
    public static void saveNoteEditContent(List<EditListModel> models) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < models.size(); i++) {
            EditListModel model = models.get(i);
            buffer.append("{\"type\":" + model.getType() + ",\"path\":\"" + model.getObj() + "\",\"addTime\":\"" + model.getAddTime() + "\"},");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("NoteEditContent", buffer.toString());
        editor.apply();
    }

    public static String getNoteEditContent() {
        return userPreferences.getString("NoteEditContent", "");
    }

    //清除语音记事本的本地缓存
    public static void clearNotEditContent() {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.remove("NoteEditContent");
        editor.commit();
    }

    public static void setVip(boolean vip) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("vip", vip);
        editor.apply();
    }

    public static boolean getVip() {
        return userPreferences.getBoolean("vip", false);
    }

    public static void setEditorSort(String sort) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("sort", sort);
        editor.apply();
    }

    public static String getEditorSort() {
        return userPreferences.getString("sort", "time");
    }

    public static void setQiNiuToken(String qiNiuToken) {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putString("qiNiuToken", qiNiuToken);
        editor.apply();
    }

    public static String getQiNiuToken() {
        return userPreferences.getString("qiNiuToken", "");
    }
}
