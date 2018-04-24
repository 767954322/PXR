package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的活动列表
 * Created by Administrator on 2018/1/8.
 */

public class MyLuckyModel implements Parcelable{


    /**
     * startTims : 1515388201000
     * activityPicture : http://diting-picture.pingxingren.cn/FPeC0XGeC6ngbjNfH6Ly0i.png
     * headline : 活动标题
     * shareUrl : https://www.baidu.com
     * endTims : 1515474605000
     * content : 测试1
     * activityUrl : http://www.ditingai.com/m/
     * id : 1
     */

    private long startTims;
    private String activityPicture;
    private String headline;
    private String shareUrl;
    private long endTims;
    private String content;
    private String activityUrl;
    private int id;

    protected MyLuckyModel(Parcel in) {
        startTims = in.readLong();
        activityPicture = in.readString();
        headline = in.readString();
        shareUrl = in.readString();
        endTims = in.readLong();
        content = in.readString();
        activityUrl = in.readString();
        id = in.readInt();
    }

    public long getStartTims() {
        return startTims;
    }

    public void setStartTims(long startTims) {
        this.startTims = startTims;
    }

    public String getActivityPicture() {
        return activityPicture;
    }

    public void setActivityPicture(String activityPicture) {
        this.activityPicture = activityPicture;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public long getEndTims() {
        return endTims;
    }

    public void setEndTims(long endTims) {
        this.endTims = endTims;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Creator<MyLuckyModel> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<MyLuckyModel> CREATOR = new Creator<MyLuckyModel>() {
        @Override
        public MyLuckyModel createFromParcel(Parcel in) {
            return new MyLuckyModel(in);
        }

        @Override
        public MyLuckyModel[] newArray(int size) {
            return new MyLuckyModel[size];
        }
    };

    @Override
    public String toString() {
        return "MyLuckyModel{" +
                "startTims=" + startTims +
                ", activityUrl='" + activityUrl + '\'' +
                ", activityPicture='" + activityPicture + '\'' +
                ", id=" + id +
                ", endTims=" + endTims +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(startTims);
        parcel.writeString(activityPicture);
        parcel.writeString(headline);
        parcel.writeString(shareUrl);
        parcel.writeLong(endTims);
        parcel.writeString(content);
        parcel.writeString(activityUrl);
        parcel.writeInt(id);
    }
}
