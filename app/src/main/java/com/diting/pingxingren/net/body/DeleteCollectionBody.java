package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/1/9.
 */

public class DeleteCollectionBody implements Parcelable {
    private String id;

    public DeleteCollectionBody() {
    }

    protected DeleteCollectionBody(Parcel in) {
        id = in.readString();
    }

    public static final Creator<DeleteCollectionBody> CREATOR = new Creator<DeleteCollectionBody>() {
        @Override
        public DeleteCollectionBody createFromParcel(Parcel in) {
            return new DeleteCollectionBody(in);
        }

        @Override
        public DeleteCollectionBody[] newArray(int size) {
            return new DeleteCollectionBody[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }
}
