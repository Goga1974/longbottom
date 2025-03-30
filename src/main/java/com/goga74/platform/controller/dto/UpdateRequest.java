package com.goga74.platform.controller.dto;

public class UpdateRequest {

    private String userId;

    private String data; // Optional

    private String userName; // Optional

    // Constructor with all parameters
    public UpdateRequest(String userId, String data, String userName) {
        this.userId = userId;
        this.data = data;
        this.userName = userName;
    }

    // Constructor with only userId
    public UpdateRequest(String userId) {
        this.userId = userId;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}