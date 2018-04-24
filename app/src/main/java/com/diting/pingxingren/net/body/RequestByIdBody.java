package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 15, 16:41.
 * Description: .
 */

public class RequestByIdBody implements Parcelable {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
    }

    public RequestByIdBody() {
    }

    protected RequestByIdBody(Parcel in) {
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<RequestByIdBody> CREATOR = new Parcelable.Creator<RequestByIdBody>() {
        @Override
        public RequestByIdBody createFromParcel(Parcel source) {
            return new RequestByIdBody(source);
        }

        @Override
        public RequestByIdBody[] newArray(int size) {
            return new RequestByIdBody[size];
        }
    };
}
