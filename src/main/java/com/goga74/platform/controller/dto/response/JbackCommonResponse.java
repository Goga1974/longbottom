package com.goga74.platform.controller.dto.response;

import com.goga74.platform.controller.dto.Item;
import com.google.gson.annotations.Expose;
import java.util.List;

public class JbackCommonResponse
{
    @Expose
    private String status;
    @Expose
    private String message;
    //@Expose
    //private Item data; // Assuming data is of type Item
    @Expose
    private String userId;
    @Expose
    private String userName;
    @Expose
    private List<Item> items;

    public String getStatus()
    {
        return status;
    }

    public JbackCommonResponse setStatus(String status)
    {
        this.status = status;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public JbackCommonResponse setMessage(String message)
    {
        this.message = message;
        return this;
    }

    /*
    public Item getData()
    {
        return data;
    }

    public CommonResponse setData(Item data)
    {
        this.data = data;
        return this;
    }
    */

    public String getUserId()
    {
        return userId;
    }

    public JbackCommonResponse setUserId(String userId)
    {
        this.userId = userId;
        return this;
    }

    public String getUserName()
    {
        return userName;
    }

    public JbackCommonResponse setUserName(String userName)
    {
        this.userName = userName;
        return this;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public JbackCommonResponse setItems(List<Item> items)
    {
        this.items = items;
        return this;
    }
}