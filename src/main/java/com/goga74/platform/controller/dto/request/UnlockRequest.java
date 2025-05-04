package com.goga74.platform.controller.dto.request;

import com.goga74.platform.controller.dto.Item;

import java.util.List;

// Class representing a request to unlock an item
public class UnlockRequest {

    // ID of the user performing the unlock operation
    private String userId;

    // List of items to be deleted
    private List<Item> itemsDelete;

    // Item to be unlocked
    private Item itemUnlock;

    // Default constructor
    public UnlockRequest() {
    }

    // Parameterized constructor
    public UnlockRequest(String userId, List<Item> itemsDelete, Item itemUnlock) {
        this.userId = userId;
        this.itemsDelete = itemsDelete;
        this.itemUnlock = itemUnlock;
    }

    // Getter and setter methods
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Item> getItemsDelete() {
        return itemsDelete;
    }

    public void setItemsDelete(List<Item> itemsDelete) {
        this.itemsDelete = itemsDelete;
    }

    public Item getItemUnlock() {
        return itemUnlock;
    }

    public void setItemUnlock(Item itemUnlock) {
        this.itemUnlock = itemUnlock;
    }
}