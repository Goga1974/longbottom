package com.goga74.platform.controller.dto.request;

import com.goga74.platform.controller.dto.Item;

import java.util.List;

public class CreateRequest {
    private String userId;
    private String userName;
    private String password; // New field for storing the password
    private List<Item> items;
    private List<Unlock> unlocked; // New field for unlocked items

    public CreateRequest() {}

    public CreateRequest(String userId, String userName, String password, List<Item> items, List<Unlock> unlocked) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.items = items;
        this.unlocked = unlocked;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Unlock> getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(List<Unlock> unlocked) {
        this.unlocked = unlocked;
    }

    // Inner class or separate class for Unlock
    public static class Unlock {
        private String itemId;

        public Unlock() {}

        public Unlock(String itemId) {
            this.itemId = itemId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
    }
}