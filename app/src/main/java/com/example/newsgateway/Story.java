package com.example.newsgateway;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

// Must be Serializable since it will be an intent extra
public class Story implements Serializable {

    private String source;
    private String headline;
    private String date;
    private String authors;
    private String text;
    private Bitmap image;

    Story(String source, String headline, String date, String authors, String text, Bitmap image) {
        this.source = source;
        this.headline = headline;
        this.date = date;
        this.authors = authors;
        this.text = text;
        this.image = image;
    }
}
