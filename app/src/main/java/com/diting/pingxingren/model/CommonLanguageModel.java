package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 09, 16:46.
 * Description: .
 */

public class CommonLanguageModel implements Parcelable {

    private String ques;
    private String user_id;
    private int id;
    private String shiro;
    private String ques_id;

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeString(this.ques);
        dest.writeString(this.user_id);
        dest.writeInt(this.id);
        dest.writeString(this.shiro);
        dest.writeString(this.ques_id);
    }

    public CommonLanguageModel() {
    }

    protected CommonLanguageModel(Parcel in) {
        this.ques = in.readString();
        this.user_id = in.readString();
        this.id = in.readInt();
        this.shiro = in.readString();
        this.ques_id = in.readString();
    }

    public static final Parcelable.Creator<CommonLanguageModel> CREATOR = new Parcelable.Creator<CommonLanguageModel>() {
        @Override
        public CommonLanguageModel createFromParcel(Parcel source) {
            return new CommonLanguageModel(source);
        }

        @Override
        public CommonLanguageModel[] newArray(int size) {
            return new CommonLanguageModel[size];
        }
    };
}
