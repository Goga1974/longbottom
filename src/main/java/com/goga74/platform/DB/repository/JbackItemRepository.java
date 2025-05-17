package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.jback.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JbackItemRepository extends JpaRepository<ItemEntity, String> {
    List<ItemEntity> findByUserId(String userId);
}