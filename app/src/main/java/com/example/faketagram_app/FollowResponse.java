package com.example.faketagram_app;

public class FollowResponse {
    private int user_followed_id;
    private int user_follower_id;

    public int getUser_followed_id() {
        return user_followed_id;
    }

    public void setUser_followed_id(int user_followed_id) {
        this.user_followed_id = user_followed_id;
    }

    public int getUser_follower_id() {
        return user_follower_id;
    }

    public void setUser_follower_id(int user_follower_id) {
        this.user_follower_id = user_follower_id;
    }
}
