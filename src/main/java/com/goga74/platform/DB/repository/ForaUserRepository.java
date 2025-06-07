package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.fora.ForaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForaUserRepository extends JpaRepository<ForaUser, Long>
{
}
