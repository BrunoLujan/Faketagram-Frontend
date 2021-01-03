package com.example.faketagram_app.model;

public class Photographs {
    private int photograph_id;
    private String publish_date;
    private String image_storage_path;
    private int user_id;

    public int getPhotograph_id() {
        return photograph_id;
    }

    public void setPhotograph_id(int photograph_id) {
        this.photograph_id = photograph_id;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getImage_storage_path() {
        return image_storage_path;
    }

    public void setImage_storage_path(String image_storage_path) {
        this.image_storage_path = image_storage_path;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
