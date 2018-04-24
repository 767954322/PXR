package com.diting.pingxingren.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.diting.pingxingren.entity.base.BasePageInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/9/21.
 */

public class RobotPageInfo extends BasePageInfo implements Parcelable{
    @SerializedName("items")
    private ArrayList<RobotConcern> robotList;

    protected RobotPageInfo(Parcel in) {
        super(in);
    }

    public ArrayList<RobotConcern> getRobotList() {
        return robotList;
    }

    public void setRobotList(ArrayList<RobotConcern> robotList) {
        this.robotList = robotList;
    }
}
