package com.diting.pingxingren.entity;

import com.diting.pingxingren.util.MySharedPreferences;

/**
 * Created by asus on 2016/4/11.
 */
public class Msg {
    public static final int TYPE_RECEIVED_TEXT = 0;
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED_IMAGE = 2;
    public static final int TYPE_RECEIVED_TEXT_AND_IMAGE = 3;
    public static final int TYPE_RECEIVED_BOOK = 4;
    public static final int TYPE_SEND_VOICE = 5;
    public static final int TYPE_RECEIVED_VOICE = 6;
    public static final int TYPE_RECEIVED_MUSIC = 7;
    public static final int TYPE_RECEIVED_REMIND = 8;
    public static final int TYPE_RECEIVED_FOOD = 9;
    public static final int TYPE_RECEIVED_MAIL = 10;
    public static final int TYPE_PLAY = 11;
    public static final int TYPE_RECEIVED_VIDEO = 12;
    public static final int TYPE_RECEIVED_AUDIO = 13;
    public static final int TYPE_RECEIVED_FILE = 14;
    public static final int TYPE_RECEIVED_TEXT_AND_VIDEO = 13;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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
}
