package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2018 on 2018/1/31.
 * 机器人姓名是否存在模型
 */

public class RobotNameIsExistsModel implements Parcelable{

    /**
     * message : success
     * code : 2
     * details : null
     */

    private String message;
    private String code;
    private Object details;

    @Override
    public String toString() {
        return "RobotNameIsExistsModel{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", details=" + details +
                '}';
    }

    public RobotNameIsExistsModel(Parcel in) {
    }

    public RobotNameIsExistsModel() {
    }

    public static final Creator<RobotNameIsExistsModel> CREATOR = new Creator<RobotNameIsExistsModel>() {
        @Override
        public RobotNameIsExistsModel createFromParcel(Parcel in) {
            return new RobotNameIsExistsModel(in);
        }

        @Override
        public RobotNameIsExistsModel[] newArray(int size) {
            return new RobotNameIsExistsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
