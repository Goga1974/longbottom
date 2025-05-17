package com.goga74.platform.DB.entity.jback;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "count", columnDefinition = "INT UNSIGNED DEFAULT 0")
    private int count;

    public ItemEntity() {}

    public ItemEntity(String itemId, String userId, int count) {
        this.itemId = itemId;
        this.userId = userId;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    // Удалите setId, если не планируете вручную устанавливать ID
    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}