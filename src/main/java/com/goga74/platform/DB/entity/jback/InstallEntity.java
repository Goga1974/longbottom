package com.goga74.platform.DB.entity.jback;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "unlocked")
public class InstallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "install_id")
    private String installId;

    @Column(name = "request_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime requestTime;

    public InstallEntity() {}

    public InstallEntity(final String userId, final String installId, LocalDateTime requestTimeStamp)
    {
        this.installId = installId;
        this.userId = userId;
        this.requestTime = requestTimeStamp;
    }

    public Long getId()
    {
        return id;
    }

    // Удалите setId, если не планируете вручную устанавливать ID
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId)
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

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

}