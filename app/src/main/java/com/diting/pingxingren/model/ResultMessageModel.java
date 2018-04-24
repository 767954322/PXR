package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 12, 10:14.
 * Description: .
 */

public class ResultMessageModel implements Parcelable {

    public String message;
    public String result;
    public String url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.result);
        dest.writeString(this.url);
    }

    public ResultMessageModel() {
    }

    protected ResultMessageModel(Parcel in) {
        this.message = in.readString();
        this.result = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ResultMessageModel> CREATOR = new Parcelable.Creator<ResultMessageModel>() {
        @Override
        public ResultMessageModel createFromParcel(Parcel source) {
            return new ResultMessageModel(source);
        }

        @Override
        public ResultMessageModel[] newArray(int size) {
            return new ResultMessageModel[size];
        }
    };
}
