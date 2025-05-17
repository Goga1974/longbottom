package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.jback.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JbackDataRepository extends JpaRepository<UserEntity, String>
{
    UserEntity findByUserId(String userId);
}