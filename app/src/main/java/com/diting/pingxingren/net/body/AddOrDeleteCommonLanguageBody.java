package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 添加或删除常用语   与pc端同步
 * Created by Administrator on 2018/1/9.
 */

public class AddOrDeleteCommonLanguageBody implements Parcelable{
    private int id;
    private boolean highFrequencyQuestion;
    private  boolean ownVisible;


    public AddOrDeleteCommonLanguageBody() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHighFrequencyQuestion() {
        return highFrequencyQuestion;
    }

    public void setHighFrequencyQuestion(boolean highFrequencyQuestion) {
        this.highFrequencyQuestion = highFrequencyQuestion;
    }

    public boolean isOwnVisible() {
        return ownVisible;
    }

    public void setOwnVisible(boolean ownVisible) {
        this.ownVisible = ownVisible;
    }

    protected AddOrDeleteCommonLanguageBody(Parcel in) {
        id = in.readInt();
        highFrequencyQuestion = in.readByte() != 0;
        ownVisible = in.readByte() != 0;
    }

    public static final Creator<AddOrDeleteCommonLanguageBody> CREATOR = new Creator<AddOrDeleteCommonLanguageBody>() {
        @Override
        public AddOrDeleteCommonLanguageBody createFromParcel(Parcel in) {
            return new AddOrDeleteCommonLanguageBody(in);
        }

        @Override
        public AddOrDeleteCommonLanguageBody[] newArray(int size) {
            return new AddOrDeleteCommonLanguageBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeByte((byte) (highFrequencyQuestion ? 1 : 0));
        parcel.writeByte((byte) (ownVisible ? 1 : 0));
    }
}
