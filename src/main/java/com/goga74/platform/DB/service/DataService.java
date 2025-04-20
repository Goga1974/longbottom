package com.goga74.platform.DB.service;

import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.DB.entity.TokenEntity;
import com.goga74.platform.DB.entity.UnlockedEntity;
import com.goga74.platform.DB.entity.UserEntity;
import com.goga74.platform.DB.entity.RequestLog;
import com.goga74.platform.DB.repository.DataRepository;
import com.goga74.platform.DB.repository.ItemRepository;
import com.goga74.platform.DB.repository.RequestLogRepository;
import com.goga74.platform.DB.repository.TokenRepository;
import com.goga74.platform.DB.repository.UnlockedRepository;
import com.goga74.platform.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final ItemRepository itemRepository;
    private final RequestLogRepository requestLogRepository;
    private final UnlockedRepository unlockedRepository;
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;

    public DataService(DataRepository dataRepository,
                       ItemRepository itemRepository,
                       RequestLogRepository requestLogRepository,
                       UnlockedRepository unlockedRepository,
                       TokenRepository tokenRepository,
                       JWTUtil jwtUtil) {
        this.dataRepository = dataRepository;
        this.itemRepository = itemRepository;
        this.requestLogRepository = requestLogRepository;
        this.unlockedRepository = unlockedRepository;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> getUser(String userId) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserEntity> userOptional = dataRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            response.put("userId", userId);
            response.put("userName", user.getUserName());

            // Получаем элементы пользователя
            List<ItemEntity> items = itemRepository.findByUserId(userId);
            response.put("items", items);

            // Получаем разблокированные элементы
            List<UnlockedEntity> unlockedItems = unlockedRepository.findByUserId(userId);
            response.put("unlocked", unlockedItems);
        } else {
            response.put("ERROR_MESSAGE", "User not found");
        }

        return response;
    }

    public void logRequest(String userId, String ipAddress, String userAgent) {
        // Запись в таблицу request_log
        RequestLog requestLog = new RequestLog();
        requestLog.setUserId(userId);
        requestLog.setIpAddress(ipAddress);
        requestLog.setUserAgent(userAgent);

        // Получение текущего времени в GMT
        ZonedDateTime gmtTime = ZonedDateTime.now(ZoneId.of("GMT"));
        LocalDateTime gmtLocalDateTime = gmtTime.toLocalDateTime();
        requestLog.setRequestTime(gmtLocalDateTime);
        requestLogRepository.save(requestLog);
    }

    public String createToken(final String userId)
    {
        if (userId != null)
        {
            String token = jwtUtil.generateToken(userId);
            // Создание или обновление токена в базе данных
            TokenEntity tokenEntity = tokenRepository.findById(userId).orElse(new TokenEntity());
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setCreatedAt(LocalDateTime.now());
            tokenEntity.setExpiresAt(LocalDateTime.now().plusDays(1)); // 24 часа
            tokenRepository.save(tokenEntity);
            return token;

        }
        return null;
    }

    public String saveToken(final String userId, final String token)
    {
        if (userId != null && token != null)
        {
            // Создание или обновление токена в базе данных
            TokenEntity tokenEntity = tokenRepository.findById(userId).orElse(new TokenEntity());
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setCreatedAt(LocalDateTime.now());
            tokenEntity.setExpiresAt(LocalDateTime.now().plusDays(1)); // 24 часа
            tokenRepository.save(tokenEntity);
            return token;
        }
        return null;
    }

    @Transactional
    public void deleteAllUnlockedByUserId(final String userId)
    {
        unlockedRepository.deleteByUserId(userId);
    }
}