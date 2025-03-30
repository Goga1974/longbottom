package com.goga74.platform.cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goga74.platform.dto.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CacheService
{

    private final int cacheExpirationInSeconds; // Cache expiration time in seconds
    private final Map<String, CacheEntry> cacheMap; // Map to store user cache

    // Constructor with cache expiration parameter
    public CacheService(int cacheExpirationInSeconds)
    {
        this.cacheExpirationInSeconds = cacheExpirationInSeconds;
        this.cacheMap = new HashMap<>();
    }

    // Method to get cached User object by userId
    public User getCachedUser(final String userId)
    {
        CacheEntry entry = cacheMap.get(userId);
        if (entry != null && isCacheValid(entry.getLastUpdated()))
        {
            return entry.getUser();
        }
        return null; // Return null if cache is invalid or not present
    }

    // Method to update User object in cache by userId
    public void updateCache(final String userId, User user)
    {
        cacheMap.put(userId, new CacheEntry(user, LocalDateTime.now()));
    }

    // Check cache validity based on last updated time
    private boolean isCacheValid(LocalDateTime lastUpdated)
    {
        return lastUpdated != null && lastUpdated.isAfter(
                LocalDateTime.now().minusSeconds(cacheExpirationInSeconds));
    }

    // Nested class to store User data and last updated time
    private static class CacheEntry
    {
        private final User user;
        private final LocalDateTime lastUpdated;

        public CacheEntry(User user, LocalDateTime lastUpdated)
        {
            this.user = user;
            this.lastUpdated = lastUpdated;
        }

        public User getUser()
        {
            return user;
        }

        public LocalDateTime getLastUpdated()
        {
            return lastUpdated;
        }
    }
}
