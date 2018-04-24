package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 获取会问的列表
 * Created by Administrator on 2018/1/10.
 */

public class AskListBody implements Parcelable{
    private String robot_unicode;//机器人unicode

    public AskListBody() {
    }

    protected AskListBody(Parcel in) {
        robot_unicode = in.readString();
    }

    public static final Creator<AskListBody> CREATOR = new Creator<AskListBody>() {
        @Override
        public AskListBody createFromParcel(Parcel in) {
            return new AskListBody(in);
        }

        @Override
        public AskListBody[] newArray(int size) {
            return new AskListBody[size];
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(robot_unicode);
    }
}
