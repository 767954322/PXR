package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 添加数据到收藏
 * Created by Administrator on 2018/1/9.
 */

public class AddCollectionBody implements Parcelable {
    private String url;//收藏地址信息
    private String text;//收藏文本信息
    private int sort;//收藏分类
    private long timer;//收藏时间
    private String onlinerobotname;//登录机器人名称
    private String onlineuniqueid;//登录机器人uniqueid
    private String chatrobotname;//聊天机器人名称
    private String chatuniqueid;//聊天机器人uniqueid

    @Override
    public String toString() {
        return "AddCollectionBody{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", sort=" + sort +
                ", timer=" + timer +
                ", onlinerobotname='" + onlinerobotname + '\'' +
                ", onlineuniqueid='" + onlineuniqueid + '\'' +
                ", chatrobotname='" + chatrobotname + '\'' +
                ", chatuniqueid='" + chatuniqueid + '\'' +
                '}';
    }

    public AddCollectionBody() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public String getOnlinerobotname() {
        return onlinerobotname;
    }

    public void setOnlinerobotname(String onlinerobotname) {
        this.onlinerobotname = onlinerobotname;
    }

    public String getOnlineuniqueid() {
        return onlineuniqueid;
    }

    public void setOnlineuniqueid(String onlineuniqueid) {
        this.onlineuniqueid = onlineuniqueid;
    }

    public String getChatrobotname() {
        return chatrobotname;
    }

    public void setChatrobotname(String chatrobotname) {
        this.chatrobotname = chatrobotname;
    }

    public String getChatuniqueid() {
        return chatuniqueid;
    }

    public void setChatuniqueid(String chatuniqueid) {
        this.chatuniqueid = chatuniqueid;
    }

    public static Creator<AddCollectionBody> getCREATOR() {
        return CREATOR;
    }

    protected AddCollectionBody(Parcel in) {
        url = in.readString();
        text = in.readString();
        sort = in.readInt();
        timer = in.readLong();
        onlinerobotname = in.readString();
        onlineuniqueid = in.readString();
        chatrobotname = in.readString();
        chatuniqueid = in.readString();
    }

    public static final Creator<AddCollectionBody> CREATOR = new Creator<AddCollectionBody>() {
        @Override
        public AddCollectionBody createFromParcel(Parcel in) {
            return new AddCollectionBody(in);
        }

        @Override
        public AddCollectionBody[] newArray(int size) {
            return new AddCollectionBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(text);
        parcel.writeInt(sort);
        parcel.writeLong(timer);
        parcel.writeString(onlinerobotname);
        parcel.writeString(onlineuniqueid);
        parcel.writeString(chatrobotname);
        parcel.writeString(chatuniqueid);
    }
}
