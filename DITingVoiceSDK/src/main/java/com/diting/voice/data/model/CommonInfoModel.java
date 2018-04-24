package com.diting.voice.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/8/18.
 */

public class CommonInfoModel implements Parcelable{
    private String message;

    protected CommonInfoModel(Parcel in) {
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommonInfoModel> CREATOR = new Creator<CommonInfoModel>() {
        @Override
        public CommonInfoModel createFromParcel(Parcel in) {
            return new CommonInfoModel(in);
        }

        @Override
        public CommonInfoModel[] newArray(int size) {
            return new CommonInfoModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
