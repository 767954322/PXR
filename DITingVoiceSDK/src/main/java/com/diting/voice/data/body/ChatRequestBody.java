package com.diting.voice.data.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 11:54.
 * Description: 对话请求体.
 */

public class ChatRequestBody implements Parcelable {

    private String uuid;
    private String question;
    private String username;
    private String source;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.question);
        dest.writeString(this.username);
        dest.writeString(this.source);
    }

    public ChatRequestBody() {
    }

    protected ChatRequestBody(Parcel in) {
        this.uuid = in.readString();
        this.question = in.readString();
        this.username = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<ChatRequestBody> CREATOR = new Parcelable.Creator<ChatRequestBody>() {
        @Override
        public ChatRequestBody createFromParcel(Parcel source) {
            return new ChatRequestBody(source);
        }

        @Override
        public ChatRequestBody[] newArray(int size) {
            return new ChatRequestBody[size];
        }
    };
}
