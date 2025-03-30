package com.goga74.platform.dto;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    private String userName;

    @Expose
    private String data;

    // Constructor
    public User(String userName, String data) {
        this.userName = userName;
        this.data = data;
    }

    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for data
    public String getData() {
        return data;
    }

    // Setter for data
    public void setData(String data) {
        this.data = data;
    }
}