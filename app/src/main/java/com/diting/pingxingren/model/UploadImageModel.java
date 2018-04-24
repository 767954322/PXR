package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 11:57.
 * Description: .
 */

public class UploadImageModel implements Parcelable {

    private String error;
    private String url;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.error);
        dest.writeString(this.url);
    }

    public UploadImageModel() {
    }

    protected UploadImageModel(Parcel in) {
        this.error = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<UploadImageModel> CREATOR = new Parcelable.Creator<UploadImageModel>() {
        @Override
        public UploadImageModel createFromParcel(Parcel source) {
            return new UploadImageModel(source);
        }

        @Override
        public UploadImageModel[] newArray(int size) {
            return new UploadImageModel[size];
        }
    };
}
