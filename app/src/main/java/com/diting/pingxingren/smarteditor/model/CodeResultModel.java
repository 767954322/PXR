package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 10:58.
 * Description: .
 */

public class CodeResultModel implements Parcelable {

    private String code;
    private String message;
    private int requestType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.message);
        dest.writeInt(this.requestType);
    }

    public CodeResultModel() {
    }

    protected CodeResultModel(Parcel in) {
        this.code = in.readString();
        this.message = in.readString();
        this.requestType = in.readInt();
    }

    public static final Parcelable.Creator<CodeResultModel> CREATOR = new Parcelable.Creator<CodeResultModel>() {
        @Override
        public CodeResultModel createFromParcel(Parcel source) {
            return new CodeResultModel(source);
        }

        @Override
        public CodeResultModel[] newArray(int size) {
            return new CodeResultModel[size];
        }
    };
}
