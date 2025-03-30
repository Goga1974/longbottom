package com.goga74.platform.dto;

import com.google.gson.annotations.Expose;

public class Item
{
    @Expose
    private String itemId;
    @Expose
    private String itemName;
    @Expose
    private String data;
    private String formattedData; // Example of an additional field if needed

    public String getItemId() {
        return itemId;
    }

    public Item setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public Item setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getData() {
        return data;
    }

    public Item setData(String data) {
        this.data = data;
        // Example of formatting or processing data
        this.formattedData = formatData(data);
        return this;
    }

    // Method for formatting data if needed
    private String formatData(String data) {
        // Example of data processing
        return data.trim();
    }

    public String getFormattedData() {
        return formattedData;
    }
}