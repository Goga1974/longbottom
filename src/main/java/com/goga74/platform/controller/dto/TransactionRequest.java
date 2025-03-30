package com.goga74.platform.controller.dto;

import java.util.Collection;
import java.util.List;

// Class representing a request to perform a transaction
public class TransactionRequest {

    // ID of the user performing the transaction
    private String userId;

    // List of items to be deleted
    private List<Item> itemsDelete;

    // List of items to be added
    private List<Item> itemsAdd;

    // Default constructor
    public TransactionRequest() {
    }

    // Parameterized constructor
    public TransactionRequest(String userId, List<Item> itemsDelete, List<Item> itemsAdd) {
        this.userId = userId;
        this.itemsDelete = itemsDelete;
        this.itemsAdd = itemsAdd;
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

    public Collection<? extends Item> getItemsAdd() {
        return itemsAdd;
    }

    public void setItemsAdd(List<Item> itemsAdd) {
        this.itemsAdd = itemsAdd;
    }
}
