package com.example.myapplication;

import java.io.Serializable;

public class BeerImageInfo implements Serializable {
    private String imageUrl;
    private String description;
    private String webUrl;

    public BeerImageInfo(String imageUrl, String description, String webUrl) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.webUrl = webUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
