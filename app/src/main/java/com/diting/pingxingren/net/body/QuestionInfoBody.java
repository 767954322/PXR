package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/6, 15:10.
 * 问题请求体.
 */

public class QuestionInfoBody implements Parcelable {

    private String uuid;
    private String question;
    private String username;
    private String source;
    private int isVoice;

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

    public int getIsVoice() {
        return isVoice;
    }

    public void setIsVoice(int isVoice) {
        this.isVoice = isVoice;
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
        dest.writeInt(this.isVoice);
    }

    public QuestionInfoBody() {
    }

    protected QuestionInfoBody(Parcel in) {
        this.uuid = in.readString();
        this.question = in.readString();
        this.username = in.readString();
        this.source = in.readString();
        this.isVoice = in.readInt();
    }

    public static final Creator<QuestionInfoBody> CREATOR = new Creator<QuestionInfoBody>() {
        @Override
        public QuestionInfoBody createFromParcel(Parcel source) {
            return new QuestionInfoBody(source);
        }

        @Override
        public QuestionInfoBody[] newArray(int size) {
            return new QuestionInfoBody[size];
        }
    };
}
