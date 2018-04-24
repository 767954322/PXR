package com.diting.pingxingren.entity;

import java.util.Date;

/**
 * Created by asus on 2017/1/17.
 */

public class Mail {
    public static final int FROM_SYSTEM = 1;
    public static final int FROM_ROBOT = 2;
    private Integer MailId;
    private String title;
    private String photo_url;
    private String uniqueId;
    private Date time;
    private String content;
    private boolean isRead;

    public Integer getMailId() {
        return MailId;
    }

    public void setMailId(Integer mailId) {
        MailId = mailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}
