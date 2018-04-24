package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 14, 13:28.
 * Description: .
 */

public class PublicChatModel implements Parcelable {

    private ActionBean action;
    private String answer;
    private String companyName;
    private String headImgUrl;
    private String img_url;
    private String message;
    private String profile;
    private String robotName;
    private String skip_url;
    private String username;
    private String uuid;
    private String welcome;
    private String hyperlinkUrl;
    private String audioUrl;
    private String fileUrl;
    private String videoUrl;


    protected PublicChatModel(Parcel in) {
        action = in.readParcelable(ActionBean.class.getClassLoader());
        answer = in.readString();
        companyName = in.readString();
        headImgUrl = in.readString();
        img_url = in.readString();
        message = in.readString();
        profile = in.readString();
        robotName = in.readString();
        skip_url = in.readString();
        username = in.readString();
        uuid = in.readString();
        welcome = in.readString();
        hyperlinkUrl = in.readString();
        audioUrl = in.readString();
        fileUrl = in.readString();
        videoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(action, flags);
        dest.writeString(answer);
        dest.writeString(companyName);
        dest.writeString(headImgUrl);
        dest.writeString(img_url);
        dest.writeString(message);
        dest.writeString(profile);
        dest.writeString(robotName);
        dest.writeString(skip_url);
        dest.writeString(username);
        dest.writeString(uuid);
        dest.writeString(welcome);
        dest.writeString(hyperlinkUrl);
        dest.writeString(audioUrl);
        dest.writeString(fileUrl);
        dest.writeString(videoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PublicChatModel> CREATOR = new Creator<PublicChatModel>() {
        @Override
        public PublicChatModel createFromParcel(Parcel in) {
            return new PublicChatModel(in);
        }

        @Override
        public PublicChatModel[] newArray(int size) {
            return new PublicChatModel[size];
        }
    };

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getHyperlinkUrl() {
        return hyperlinkUrl;
    }

    public void setHyperlinkUrl(String hyperlinkUrl) {
        this.hyperlinkUrl = hyperlinkUrl;
    }

    public ActionBean getAction() {
        return action;
    }

    public void setAction(ActionBean action) {
        this.action = action;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getSkip_url() {
        return skip_url;
    }

    public void setSkip_url(String skip_url) {
        this.skip_url = skip_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }




    public static class ActionBean implements Parcelable {

        private String actionOption;
        private String available;
        private String question;

        public String getActionOption() {
            return actionOption;
        }

        public void setActionOption(String actionOption) {
            this.actionOption = actionOption;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
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
            dest.writeString(this.actionOption);
            dest.writeString(this.available);
            dest.writeString(this.question);
        }

        public ActionBean() {
        }

        protected ActionBean(Parcel in) {
            this.actionOption = in.readString();
            this.available = in.readString();
            this.question = in.readString();
        }

        public static final Parcelable.Creator<ActionBean> CREATOR = new Parcelable.Creator<ActionBean>() {
            @Override
            public ActionBean createFromParcel(Parcel source) {
                return new ActionBean(source);
            }

            @Override
            public ActionBean[] newArray(int size) {
                return new ActionBean[size];
            }
        };
    }


    public PublicChatModel() {
    }


    @Override
    public String toString() {
        return "PublicChatModel{" +
                "action=" + action +
                ", answer='" + answer + '\'' +
                ", companyName='" + companyName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", img_url='" + img_url + '\'' +
                ", message='" + message + '\'' +
                ", profile='" + profile + '\'' +
                ", robotName='" + robotName + '\'' +
                ", skip_url='" + skip_url + '\'' +
                ", username='" + username + '\'' +
                ", uuid='" + uuid + '\'' +
                ", welcome='" + welcome + '\'' +
                ", hyperlinkUrl='" + hyperlinkUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
