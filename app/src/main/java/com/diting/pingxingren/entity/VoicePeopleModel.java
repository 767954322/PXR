package com.diting.pingxingren.entity;

/**
 * Created by Gu FanFan.
 * Date: 2017/5/27, 15:06.
 */

public class VoicePeopleModel {

    private int mPeopleHead;
    private String mPeopleName;
    private String mPeopleCode;

    public int getPeopleHead() {
        return mPeopleHead;
    }

    public void setPeopleHead(int peopleHead) {
        mPeopleHead = peopleHead;
    }

    public String getPeopleName() {
        return mPeopleName;
    }

    public void setPeopleName(String peopleName) {
        mPeopleName = peopleName;
    }

    public String getPeopleCode() {
        return mPeopleCode;
    }

    public void setPeopleCode(String peopleCode) {
        mPeopleCode = peopleCode;
    }

    @Override
    public String toString() {
        return "VoicePeopleModel{" +
                "mPeopleHead=" + mPeopleHead +
                ", mPeopleName='" + mPeopleName + '\'' +
                ", mPeopleCode='" + mPeopleCode + '\'' +
                '}';
    }
}
