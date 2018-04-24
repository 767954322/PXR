package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:20.
 * Description: .
 */

public class ArticleModel implements Parcelable {


    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<ItemsBean> items;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Parcelable {

        private String updatedtime;
        private String star;
        private String title;
        private EditorTypeBean editortype;
        private String editortion;
        private int id;
        private String robot_id;
        private String createdtime;
        private String deletetype;
        private String iec_id;

        public String getUpdatedtime() {
            return updatedtime;
        }

        public void setUpdatedtime(String updatedtime) {
            this.updatedtime = updatedtime;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public EditorTypeBean getEditortype() {
            return editortype;
        }

        public void setEditortype(EditorTypeBean editortype) {
            this.editortype = editortype;
        }

        public String getEditortion() {
            return editortion;
        }

        public void setEditortion(String editortion) {
            this.editortion = editortion;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRobot_id() {
            return robot_id;
        }

        public void setRobot_id(String robot_id) {
            this.robot_id = robot_id;
        }

        public String getCreatedtime() {
            return createdtime;
        }

        public void setCreatedtime(String createdtime) {
            this.createdtime = createdtime;
        }

        public String getDeletetype() {
            return deletetype;
        }

        public void setDeletetype(String deletetype) {
            this.deletetype = deletetype;
        }

        public String getIec_id() {
            return iec_id;
        }

        public void setIec_id(String iec_id) {
            this.iec_id = iec_id;
        }

        public static class EditorTypeBean implements Parcelable {

            private String robot_id;
            private String id;
            private String classification;

            public String getRobot_id() {
                return robot_id;
            }

            public void setRobot_id(String robot_id) {
                this.robot_id = robot_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getClassification() {
                return classification;
            }

            public void setClassification(String classification) {
                this.classification = classification;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.robot_id);
                dest.writeString(this.id);
                dest.writeString(this.classification);
            }

            public EditorTypeBean() {
            }

            protected EditorTypeBean(Parcel in) {
                this.robot_id = in.readString();
                this.id = in.readString();
                this.classification = in.readString();
            }

            public static final Creator<EditorTypeBean> CREATOR = new Creator<EditorTypeBean>() {
                @Override
                public EditorTypeBean createFromParcel(Parcel source) {
                    return new EditorTypeBean(source);
                }

                @Override
                public EditorTypeBean[] newArray(int size) {
                    return new EditorTypeBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.updatedtime);
            dest.writeString(this.star);
            dest.writeString(this.title);
            dest.writeParcelable(this.editortype, flags);
            dest.writeString(this.editortion);
            dest.writeInt(this.id);
            dest.writeString(this.robot_id);
            dest.writeString(this.createdtime);
            dest.writeString(this.deletetype);
            dest.writeString(this.iec_id);
        }

        public ItemsBean() {
        }

        protected ItemsBean(Parcel in) {
            this.updatedtime = in.readString();
            this.star = in.readString();
            this.title = in.readString();
            this.editortype = in.readParcelable(EditorTypeBean.class.getClassLoader());
            this.editortion = in.readString();
            this.id = in.readInt();
            this.robot_id = in.readString();
            this.createdtime = in.readString();
            this.deletetype = in.readString();
            this.iec_id = in.readString();
        }

        public static final Parcelable.Creator<ItemsBean> CREATOR = new Parcelable.Creator<ItemsBean>() {
            @Override
            public ItemsBean createFromParcel(Parcel source) {
                return new ItemsBean(source);
            }

            @Override
            public ItemsBean[] newArray(int size) {
                return new ItemsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalPage);
        dest.writeInt(this.pageNo);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.totalRecord);
        dest.writeInt(this.total);
        dest.writeTypedList(this.items);
    }

    public ArticleModel() {
    }

    protected ArticleModel(Parcel in) {
        this.totalPage = in.readInt();
        this.pageNo = in.readInt();
        this.pageSize = in.readInt();
        this.totalRecord = in.readInt();
        this.total = in.readInt();
        this.items = in.createTypedArrayList(ItemsBean.CREATOR);
    }

    public static final Parcelable.Creator<ArticleModel> CREATOR = new Parcelable.Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel source) {
            return new ArticleModel(source);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };
}
