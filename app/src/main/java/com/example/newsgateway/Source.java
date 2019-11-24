package com.example.newsgateway;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Source implements Serializable, Parcelable {

    private String id;
    private String name;
    private String category;

    Source(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    protected Source(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }

    @NonNull
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
    }
}
