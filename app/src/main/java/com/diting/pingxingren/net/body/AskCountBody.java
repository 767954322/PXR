package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 获取会问的数量
 * Created by Administrator on 2018/1/15.
 */

public class AskCountBody implements Parcelable{
    private String robot_unicode;

    public AskCountBody() {
    }

    protected AskCountBody(Parcel in) {
        robot_unicode = in.readString();
    }

    public static final Creator<AskCountBody> CREATOR = new Creator<AskCountBody>() {
        @Override
        public AskCountBody createFromParcel(Parcel in) {
            return new AskCountBody(in);
        }

        @Override
        public AskCountBody[] newArray(int size) {
            return new AskCountBody[size];
        }
    };

    public String getRobot_unicode() {
        return robot_unicode;
    }

    public void setRobot_unicode(String robot_unicode) {
        this.robot_unicode = robot_unicode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(robot_unicode);
    }
}
