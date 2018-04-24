package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会问数量
 * Created by Administrator on 2018/1/15.
 */

public class AskCountModel implements Parcelable{


    /**
     * count : 7
     * ask_id : null
     * id : null
     * page : null
     * robot_unicode : null
     */

    private int count;
    private String ask_id;
    private String id;
    private String page;
    private String robot_unicode;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    protected AskCountModel(Parcel in) {
        count = in.readInt();
        ask_id = in.readString();
        id = in.readString();
        page = in.readString();
        robot_unicode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeString(ask_id);
        dest.writeString(id);
        dest.writeString(page);
        dest.writeString(robot_unicode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AskCountModel> CREATOR = new Creator<AskCountModel>() {
        @Override
        public AskCountModel createFromParcel(Parcel in) {
            return new AskCountModel(in);
        }

        @Override
        public AskCountModel[] newArray(int size) {
            return new AskCountModel[size];
        }
    };
}
