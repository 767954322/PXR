package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/25.
 */

public class ChatLogItemModel implements Parcelable {


    /**
     * robotName : null
     * appKey : 01db1e57d5be467ea2ebcc6493355e45
     * headImgUrl : null
     * newUsername : null
     * loginUsername : 13811117777
     * ip : 127.0.0.1
     * userId : null
     * scene : null
     * audio_url : null
     * num : null
     * questions : null
     * owner : null
     * actionOption : null
     * id : 1478175
     * uuid : dgLIBIFQFSTHcGLRLERgSfGCRYLHhDKK
     * question : 同意
     * video_url : null
     * extra1 : 0
     * extra2 : null
     * extra3 : null
     * extra4 : 1
     * attentionState : null
     * answer : 对某种主张表示相同的意见；赞成；准许：我的意见你～吗？①党委会～你们的要求。
     * img_url : null
     * username : 13811117777
     * companyName : null
     * deleted : false
     * ownerType : null
     * welcome : null
     * profile : null
     * dataSource : A
     * createdBy : null
     * updatedBy : null
     * oldUsername : null
     * hyperlink_url : null
     * count : null
     * updatedTime : 1514180742166
     * charge_mode : 普通模式
     * file_url : null
     * createdTime : 1514180742166
     * robotValue : null
     */

    private String robotName;
    private String appKey;
    private String headImgUrl;
    private String newUsername;
    private String loginUsername;
    private String ip;
    private String userId;
    private String scene;
    private String audio_url;
    private String num;
    private String questions;
    private String owner;
    private String actionOption;
    private int id;
    private String uuid;
    private String question;
    private String video_url;
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;
    private String attentionState;
    private String answer;
    private String img_url;
    private String username;
    private String companyName;
    private boolean deleted;
    private String ownerType;
    private String welcome;
    private String profile;
    private String dataSource;
    private String createdBy;
    private String updatedBy;
    private String oldUsername;
    private String hyperlink_url;
    private String count;
    private long updatedTime;
    private String charge_mode;
    private String file_url;
    private long createdTime;
    private String robotValue;

    protected ChatLogItemModel(Parcel in) {
        robotName = in.readString();
        appKey = in.readString();
        headImgUrl = in.readString();
        newUsername = in.readString();
        loginUsername = in.readString();
        ip = in.readString();
        userId = in.readString();
        scene = in.readString();
        audio_url = in.readString();
        num = in.readString();
        questions = in.readString();
        owner = in.readString();
        actionOption = in.readString();
        id = in.readInt();
        uuid = in.readString();
        question = in.readString();
        video_url = in.readString();
        extra1 = in.readString();
        extra2 = in.readString();
        extra3 = in.readString();
        extra4 = in.readString();
        attentionState = in.readString();
        answer = in.readString();
        img_url = in.readString();
        username = in.readString();
        companyName = in.readString();
        deleted = in.readByte() != 0;
        ownerType = in.readString();
        welcome = in.readString();
        profile = in.readString();
        dataSource = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        oldUsername = in.readString();
        hyperlink_url = in.readString();
        count = in.readString();
        updatedTime = in.readLong();
        charge_mode = in.readString();
        file_url = in.readString();
        createdTime = in.readLong();
        robotValue = in.readString();
    }

    public static final Creator<ChatLogItemModel> CREATOR = new Creator<ChatLogItemModel>() {
        @Override
        public ChatLogItemModel createFromParcel(Parcel in) {
            return new ChatLogItemModel(in);
        }

        @Override
        public ChatLogItemModel[] newArray(int size) {
            return new ChatLogItemModel[size];
        }
    };

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getActionOption() {
        return actionOption;
    }

    public void setActionOption(String actionOption) {
        this.actionOption = actionOption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getHyperlink_url() {
        return hyperlink_url;
    }

    public void setHyperlink_url(String hyperlink_url) {
        this.hyperlink_url = hyperlink_url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(String charge_mode) {
        this.charge_mode = charge_mode;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getRobotValue() {
        return robotValue;
    }

    public void setRobotValue(String robotValue) {
        this.robotValue = robotValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(robotName);
        dest.writeString(appKey);
        dest.writeString(headImgUrl);
        dest.writeString(newUsername);
        dest.writeString(loginUsername);
        dest.writeString(ip);
        dest.writeString(userId);
        dest.writeString(scene);
        dest.writeString(audio_url);
        dest.writeString(num);
        dest.writeString(questions);
        dest.writeString(owner);
        dest.writeString(actionOption);
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeString(question);
        dest.writeString(video_url);
        dest.writeString(extra1);
        dest.writeString(extra2);
        dest.writeString(extra3);
        dest.writeString(extra4);
        dest.writeString(attentionState);
        dest.writeString(answer);
        dest.writeString(img_url);
        dest.writeString(username);
        dest.writeString(companyName);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(ownerType);
        dest.writeString(welcome);
        dest.writeString(profile);
        dest.writeString(dataSource);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeString(oldUsername);
        dest.writeString(hyperlink_url);
        dest.writeString(count);
        dest.writeLong(updatedTime);
        dest.writeString(charge_mode);
        dest.writeString(file_url);
        dest.writeLong(createdTime);
        dest.writeString(robotValue);
    }
}
