package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.WebRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WebRequestRepository extends JpaRepository<WebRequestEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM WebRequestEntity w WHERE w.url = :url")
    void deleteByUrl(String url);
}

