package com.goga74.platform.controller.dto;

import java.util.List;

public class CreateRequest
{
    private String userId;
    private String userName;
    private String password; // New field for storing the password
    private List<Item> items;

    public CreateRequest() {}

    public CreateRequest(String userId, String userName, String password, List<Item> items)
    {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }
}