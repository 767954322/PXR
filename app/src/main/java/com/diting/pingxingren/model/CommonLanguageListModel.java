package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 常用语；列表
 * Created by Administrator on 2018/1/9.
 */

public class CommonLanguageListModel implements Parcelable{
    private String question ;
    private int id;

    public CommonLanguageListModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected CommonLanguageListModel(Parcel in) {
        question = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommonLanguageListModel> CREATOR = new Creator<CommonLanguageListModel>() {
        @Override
        public CommonLanguageListModel createFromParcel(Parcel in) {
            return new CommonLanguageListModel(in);
        }

        @Override
        public CommonLanguageListModel[] newArray(int size) {
            return new CommonLanguageListModel[size];
        }
    };
}
