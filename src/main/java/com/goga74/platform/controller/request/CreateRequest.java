package com.goga74.platform.controller.request;

import com.goga74.platform.controller.dto.jback.Item;
import com.google.gson.annotations.Expose;

import java.util.List;

public class CreateRequest {
    @Expose
    private String userId;
    @Expose
    private String installId;
    @Expose
    private String userName;
    @Expose
    private String pin;
    @Expose
    private List<Item> items;
    @Expose
    private List<Unlock> unlocked;

    public CreateRequest() {}

    public CreateRequest(final String userId, final String installId,
                         final String userName, final String pin,
                         List<Item> items, List<Unlock> unlocked)
    {
        this.userId = userId;
        this.installId = installId;
        this.userName = userName;
        this.pin = pin;
        this.items = items;
        this.unlocked = unlocked;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(final String userId)
    {
        this.userId = userId;
    }

    public String getInstallId()
    {
        return installId;
    }

    public void setInstallId(final String installId)
    {
        this.installId = installId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    public String getPin()
    {
        return pin;
    }

    public void setPin(final String pin)
    {
        this.pin = pin;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }

    public List<Unlock> getUnlocked()
    {
        return unlocked;
    }

    public void setUnlocked(List<Unlock> unlocked)
    {
        this.unlocked = unlocked;
    }

    // ToDo - modify because unlocked is a list
    // Inner class or separate class for Unlock
    public static class Unlock
    {
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