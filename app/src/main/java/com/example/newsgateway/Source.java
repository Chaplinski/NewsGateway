package com.example.newsgateway;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Source implements Serializable {

    private String id;
    private String name;
    private String category;

    Source(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

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
}
