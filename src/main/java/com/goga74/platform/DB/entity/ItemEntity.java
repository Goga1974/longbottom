package com.goga74.platform.DB.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class ItemEntity
{
    @Id
    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;

    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Column(name = "data", columnDefinition = "JSON", nullable = false)
    private String data;

    public ItemEntity() {}

    public ItemEntity(String itemId, String itemName, String data) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.data = data;
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

}