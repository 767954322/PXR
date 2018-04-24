package com.diting.voice.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/11, 11:35.
 * Description: 开放接口对话信息Model.
 */

public class PublicChatInfoModel implements Parcelable {

    private ActionModel action;
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

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
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

    public Object getHeadImgUrl() {
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

    public Object getProfile() {
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

    public Object getSkip_url() {
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

    public Object getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public static class ActionModel implements Parcelable {

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

        public ActionModel() {
        }

        protected ActionModel(Parcel in) {
            this.actionOption = in.readString();
            this.available = in.readString();
            this.question = in.readString();
        }

        public static final Creator<ActionModel> CREATOR = new Creator<ActionModel>() {
            @Override
            public ActionModel createFromParcel(Parcel source) {
                return new ActionModel(source);
            }

            @Override
            public ActionModel[] newArray(int size) {
                return new ActionModel[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.action, flags);
        dest.writeString(this.answer);
        dest.writeString(this.companyName);
        dest.writeString(this.headImgUrl);
        dest.writeString(this.img_url);
        dest.writeString(this.message);
        dest.writeString(this.profile);
        dest.writeString(this.robotName);
        dest.writeString(this.skip_url);
        dest.writeString(this.username);
        dest.writeString(this.uuid);
        dest.writeString(this.welcome);
    }

    public PublicChatInfoModel() {
    }

    protected PublicChatInfoModel(Parcel in) {
        this.action = in.readParcelable(ActionModel.class.getClassLoader());
        this.answer = in.readString();
        this.companyName = in.readString();
        this.headImgUrl = in.readString();
        this.img_url = in.readString();
        this.message = in.readString();
        this.profile = in.readString();
        this.robotName = in.readString();
        this.skip_url = in.readString();
        this.username = in.readString();
        this.uuid = in.readString();
        this.welcome = in.readString();
    }

    public static final Parcelable.Creator<PublicChatInfoModel> CREATOR = new Parcelable.Creator<PublicChatInfoModel>() {
        @Override
        public PublicChatInfoModel createFromParcel(Parcel source) {
            return new PublicChatInfoModel(source);
        }

        @Override
        public PublicChatInfoModel[] newArray(int size) {
            return new PublicChatInfoModel[size];
        }
    };
}
