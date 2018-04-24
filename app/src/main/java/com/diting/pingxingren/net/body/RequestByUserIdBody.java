package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 15, 16:41.
 * Description: .
 */

public class RequestByUserIdBody implements Parcelable {

    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
    }

    public RequestByUserIdBody() {
    }

    protected RequestByUserIdBody(Parcel in) {
        this.user_id = in.readString();
    }

    public static final Parcelable.Creator<RequestByUserIdBody> CREATOR = new Parcelable.Creator<RequestByUserIdBody>() {
        @Override
        public RequestByUserIdBody createFromParcel(Parcel source) {
            return new RequestByUserIdBody(source);
        }

        @Override
        public RequestByUserIdBody[] newArray(int size) {
            return new RequestByUserIdBody[size];
        }
    };
}
