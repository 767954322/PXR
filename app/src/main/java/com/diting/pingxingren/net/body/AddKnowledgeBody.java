package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 15, 15:51.
 * Description: .
 */

public class AddKnowledgeBody implements Parcelable {

    private int id;
    private String question;
    private String answer;
    private String actionOption;
    private String img_url;
    private String video_url;
    private String audio_url;
    private String file_url;
    private String scene;
    private String hyperlink_url;

    public AddKnowledgeBody() {
    }

    @Override
    public String toString() {
        return "AddKnowledgeBody{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", actionOption='" + actionOption + '\'' +
                ", img_url='" + img_url + '\'' +
                ", video_url='" + video_url + '\'' +
                ", audio_url='" + audio_url + '\'' +
                ", file_url='" + file_url + '\'' +
                ", scene='" + scene + '\'' +
                ", hyperlink_url='" + hyperlink_url + '\'' +
                '}';
    }

    protected AddKnowledgeBody(Parcel in) {
        id = in.readInt();
        question = in.readString();
        answer = in.readString();
        actionOption = in.readString();
        img_url = in.readString();
        video_url = in.readString();
        audio_url = in.readString();
        file_url = in.readString();
        scene = in.readString();
        hyperlink_url = in.readString();
    }

    public static final Creator<AddKnowledgeBody> CREATOR = new Creator<AddKnowledgeBody>() {
        @Override
        public AddKnowledgeBody createFromParcel(Parcel in) {
            return new AddKnowledgeBody(in);
        }

        @Override
        public AddKnowledgeBody[] newArray(int size) {
            return new AddKnowledgeBody[size];
        }
    };

    public String getHyperlink_url() {
        return hyperlink_url;
    }

    public void setHyperlink_url(String hyperlink_url) {
        this.hyperlink_url = hyperlink_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getActionOption() {
        return actionOption;
    }

    public void setActionOption(String actionOption) {
        this.actionOption = actionOption;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(actionOption);
        dest.writeString(img_url);
        dest.writeString(video_url);
        dest.writeString(audio_url);
        dest.writeString(file_url);
        dest.writeString(scene);
        dest.writeString(hyperlink_url);
    }
}
