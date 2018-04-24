package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 15, 17:21.
 * Description: .
 */

public class CommonLanguageBody implements Parcelable {

    private String user_id;
    private String ques;
    private String shiro;
    private String ques_id ;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getShiro() {
        return shiro;
    }

    public void setShiro(String shiro) {
        this.shiro = shiro;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.ques);
        dest.writeString(this.shiro);
        dest.writeString(this.ques_id );
    }

    public CommonLanguageBody() {
    }

    protected CommonLanguageBody(Parcel in) {
        this.user_id = in.readString();
        this.ques = in.readString();
        this.shiro = in.readString();
        this.ques_id  = in.readString();
    }

    public static final Parcelable.Creator<CommonLanguageBody> CREATOR = new Parcelable.Creator<CommonLanguageBody>() {
        @Override
        public CommonLanguageBody createFromParcel(Parcel source) {
            return new CommonLanguageBody(source);
        }

        @Override
        public CommonLanguageBody[] newArray(int size) {
            return new CommonLanguageBody[size];
        }
    };
}
