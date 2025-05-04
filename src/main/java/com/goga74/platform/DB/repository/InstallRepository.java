package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.InstallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallRepository extends JpaRepository<InstallEntity, Long>
{
    List<InstallEntity> findByUserId(final String userId);

    Optional<InstallEntity> findByUserIdAndInstallId(final String userId, String installId);
}