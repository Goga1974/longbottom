package com.goga74.platform.controller.dto.jback;

import com.google.gson.annotations.Expose;

public class Item {

    @Expose
    private String itemId;
    @Expose
    private String userId;
    @Expose
    private int count;

    public String getItemId() {
        return itemId;
    }

    public Item setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Item setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Item setCount(int count) {
        this.count = count;
        return this;
    }
}