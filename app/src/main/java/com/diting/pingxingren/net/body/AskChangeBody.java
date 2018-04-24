package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会问的问题  回答后改变
 * Created by Administrator on 2018/1/10.
 */

public class AskChangeBody implements Parcelable{
    private String ask_id;
    private String robot_unicode;

    public AskChangeBody() {
    }

    protected AskChangeBody(Parcel in) {
        ask_id = in.readString();
        robot_unicode = in.readString();
    }

    public static final Creator<AskChangeBody> CREATOR = new Creator<AskChangeBody>() {
        @Override
        public AskChangeBody createFromParcel(Parcel in) {
            return new AskChangeBody(in);
        }

        @Override
        public AskChangeBody[] newArray(int size) {
            return new AskChangeBody[size];
        }
    };

    public String getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(String ask_id) {
        this.ask_id = ask_id;
    }

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
        dest.writeString(ask_id);
        dest.writeString(robot_unicode);
    }
}
