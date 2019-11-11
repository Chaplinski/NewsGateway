package com.example.newsgateway;

public class Source {

    private String id;
    private String name;
    private String category;

    private void setID(String id){
        this.id = id;
    }

    private String getID(){
        return this.id;
    }

    private void setName(String name){
        this.name = name;
    }

    private String getName(){
        return this.name;
    }

    private void setCategory(String category){
        this.category = category;
    }

    private String getCategory(){
        return this.getCategory();
    }
}
