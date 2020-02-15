package com.google.healthme.Model;

public class CategoryModel {
    private String imageLink, name;

    public CategoryModel() {
    }

    public CategoryModel(String imageLink, String name) {
        this.imageLink = imageLink;
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
