package com.example.newsgateway;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
//import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

// Must be Serializable since it will be an intent extra
public class Story implements Serializable, Parcelable {

    private String source;
    private String title;
    private String date;
    private String author;
    private String description;
    private String url;
    private String urlToImage;

    Story(String author, String title, String description, String url, String urlToImage, String date) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.date = date;
    }

    protected Story(Parcel in) {
        source = in.readString();
        title = in.readString();
        date = in.readString();
        author = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    String getSource(){
        return source;
    }

    String getAuthor(){
        return author;
    }

    String getTitle(){
        return title;
    }

    String getDescription(){
        return description;
    }

    String getUrl(){
        return url;
    }

    String getUrlToImage(){ return urlToImage; }

    String getDate(){
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(source);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(author);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(urlToImage);
    }
}
