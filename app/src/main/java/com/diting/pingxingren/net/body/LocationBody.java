package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/11, 14:16.
 * 登录请求体.
 */

public class LocationBody implements Parcelable {

    private String lat;
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    public LocationBody() {
    }

    protected LocationBody(Parcel in) {
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Parcelable.Creator<LocationBody> CREATOR = new Parcelable.Creator<LocationBody>() {
        @Override
        public LocationBody createFromParcel(Parcel source) {
            return new LocationBody(source);
        }

        @Override
        public LocationBody[] newArray(int size) {
            return new LocationBody[size];
        }
    };
}
