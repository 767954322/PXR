package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 13, 17:45.
 * Description: .
 */

public class KnowledgeItemModel implements Parcelable {

    private int emotion;
    private String video_url;
    private String file_url;
    private int ownerType;
    private String scene;
    private String audio_url;
    private int frequency;
    private int owner;
    private String actionOption;
    private int id;
    private int accountId;
    private String hyperlink_url;
    private int companyId;
    private String explain;
    private String question;
    private String internalInstruction;
    private boolean highFrequencyQuestion;
    private String synonymQuestion;
    private String answer;
    private String img_url;
    private String productId;
    private String username;
    private boolean deleted;
    private String attrValue;
    private String kw1;
    private String action;
    private String kw3;
    private String kw2;
    private String kw5;
    private String kw4;
    private String createdBy;
    private String updatedBy;
    private long updatedTime;
    private String attr;
    private String handleQuestion;
    private String ids;
    private int robotId;
    private long createdTime;
    private boolean isAddCommon;

    protected KnowledgeItemModel(Parcel in) {
        emotion = in.readInt();
        video_url = in.readString();
        file_url = in.readString();
        ownerType = in.readInt();
        scene = in.readString();
        audio_url = in.readString();
        frequency = in.readInt();
        owner = in.readInt();
        actionOption = in.readString();
        id = in.readInt();
        accountId = in.readInt();
        hyperlink_url = in.readString();
        companyId = in.readInt();
        explain = in.readString();
        question = in.readString();
        internalInstruction = in.readString();
        highFrequencyQuestion = in.readByte() != 0;
        synonymQuestion = in.readString();
        answer = in.readString();
        img_url = in.readString();
        productId = in.readString();
        username = in.readString();
        deleted = in.readByte() != 0;
        attrValue = in.readString();
        kw1 = in.readString();
        action = in.readString();
        kw3 = in.readString();
        kw2 = in.readString();
        kw5 = in.readString();
        kw4 = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        updatedTime = in.readLong();
        attr = in.readString();
        handleQuestion = in.readString();
        ids = in.readString();
        robotId = in.readInt();
        createdTime = in.readLong();
        isAddCommon = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(emotion);
        dest.writeString(video_url);
        dest.writeString(file_url);
        dest.writeInt(ownerType);
        dest.writeString(scene);
        dest.writeString(audio_url);
        dest.writeInt(frequency);
        dest.writeInt(owner);
        dest.writeString(actionOption);
        dest.writeInt(id);
        dest.writeInt(accountId);
        dest.writeString(hyperlink_url);
        dest.writeInt(companyId);
        dest.writeString(explain);
        dest.writeString(question);
        dest.writeString(internalInstruction);
        dest.writeByte((byte) (highFrequencyQuestion ? 1 : 0));
        dest.writeString(synonymQuestion);
        dest.writeString(answer);
        dest.writeString(img_url);
        dest.writeString(productId);
        dest.writeString(username);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(attrValue);
        dest.writeString(kw1);
        dest.writeString(action);
        dest.writeString(kw3);
        dest.writeString(kw2);
        dest.writeString(kw5);
        dest.writeString(kw4);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeLong(updatedTime);
        dest.writeString(attr);
        dest.writeString(handleQuestion);
        dest.writeString(ids);
        dest.writeInt(robotId);
        dest.writeLong(createdTime);
        dest.writeByte((byte) (isAddCommon ? 1 : 0));
    }

    @Override
    public String toString() {
        return "KnowledgeItemModel{" +
                "emotion=" + emotion +
                ", video_url='" + video_url + '\'' +
                ", file_url='" + file_url + '\'' +
                ", ownerType=" + ownerType +
                ", scene='" + scene + '\'' +
                ", audio_url='" + audio_url + '\'' +
                ", frequency=" + frequency +
                ", owner=" + owner +
                ", actionOption='" + actionOption + '\'' +
                ", id=" + id +
                ", accountId=" + accountId +
                ", hyperlink_url='" + hyperlink_url + '\'' +
                ", companyId=" + companyId +
                ", explain='" + explain + '\'' +
                ", question='" + question + '\'' +
                ", internalInstruction='" + internalInstruction + '\'' +
                ", highFrequencyQuestion=" + highFrequencyQuestion +
                ", synonymQuestion='" + synonymQuestion + '\'' +
                ", answer='" + answer + '\'' +
                ", img_url='" + img_url + '\'' +
                ", productId='" + productId + '\'' +
                ", username='" + username + '\'' +
                ", deleted=" + deleted +
                ", attrValue='" + attrValue + '\'' +
                ", kw1='" + kw1 + '\'' +
                ", action='" + action + '\'' +
                ", kw3='" + kw3 + '\'' +
                ", kw2='" + kw2 + '\'' +
                ", kw5='" + kw5 + '\'' +
                ", kw4='" + kw4 + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", attr='" + attr + '\'' +
                ", handleQuestion='" + handleQuestion + '\'' +
                ", ids='" + ids + '\'' +
                ", robotId=" + robotId +
                ", createdTime=" + createdTime +
                ", isAddCommon=" + isAddCommon +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KnowledgeItemModel> CREATOR = new Creator<KnowledgeItemModel>() {
        @Override
        public KnowledgeItemModel createFromParcel(Parcel in) {
            return new KnowledgeItemModel(in);
        }

        @Override
        public KnowledgeItemModel[] newArray(int size) {
            return new KnowledgeItemModel[size];
        }
    };

    public boolean isAddCommon() {
        return isAddCommon;
    }

    public void setAddCommon(boolean addCommon) {
        isAddCommon = addCommon;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getHyperlink_url() {
        return hyperlink_url;
    }

    public void setHyperlink_url(String hyperlink_url) {
        this.hyperlink_url = hyperlink_url;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getInternalInstruction() {
        return internalInstruction;
    }

    public void setInternalInstruction(String internalInstruction) {
        this.internalInstruction = internalInstruction;
    }

    public boolean isHighFrequencyQuestion() {
        return highFrequencyQuestion;
    }

    public void setHighFrequencyQuestion(boolean highFrequencyQuestion) {
        this.highFrequencyQuestion = highFrequencyQuestion;
    }

    public String getSynonymQuestion() {
        return synonymQuestion;
    }

    public void setSynonymQuestion(String synonymQuestion) {
        this.synonymQuestion = synonymQuestion;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getKw1() {
        return kw1;
    }

    public void setKw1(String kw1) {
        this.kw1 = kw1;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKw3() {
        return kw3;
    }

    public void setKw3(String kw3) {
        this.kw3 = kw3;
    }

    public String getKw2() {
        return kw2;
    }

    public void setKw2(String kw2) {
        this.kw2 = kw2;
    }

    public String getKw5() {
        return kw5;
    }

    public void setKw5(String kw5) {
        this.kw5 = kw5;
    }

    public String getKw4() {
        return kw4;
    }

    public void setKw4(String kw4) {
        this.kw4 = kw4;
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

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getHandleQuestion() {
        return handleQuestion;
    }

    public void setHandleQuestion(String handleQuestion) {
        this.handleQuestion = handleQuestion;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getRobotId() {
        return robotId;
    }

    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }


    public KnowledgeItemModel() {
    }


}
