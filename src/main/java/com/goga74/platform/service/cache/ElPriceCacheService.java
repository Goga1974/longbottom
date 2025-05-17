package com.goga74.platform.service.cache;

import com.goga74.platform.controller.dto.elprice.PriceEntry;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElPriceCacheService {

    private final int cacheExpirationInSeconds; // Время устаревания кеша
    private final Map<String, CacheEntry> cacheMap; // Мапа для хранения разных кешей

    // Конструктор с параметром времени устаревания
    public ElPriceCacheService(int cacheExpirationInSeconds) {
        this.cacheExpirationInSeconds = cacheExpirationInSeconds;
        this.cacheMap = new HashMap<>();
    }

    // Метод для получения данных из кеша по ключу
    public List<PriceEntry> getCachedPrices(final String key) {
        CacheEntry entry = cacheMap.get(key);
        if (entry != null && isCacheValid(entry.getLastUpdated())) {
            return entry.getPrices();
        }
        return null; // Если кеш невалиден или отсутствует, возвращаем null
    }

    // Метод для обновления данных в кеше по ключу
    public void updateCache(final String key, List<PriceEntry> prices) {
        cacheMap.put(key, new CacheEntry(prices, LocalDateTime.now()));
    }

    // Проверка валидности кеша по времени последнего обновления
    private boolean isCacheValid(LocalDateTime lastUpdated) {
        return lastUpdated != null && lastUpdated.isAfter(
                LocalDateTime.now().minusSeconds(cacheExpirationInSeconds));
    }

    // Вложенный класс для хранения данных и времени обновления
    private static class CacheEntry {
        private final List<PriceEntry> prices;
        private final LocalDateTime lastUpdated;

        public CacheEntry(List<PriceEntry> prices, LocalDateTime lastUpdated) {
            this.prices = prices;
            this.lastUpdated = lastUpdated;
        }

        public List<PriceEntry> getPrices() {
            return prices;
        }

        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }
    }
}
