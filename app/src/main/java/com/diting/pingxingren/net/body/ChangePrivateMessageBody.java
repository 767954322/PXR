package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gu FanFan.
 * Date: 2017/6/8, 11:58.
 */

public class ChangePrivateMessageBody implements Parcelable {

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

    public ChangePrivateMessageBody() {
    }

    protected ChangePrivateMessageBody(Parcel in) {
        this.id = in.readInt();
    }

    public static final Creator<ChangePrivateMessageBody> CREATOR = new Creator<ChangePrivateMessageBody>() {
        @Override
        public ChangePrivateMessageBody createFromParcel(Parcel source) {
            return new ChangePrivateMessageBody(source);
        }

        @Override
        public ChangePrivateMessageBody[] newArray(int size) {
            return new ChangePrivateMessageBody[size];
        }
    };
}
