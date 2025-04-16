package com.goga74.platform.DB.service;

import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.DB.entity.UnlockedEntity;
import com.goga74.platform.DB.entity.UserEntity;
import com.goga74.platform.DB.repository.DataRepository;
import com.goga74.platform.DB.repository.ItemRepository;
import com.goga74.platform.DB.repository.RequestLogRepository;
import com.goga74.platform.DB.repository.UnlockedRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final ItemRepository itemRepository;
    private final RequestLogRepository requestLogRepository;
    private final UnlockedRepository unlockedRepository;

    public DataService(DataRepository dataRepository,
                       ItemRepository itemRepository,
                       RequestLogRepository requestLogRepository,
                       UnlockedRepository unlockedRepository) {
        this.dataRepository = dataRepository;
        this.itemRepository = itemRepository;
        this.requestLogRepository = requestLogRepository;
        this.unlockedRepository = unlockedRepository;
    }

    /*
    public void saveItems(List<ItemEntity> items) {
        itemRepository.saveAll(items);
    }
     */

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
}