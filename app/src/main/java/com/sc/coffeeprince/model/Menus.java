package com.sc.coffeeprince.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by young on 2017-08-11.
 */

public class Menus implements Parcelable{
    int id;
    int mindex;
    String name;
    String content;
    String image;
    int price;
    int groupmenusId;

    public Menus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMindex() {
        return mindex;
    }

    public void setMindex(int mindex) {
        this.mindex = mindex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGroupmenusId() {
        return groupmenusId;
    }

    public void setGroupmenusId(int groupmenusId) {
        this.groupmenusId = groupmenusId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.mindex);
        parcel.writeString(this.name);
        parcel.writeString(this.content);
        parcel.writeString(this.image);
        parcel.writeInt(this.price);
        parcel.writeInt(this.groupmenusId);

    }

    private Menus(Parcel in) {
        this.id = in.readInt();
        this.mindex = in.readInt();
        this.name = in.readString();
        this.content = in.readString();
        this.image = in.readString();
        this.price = in.readInt();
        this.groupmenusId = in.readInt();
    }

    public static final Parcelable.Creator<Menus> CREATOR = new Parcelable.Creator<Menus>() {
        public Menus createFromParcel(Parcel in) {
            return new Menus(in);
        }
        public Menus[] newArray (int size) {
            return new Menus[size];
        }
    };

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
