package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/8/16, 15:31.
 * Description: 反馈建议请求体.
 */

public class FeedBackBody implements Parcelable {

    private String suggestion;
    private String contactInformation;

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suggestion);
        dest.writeString(this.contactInformation);
    }

    public FeedBackBody() {
    }

    protected FeedBackBody(Parcel in) {
        this.suggestion = in.readString();
        this.contactInformation = in.readString();
    }

    public static final Creator<FeedBackBody> CREATOR = new Creator<FeedBackBody>() {
        @Override
        public FeedBackBody createFromParcel(Parcel source) {
            return new FeedBackBody(source);
        }

        @Override
        public FeedBackBody[] newArray(int size) {
            return new FeedBackBody[size];
        }
    };
}
