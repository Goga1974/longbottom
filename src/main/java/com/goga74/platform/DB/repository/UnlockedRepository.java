package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.UnlockedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnlockedRepository extends JpaRepository<UnlockedEntity, Long>
{
    List<UnlockedEntity> findByUserId(String userId);

    Optional<UnlockedEntity> findByUserIdAndItemId(String userId, String itemId);

    void deleteByUserId(String userId);
}