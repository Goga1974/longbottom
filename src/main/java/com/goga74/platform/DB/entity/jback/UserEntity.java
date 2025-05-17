package com.goga74.platform.DB.entity.jback;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class UserEntity
{
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pin")
    private String pin;

    @Column(name = "data", columnDefinition = "JSON")
    private String data;

    public UserEntity() {}

    public UserEntity(String userId, String userName, String data)
    {
        this.userId = userId;
        this.userName = userName;
        this.data = data;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPin()
    {
        return pin;
    }

    public void setPin(String pin)
    {
        this.pin = pin;
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