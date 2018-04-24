package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的收藏
 * Created by Administrator on 2018/1/5.
 */

public class CollectionModel implements Parcelable{

    private String   id;//收藏id
    private String  url;//收藏地址信息
    private String  text;//收藏文本信息
    private int sort;//收藏分类   //1.音频  2.文件  3.  图片  4.视频  5.文本
    private long  timer;//收藏时间
    private String  onlinerobotname;//登录机器人名称
    private String  onlineuniqueid;//登录机器人uniqueid
    private String  chatrobotname;//聊天机器人名称
    private String  chatuniqueid;//聊天机器人uniqueid

    public CollectionModel() {
    }


    @Override
    public String toString() {
        return "CollectionModel{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", sort=" + sort +
                ", timer=" + timer +
                ", onlinerobotname='" + onlinerobotname + '\'' +
                ", onlineuniqueid='" + onlineuniqueid + '\'' +
                ", chatrobotname='" + chatrobotname + '\'' +
                ", chatuniqueid='" + chatuniqueid + '\'' +
                '}';
    }

    protected CollectionModel(Parcel in) {
        id = in.readString();
        url = in.readString();
        text = in.readString();
        sort = in.readInt();
        timer = in.readLong();
        onlinerobotname = in.readString();
        onlineuniqueid = in.readString();
        chatrobotname = in.readString();
        chatuniqueid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(text);
        dest.writeInt(sort);
        dest.writeLong(timer);
        dest.writeString(onlinerobotname);
        dest.writeString(onlineuniqueid);
        dest.writeString(chatrobotname);
        dest.writeString(chatuniqueid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectionModel> CREATOR = new Creator<CollectionModel>() {
        @Override
        public CollectionModel createFromParcel(Parcel in) {
            return new CollectionModel(in);
        }

        @Override
        public CollectionModel[] newArray(int size) {
            return new CollectionModel[size];
        }
    };

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



}
