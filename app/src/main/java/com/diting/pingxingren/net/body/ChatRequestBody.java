package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 11:54.
 * Description: 对话请求体.
 */

public class ChatRequestBody implements Parcelable {

    private String uuid;
    private String question;
    private String username;
    private String source;
    private String lat;
    private String lng;
    private int isVoice;

    private String answer1;
    private String answer2;
    private String answer3;
    private String unique_id;
    private String robotHeadImg;


    protected ChatRequestBody(Parcel in) {
        uuid = in.readString();
        question = in.readString();
        username = in.readString();
        source = in.readString();
        lat = in.readString();
        lng = in.readString();
        isVoice = in.readInt();
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        unique_id = in.readString();
        robotHeadImg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(question);
        dest.writeString(username);
        dest.writeString(source);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeInt(isVoice);
        dest.writeString(answer1);
        dest.writeString(answer2);
        dest.writeString(answer3);
        dest.writeString(unique_id);
        dest.writeString(robotHeadImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatRequestBody> CREATOR = new Creator<ChatRequestBody>() {
        @Override
        public ChatRequestBody createFromParcel(Parcel in) {
            return new ChatRequestBody(in);
        }

        @Override
        public ChatRequestBody[] newArray(int size) {
            return new ChatRequestBody[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getIsVoice() {
        return isVoice;
    }

    public void setIsVoice(int isVoice) {
        this.isVoice = isVoice;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getRobotHeadImg() {
        return robotHeadImg;
    }

    public void setRobotHeadImg(String robotHeadImg) {
        this.robotHeadImg = robotHeadImg;
    }

    public ChatRequestBody() {
    }


}
