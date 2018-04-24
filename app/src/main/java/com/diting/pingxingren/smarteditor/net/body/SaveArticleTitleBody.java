package com.diting.pingxingren.smarteditor.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 29, 15:02.
 * Description: .
 */

public class SaveArticleTitleBody implements Parcelable {

    private String title;
    private String iec_id;
    private int deletetype;
    private String createdtime;
    private String updatedtime;
    private String robot_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIec_id() {
        return iec_id;
    }

    public void setIec_id(String iec_id) {
        this.iec_id = iec_id;
    }

    public int getDeletetype() {
        return deletetype;
    }

    public void setDeletetype(int deletetype) {
        this.deletetype = deletetype;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.iec_id);
        dest.writeInt(this.deletetype);
        dest.writeString(this.createdtime);
        dest.writeString(this.updatedtime);
        dest.writeString(this.robot_id);
    }

    public SaveArticleTitleBody() {
    }

    protected SaveArticleTitleBody(Parcel in) {
        this.title = in.readString();
        this.iec_id = in.readString();
        this.deletetype = in.readInt();
        this.createdtime = in.readString();
        this.updatedtime = in.readString();
        this.robot_id = in.readString();
    }

    public static final Parcelable.Creator<SaveArticleTitleBody> CREATOR = new Parcelable.Creator<SaveArticleTitleBody>() {
        @Override
        public SaveArticleTitleBody createFromParcel(Parcel source) {
            return new SaveArticleTitleBody(source);
        }

        @Override
        public SaveArticleTitleBody[] newArray(int size) {
            return new SaveArticleTitleBody[size];
        }
    };
}
