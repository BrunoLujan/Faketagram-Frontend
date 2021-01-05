package com.example.faketagram_app.model;

public class Comments {
    private int comment_id;
    private String publish_date;
    private String comment;
    private int user_id;
    private int photograph_id;

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPhotograph_id() {
        return photograph_id;
    }

    public void setPhotograph_id(int photograph_id) {
        this.photograph_id = photograph_id;
    }
}
