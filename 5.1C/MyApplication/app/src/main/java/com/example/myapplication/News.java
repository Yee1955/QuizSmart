package com.example.myapplication;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String description;
    private String imageURL;

    public News(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.imageURL = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
