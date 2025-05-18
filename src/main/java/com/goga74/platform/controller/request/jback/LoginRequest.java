package com.goga74.platform.controller.request.jback;

import com.google.gson.annotations.Expose;

public class LoginRequest
{
    @Expose
    private String userId; // Corresponds to `user_id` in the table
    @Expose
    private String installId;
    @Expose
    private String username; // Corresponds to `user_name` in the table
    @Expose
    private String pin; // Corresponds to `pin` in the table

    public String getUserId()
    {
        return userId;
    }

    public LoginRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getInstallId()
    {
        return installId;
    }

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPin() {
        return this.pin;
    }

    public LoginRequest setPin(String password) {
        this.pin = password;
        return this;
    }
}