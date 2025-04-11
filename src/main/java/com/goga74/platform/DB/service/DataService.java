package com.goga74.platform.DB.service;

import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.DB.repository.DataRepository;
import com.goga74.platform.DB.repository.ItemRepository;
import com.goga74.platform.DB.repository.RequestLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final DataRepository dataRepository;
    private final ItemRepository itemRepository;
    private final RequestLogRepository requestLogRepository;

    public DataService(DataRepository dataRepository, ItemRepository itemRepository, RequestLogRepository requestLogRepository)
    {
        this.dataRepository = dataRepository;
        this.itemRepository = itemRepository;
        this.requestLogRepository = requestLogRepository;
    }

    public void saveItems(List<ItemEntity> items) {
        itemRepository.saveAll(items);
    }

}