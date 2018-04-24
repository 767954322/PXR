package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 11:54.
 * Description: 对话请求体.
 */

public class VoiceLoginQuestBody implements Parcelable {

    private String uuid;
    private String question;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.question);
    }

    protected VoiceLoginQuestBody(Parcel in) {
        this.uuid = in.readString();
        this.question = in.readString();
    }

    public VoiceLoginQuestBody() {
    }

    public static final Creator<VoiceLoginQuestBody> CREATOR = new Creator<VoiceLoginQuestBody>() {
        @Override
        public VoiceLoginQuestBody createFromParcel(Parcel source) {
            return new VoiceLoginQuestBody(source);
        }

        @Override
        public VoiceLoginQuestBody[] newArray(int size) {
            return new VoiceLoginQuestBody[size];
        }
    };
}
