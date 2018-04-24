package com.diting.pingxingren.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/1/18.
 */

public class Knowledge implements Parcelable{
    private Integer knowledgeId;
    private String question;
    private String answer;
    private String scene;
    private String picture;
    private String video;
    private String file;
    private String hyperlink;
    private String audio;

    public Integer getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Integer knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(knowledgeId);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(scene);
        dest.writeString(picture);
        dest.writeString(video);
        dest.writeString(audio);
        dest.writeString(file);
        dest.writeString(hyperlink);
    }

    public static final Parcelable.Creator<Knowledge> CREATOR = new Creator<Knowledge>() {
        @Override
        public Knowledge createFromParcel(Parcel source) {
            Knowledge knowledge = new Knowledge();
            knowledge.knowledgeId = source.readInt();
            knowledge.question = source.readString();
            knowledge.answer = source.readString();
            knowledge.scene = source.readString();
            knowledge.picture = source.readString();
            knowledge.video = source.readString();
            knowledge.audio = source.readString();
            knowledge.file = source.readString();
            knowledge.hyperlink = source.readString();
            return knowledge;
        }

        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }
    };
}
