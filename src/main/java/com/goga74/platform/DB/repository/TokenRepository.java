package com.goga74.platform.DB.repository;

import com.goga74.platform.DB.entity.jback.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    // Вы можете добавить дополнительные методы для работы с токенами, если это необходимо.
}