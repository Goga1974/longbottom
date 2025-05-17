package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.jback.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JbackRequestLogRepository extends JpaRepository<RequestLog, Long> {
    Optional<RequestLog> findFirstByIpAddressOrderByRequestTimeDesc(String ipAddress);
}