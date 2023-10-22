package com.cjmkeke.mymemo.modelClass;

public class ModelMyFriends {

    private String email;
    private String profile;
    private String token;
    private String friendKey;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendKey() {
        return friendKey;
    }

    public void setFriendKey(String friendKey) {
        this.friendKey = friendKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
