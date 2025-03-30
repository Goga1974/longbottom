package com.goga74.platform.controller.dto;

import com.google.gson.annotations.Expose;

public class LoginRequest
{
    @Expose
    private String userId; // Corresponds to `user_id` in the table
    @Expose
    private String username; // Corresponds to `user_name` in the table
    @Expose
    private String password; // Corresponds to `pass` in the table

    public String getUserId() {
        return userId;
    }

    public LoginRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}