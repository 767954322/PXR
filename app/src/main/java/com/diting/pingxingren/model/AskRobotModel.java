package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会问列表里的
 * Created by Administrator on 2018/1/10.
 */

public class AskRobotModel implements Parcelable{
    private String  ask_id;
    private String id;
    private String page;
    private String robot_unicode;

    @Override
    public String toString() {
        return "AskRobotModel{" +
                "ask_id='" + ask_id + '\'' +
                ", id='" + id + '\'' +
                ", page='" + page + '\'' +
                ", robot_unicode='" + robot_unicode + '\'' +
                '}';
    }

    protected AskRobotModel(Parcel in) {
        ask_id = in.readString();
        id = in.readString();
        page = in.readString();
        robot_unicode = in.readString();
    }

    public static final Creator<AskRobotModel> CREATOR = new Creator<AskRobotModel>() {
        @Override
        public AskRobotModel createFromParcel(Parcel in) {
            return new AskRobotModel(in);
        }

        @Override
        public AskRobotModel[] newArray(int size) {
            return new AskRobotModel[size];
        }
    };

    public String getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(String ask_id) {
        this.ask_id = ask_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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
        dest.writeString(id);
        dest.writeString(page);
        dest.writeString(robot_unicode);
    }
}
