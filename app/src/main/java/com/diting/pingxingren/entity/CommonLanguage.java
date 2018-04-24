package com.diting.pingxingren.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 05, 14:18.
 * Description: .
 */

public class CommonLanguage implements Parcelable {

    private String mQuestion;
    private String mUserID;
    private int mID;

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mQuestion);
        dest.writeString(this.mUserID);
        dest.writeInt(this.mID);
    }

    public CommonLanguage() {
    }

    protected CommonLanguage(Parcel in) {
        this.mQuestion = in.readString();
        this.mUserID = in.readString();
        this.mID = in.readInt();
    }

    public static final Parcelable.Creator<CommonLanguage> CREATOR = new Parcelable.Creator<CommonLanguage>() {
        @Override
        public CommonLanguage createFromParcel(Parcel source) {
            return new CommonLanguage(source);
        }

        @Override
        public CommonLanguage[] newArray(int size) {
            return new CommonLanguage[size];
        }
    };
}
