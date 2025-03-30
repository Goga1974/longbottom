package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<UserEntity, String>
{
    UserEntity findByUserId(String userId);
}