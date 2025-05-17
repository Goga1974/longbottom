package com.goga74.platform.DB.dbservice;

import com.goga74.platform.DB.entity.jback.ItemEntity;
import com.goga74.platform.DB.entity.jback.TokenEntity;
import com.goga74.platform.DB.entity.jback.UnlockedEntity;
import com.goga74.platform.DB.entity.jback.UserEntity;
import com.goga74.platform.DB.entity.jback.RequestLog;
import com.goga74.platform.DB.repository.*;
import com.goga74.platform.controller.dto.jback.Item;
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
public class JbackDataService {

    private final JbackDataRepository JBackDataRepository;
    private final JbackItemRepository jbackItemRepository;
    private final JbackRequestLogRepository jbackRequestLogRepository;
    private final JbackUnlockedRepository jbackUnlockedRepository;
    private final TokenRepository tokenRepository;
    private final JbackInstallRepository jbackInstallRepository;
    private final JWTUtil jwtUtil;

    public JbackDataService(JbackDataRepository JBackDataRepository,
                            JbackItemRepository jbackItemRepository,
                            JbackRequestLogRepository jbackRequestLogRepository,
                            JbackUnlockedRepository jbackUnlockedRepository,
                            TokenRepository tokenRepository,
                            JbackInstallRepository jbackInstallRepository,
                            JWTUtil jwtUtil) {
        this.JBackDataRepository = JBackDataRepository;
        this.jbackItemRepository = jbackItemRepository;
        this.jbackRequestLogRepository = jbackRequestLogRepository;
        this.jbackUnlockedRepository = jbackUnlockedRepository;
        this.tokenRepository = tokenRepository;
        this.jbackInstallRepository = jbackInstallRepository;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> getUser(String userId) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserEntity> userOptional = JBackDataRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            response.put("userId", userId);
            response.put("userName", user.getUserName());

            // Получаем элементы пользователя
            List<ItemEntity> items = jbackItemRepository.findByUserId(userId);
            response.put("items", items);

            // Получаем разблокированные элементы
            List<UnlockedEntity> unlockedItems = jbackUnlockedRepository.findByUserId(userId);
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
        jbackRequestLogRepository.save(requestLog);
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
    public String saveUnlocked(final String userId, final List<Item> unlockItems)
    {
        StringBuilder sb = new StringBuilder();
        for (Item unlockItem : unlockItems)
        {
            final String itemId = unlockItem.getItemId();
            Optional<UnlockedEntity> unlockedEntityOptional = jbackUnlockedRepository
                    .findByUserIdAndItemId(userId, itemId);

            if (unlockedEntityOptional.isPresent())
            {
                // Record already exists
                // ToDO: handle?
                if (!sb.isEmpty())
                {
                    sb.append("; ");
                }
                //noinspection MalformedFormatString
                sb.append(String.format("Item %s for user %s already unlocked %s", itemId, userId));
            } else {
                // record not found
                UnlockedEntity newUnlockedEntity = new UnlockedEntity(itemId, userId, 1);
                jbackUnlockedRepository.save(newUnlockedEntity);
            }
        }
        return sb.toString();
    }

    @Transactional
    public void deleteAllUnlockedByUserId(final String userId)
    {
        jbackUnlockedRepository.deleteByUserId(userId);
    }
}