package com.example.newsgateway;

import android.graphics.Bitmap;
//import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

// Must be Serializable since it will be an intent extra
public class Story implements Serializable {

    private String source;
    private String headline;
    private String date;
    private String author;
    private String story;
    private Bitmap image;

    Story(String source, String headline, String date, String author, String story, Bitmap image) {
        this.source = source;
        this.headline = headline;
        this.date = date;
        this.author = author;
        this.story = story;
        this.image = image;
    }

    String getSource(){
        return source;
    }

    String getHeadline(){
        return headline;
    }

    String getDate(){
        return date;
    }

    String getAuthor(){
        return author;
    }

    String getStory(){
        return story;
    }

    Bitmap getImage(){
        return image;
    }
}
