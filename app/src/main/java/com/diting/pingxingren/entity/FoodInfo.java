package com.diting.pingxingren.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/8/28.
 */

public class FoodInfo implements Parcelable{
    private String area;
    private String address;
    private String city;
    private String photos;
    private String phone;
    private String name;
    private Float latitude;
    private Float longitude;
    private float distance;

    protected FoodInfo(Parcel in) {
        area = in.readString();
        address = in.readString();
        city = in.readString();
        photos = in.readString();
        phone = in.readString();
        name = in.readString();
        distance = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(area);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(photos);
        dest.writeString(phone);
        dest.writeString(name);
        dest.writeFloat(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodInfo> CREATOR = new Creator<FoodInfo>() {
        @Override
        public FoodInfo createFromParcel(Parcel in) {
            return new FoodInfo(in);
        }

        @Override
        public FoodInfo[] newArray(int size) {
            return new FoodInfo[size];
        }
    };

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
