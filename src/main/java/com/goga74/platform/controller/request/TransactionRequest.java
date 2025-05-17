package com.goga74.platform.controller.request;

import com.goga74.platform.controller.dto.jback.Item;

import java.util.Collection;
import java.util.List;

// Class representing a request to perform a transaction
public class TransactionRequest
{
    private String userId;
    private String installId;
    private String pin;
    private List<Item> delete;
    private List<Item> add;
    private List<Item> unlock;

    // Default constructor
    public TransactionRequest()
    {
    }

    // Parameterized constructor
    public TransactionRequest(String userId, List<Item> itemsDelete, List<Item> itemsAdd,
                              List<Item> itemsUnlock)
    {
        this.userId = userId;
        this.delete = itemsDelete;
        this.add = itemsAdd;
        this.unlock = itemsUnlock;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public List<Item> getItemsDelete()
    {
        return this.delete;
    }

    public void setItemsDelete(List<Item> itemsDelete)
    {
        this.delete = itemsDelete;
    }

    public Collection<? extends Item> getItemsAdd()
    {
        return this.add;
    }

    public void setItemsAdd(List<Item> itemsAdd)
    {
        this.add = itemsAdd;
    }

    public List<Item> getItemsUnlock()
    {
        return this.unlock;
    }

    public void setItemsUnlock(List<Item> itemsUnlock)
    {
        this.unlock = itemsUnlock;
    }
}
