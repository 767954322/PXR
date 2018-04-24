package com.diting.voice.data.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通话记录
 * Created by asus on 2017/8/16.
 */

public class VoiceCallInfo implements Parcelable{

    private String fromUserName;
    private String toUserName;
    private String callType;
    private String fromRobotName;
    private String fromCompanyName;
    private String toRobotName;
    private String toCompanyName;
    private String startTime;
    private int callTime;
    private String endType;
    private String fromHeadImgUrl;
    private String toheadImgUrl;


    public VoiceCallInfo() {
    }

    public String getEndType() {
        return endType;
    }

    public void setEndType(EndType endType) {
        this.endType = endType.toString();
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType.toString();
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromRobotName() {
        return fromRobotName;
    }

    public void setFromRobotName(String fromRobotName) {
        this.fromRobotName = fromRobotName;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getToRobotName() {
        return toRobotName;
    }

    public void setToRobotName(String toRobotName) {
        this.toRobotName = toRobotName;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getCallTime() {
        return callTime;
    }

    public void setCallTime(int callTime) {
        this.callTime = callTime;
    }

    public String getFromHeadImgUrl() {
        return fromHeadImgUrl;
    }

    public void setFromHeadImgUrl(String fromHeadImgUrl) {
        this.fromHeadImgUrl = fromHeadImgUrl;
    }

    public String getToheadImgUrl() {
        return toheadImgUrl;
    }

    public void setToheadImgUrl(String toheadImgUrl) {
        this.toheadImgUrl = toheadImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromUserName);
        dest.writeString(toUserName);
        dest.writeString(callType.toString());
        dest.writeString(fromRobotName);
        dest.writeString(fromCompanyName);
        dest.writeString(toRobotName);
        dest.writeString(toCompanyName);
        dest.writeString(startTime);
        dest.writeInt(callTime);
        dest.writeString(endType.toString());
        dest.writeString(fromHeadImgUrl);
        dest.writeString(toheadImgUrl);
    }

    protected VoiceCallInfo(Parcel in) {
        fromUserName = in.readString();
        toUserName = in.readString();
        callType = in.readString();
        fromRobotName = in.readString();
        fromCompanyName = in.readString();
        toRobotName = in.readString();
        toCompanyName = in.readString();
        startTime = in.readString();
        callTime = in.readInt();
        endType = in.readString();
        fromHeadImgUrl = in.readString();
        toheadImgUrl = in.readString();
    }

    public static final Creator<VoiceCallInfo> CREATOR = new Creator<VoiceCallInfo>() {
        @Override
        public VoiceCallInfo createFromParcel(Parcel in) {
            return new VoiceCallInfo(in);
        }

        @Override
        public VoiceCallInfo[] newArray(int size) {
            return new VoiceCallInfo[size];
        }
    };


    public enum CallType{
        VOICE,  // 音频
        VIDEO   //视频
    }

    public enum EndType{
        NORMAL, //正常结束
        REJECT, //拒接
        NORESPONSE  //无应答
    }

}
