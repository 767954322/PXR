package com.diting.pingxingren.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 05, 13:32.
 * Description: .
 */

public class ChatBundle implements Parcelable {

    private String mFrom;
    private String mUserName;
    private String mHeadPortrait;
    private String mWelcome;
    private String mRobotName;
    private String mUniqueId;
    private String answer1;
    private String answer2;
    private String answer3;

    protected ChatBundle(Parcel in) {
        mFrom = in.readString();
        mUserName = in.readString();
        mHeadPortrait = in.readString();
        mWelcome = in.readString();
        mRobotName = in.readString();
        mUniqueId = in.readString();
        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
    }

    public static final Creator<ChatBundle> CREATOR = new Creator<ChatBundle>() {
        @Override
        public ChatBundle createFromParcel(Parcel in) {
            return new ChatBundle(in);
        }

        @Override
        public ChatBundle[] newArray(int size) {
            return new ChatBundle[size];
        }
    };

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

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getHeadPortrait() {
        return mHeadPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        mHeadPortrait = headPortrait;
    }

    public String getWelcome() {
        return mWelcome;
    }

    public void setWelcome(String welcome) {
        mWelcome = welcome;
    }

    public String getRobotName() {
        return mRobotName;
    }

    public void setRobotName(String robotName) {
        mRobotName = robotName;
    }

    public String getUniqueId() {
        return mUniqueId;
    }

    public void setUniqueId(String uniqueId) {
        mUniqueId = uniqueId;
    }



    public ChatBundle() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFrom);
        dest.writeString(mUserName);
        dest.writeString(mHeadPortrait);
        dest.writeString(mWelcome);
        dest.writeString(mRobotName);
        dest.writeString(mUniqueId);
        dest.writeString(answer1);
        dest.writeString(answer2);
        dest.writeString(answer3);
    }
}
