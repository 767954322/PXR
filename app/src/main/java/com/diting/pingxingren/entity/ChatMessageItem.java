package com.diting.pingxingren.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.diting.pingxingren.util.MySharedPreferences;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 27, 13:40.
 * Description: .
 */

public class ChatMessageItem implements MultiItemEntity {
    @Override
    public String toString() {
        return "ChatMessageItem{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", type=" + type +
                ", userName='" + userName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", time=" + time +
                ", headPortrait='" + headPortrait + '\'' +
                ", book=" + book +
                ", voiceTime=" + voiceTime +
                ", voicePath='" + voicePath + '\'' +
                ", robotName='" + robotName + '\'' +
                ", hyperlinkUrl='" + hyperlinkUrl + '\'' +
                '}';
    }

    private int id;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String fileUrl;
    private String audioUrl;
    private int type;
    private String userName;
    private String fromUserName = MySharedPreferences.getUsername();
    private long time;
    private String headPortrait;
    private Book book;
    private float voiceTime;
    private String voicePath;
    private String robotName;
    private String  hyperlinkUrl;

    public String getHyperlinkUrl() {
        return hyperlinkUrl;
    }

    public void setHyperlinkUrl(String hyperlinkUrl) {
        this.hyperlinkUrl = hyperlinkUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public float getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(float voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }


}
