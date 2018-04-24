package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2018 on 2018/1/31.
 */

public class OnlyNameBody implements Parcelable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected OnlyNameBody(Parcel in) {
    }

    public OnlyNameBody() {
    }

    public static final Creator<OnlyNameBody> CREATOR = new Creator<OnlyNameBody>() {
        @Override
        public OnlyNameBody createFromParcel(Parcel in) {
            return new OnlyNameBody(in);
        }

        @Override
        public OnlyNameBody[] newArray(int size) {
            return new OnlyNameBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
