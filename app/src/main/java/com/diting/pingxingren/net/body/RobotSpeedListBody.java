package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 速配上传数据
 * Created by Administrator on 2017/12/20.
 */

public class RobotSpeedListBody implements Parcelable{

    private String hangye;
    private String  uniqueId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye;
    }

    public RobotSpeedListBody() {
    }

    protected RobotSpeedListBody(Parcel in) {
        hangye = in.readString();
        uniqueId = in.readString();
    }

    public static final Creator<RobotSpeedListBody> CREATOR = new Creator<RobotSpeedListBody>() {
        @Override
        public RobotSpeedListBody createFromParcel(Parcel in) {
            return new RobotSpeedListBody(in);
        }

        @Override
        public RobotSpeedListBody[] newArray(int size) {
            return new RobotSpeedListBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hangye);
        dest.writeString(uniqueId);
    }
}
